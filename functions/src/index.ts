import { onSchedule } from "firebase-functions/v2/scheduler";
import * as admin from "firebase-admin";
import { GoogleGenAI } from "@google/genai";
import { defineSecret } from "firebase-functions/params";
admin.initializeApp();

const GEMINI_API_KEY = defineSecret("GEMINI_API_KEY");

const SIGNS = [
  "aries", "taurus", "gemini", "cancer", "leo", "virgo",
  "libra", "scorpio", "sagittarius", "capricorn", "aquarius", "pisces",
];

// Fallback chain, best-first. All Flash-family = free-tier eligible.
// gemini-2.0-flash was REMOVED — shut down June 1, 2026 (returns 404).
const MODELS = ["gemini-3.5-flash", "gemini-2.5-flash", "gemini-2.5-flash-lite"];

// Retry cascade — deliberately EXCLUDES 3.5. A sign only goes missing after the
// full cascade fails, and the usual culprit is "3.5 flash is busy" at the 00:00
// spike. The in-run retry pass skips 3.5 entirely and uses the 2.5 models only.
const RETRY_MODELS = ["gemini-2.5-flash", "gemini-2.5-flash-lite"];

// Hard cap on a single generateContent call. The @google/genai SDK's own
// timeout (httpOptions.timeout) is reported broken for generateContent, and
// undici's default lets a stalled request hang ~5 min (observed as
// UND_ERR_HEADERS_TIMEOUT in the logs). Promise.race abandons the await after
// this many ms regardless of what the SDK does, so a hung call fails fast and
// the loop moves to the next attempt/model instead of eating the timeout budget.
const GENERATE_TIMEOUT_MS = 25000;

// The freehoroscopeapi source draws all 12 signs from ~3-4 recycled templates
// and is non-deterministic across time. So we no longer TRANSLATE it faithfully
// (that just propagated the repetition) — we use it as a loose seed and have
// Gemini ENRICH it into original, date-specific text in both languages.
// generalEn is now GENERATED (Gemini's enriched English), not the raw API text.
const RESPONSE_SCHEMA = {
  type: "object",
  properties: {
    generalTr: { type: "string", description: "Zengin, özgün Türkçe genel yorum. Ham metnin birebir çevirisi DEĞİL." },
    generalEn: { type: "string", description: "Rich, original English general reading. NOT a copy of the seed text." },
    loveTr:   { type: "string", description: "Aşk ve ilişkiler, Türkçe. 2-3 cümle." },
    loveEn:   { type: "string", description: "Love and relationships, English. 2-3 sentences." },
    workTr:   { type: "string", description: "Kariyer ve iş, Türkçe. 2-3 cümle." },
    workEn:   { type: "string", description: "Career and work, English. 2-3 sentences." },
    healthTr: { type: "string", description: "Sağlık ve enerji, Türkçe. 2-3 cümle." },
    healthEn: { type: "string", description: "Health and energy, English. 2-3 sentences." },
    luckTr:   { type: "string", description: "Şans ve fırsatlar, Türkçe. 2-3 cümle." },
    luckEn:   { type: "string", description: "Luck and opportunities, English. 2-3 sentences." },
  },
  required: [
    "generalTr","generalEn","loveTr","loveEn","workTr","workEn",
    "healthTr","healthEn","luckTr","luckEn",
  ],
};

interface GeminiReadingResult {
  generalTr: string; generalEn: string;
  loveTr: string;   loveEn: string;
  workTr: string;   workEn: string;
  healthTr: string; healthEn: string;
  luckTr: string;   luckEn: string;
}

export const generateDailyReadings = onSchedule(
  {
    schedule: "0 0 * * *",
    timeZone: "Europe/Istanbul",
    secrets: [GEMINI_API_KEY],
    timeoutSeconds: 540, // bumped from 300 — larger payload + 15s gaps per sign
  },
  async () => {
    const db = admin.firestore();
    // Istanbul-local date, YYYY-MM-DD — must match how the app computes "today".
    const today = new Intl.DateTimeFormat("en-CA", {
      timeZone: "Europe/Istanbul",
    }).format(new Date());

    const ai = new GoogleGenAI({ apiKey: GEMINI_API_KEY.value() });

    // First pass — full cascade (incl. 3.5), 15s gap for the 00:00 spike.
    for (const sign of SIGNS) {
      await generateForSign(db, ai, sign, today, MODELS, 15000);
    }

    // In-run retry pass — only signs whose doc is still missing (all models
    // failed above, usually a 3.5-busy spike). Skips 3.5 via RETRY_MODELS and
    // uses a shorter gap since the retry set is small and the spike has eased.
    const missing = await missingSigns(db, today);
    if (missing.length > 0) {
      console.warn(`Retry pass for missing signs: ${missing.join(", ")}`);
      for (const sign of missing) {
        await generateForSign(db, ai, sign, today, RETRY_MODELS, 5000);
      }

      const stillMissing = await missingSigns(db, today);
      if (stillMissing.length > 0) {
        console.error(`Still missing after retry: ${stillMissing.join(", ")}`);
      }
    }
  }
);

// Returns the sub-list of SIGNS that have no daily doc for `date` yet.
async function missingSigns(
  db: admin.firestore.Firestore,
  date: string
): Promise<string[]> {
  const checks = await Promise.all(
    SIGNS.map(async (sign) => {
      const snap = await db
        .collection("readings")
        .doc(`${sign}_${date}_daily`)
        .get();
      return snap.exists ? null : sign;
    })
  );
  return checks.filter((s): s is string => s !== null);
}

// Generate + write one sign. No-op if the doc already exists. Never writes a
// partial doc. Swallows its own errors so one bad sign can't abort the batch.
async function generateForSign(
  db: admin.firestore.Firestore,
  ai: GoogleGenAI,
  sign: string,
  today: string,
  models: string[],
  sleepMs: number
): Promise<void> {
  const docId = `${sign}_${today}_daily`;

  try {
    // Skip if already generated — fast path (no sleep, no Gemini call).
    const existing = await db.collection("readings").doc(docId).get();
    if (existing.exists) {
      console.log(`Skip (exists): ${docId}`);
      return;
    }

    // Free-tier rate limit (5/min) — gap only when about to call Gemini.
    await sleep(sleepMs);

    // 1. Fetch from freehoroscopeapi — used only as a loose seed now.
    const res = await fetch(
      `https://freehoroscopeapi.com/api/v1/get-horoscope/daily?sign=${sign}`
    );
    if (!res.ok) throw new Error(`API ${res.status} for ${sign}`);

    const json = await res.json();
    const rawText: string = json?.data?.horoscope ?? "";
    if (!rawText.trim()) throw new Error(`Empty horoscope for ${sign}`);

    // 2. Enrich seed into original bilingual reading + 4 categories.
    const result = await rewriteWithGemini(ai, rawText, sign, today, models);

    // 3. On failure, do NOT write — never store partial as final, never
    //    clobber a good doc. App fallback handles the gap.
    if (result === null) {
      console.error(`Skip write (generation failed): ${docId}`);
      return;
    }

    // 4. Write to Firestore. textEn is now Gemini's enriched English so it
    //    stays in sync with textTr (both diverge from the raw seed).
    await db.collection("readings").doc(docId).set({
      textTr: result.generalTr,
      textEn: result.generalEn,
      textLoveTr: result.loveTr,     textLoveEn: result.loveEn,
      textWorkTr: result.workTr,     textWorkEn: result.workEn,
      textHealthTr: result.healthTr, textHealthEn: result.healthEn,
      textLuckTr: result.luckTr,     textLuckEn: result.luckEn,
      scoreLove:   pseudoScore(sign, today, "love"),
      scoreWork:   pseudoScore(sign, today, "work"),
      scoreHealth: pseudoScore(sign, today, "health"),
      scoreLuck:   pseudoScore(sign, today, "luck"),
      translated: true,
    });

    console.log(`Written: ${docId}`);
  } catch (e) {
    console.error(`Failed for ${sign}:`, e);
  }
}

// Returns the parsed result, or null if every model + attempt fails.
async function rewriteWithGemini(
  ai: GoogleGenAI,
  seed: string,
  sign: string,
  date: string,
  models: string[]
): Promise<GeminiReadingResult | null> {
  const prompt =
    `Sen deneyimli bir astrologsun. Aşağıda bir burç için ham, şablon gibi ` +
    `yazılmış İngilizce bir günlük yorum taslağı var.\n\n` +
    `ÖNEMLİ: Bu taslağı BİREBİR ÇEVİRME. Onu yalnızca bir ilham / başlangıç ` +
    `noktası olarak kullan ve ${sign} burcu için ${date} tarihine özel, daha ` +
    `zengin, özgün ve akıcı bir günlük yorum üret.\n\n` +
    `Kurallar:\n` +
    `- Taslaktaki genel temayı koruyabilirsin ama kendi kelimelerinle, daha ` +
    `derin, kişisel ve canlı bir dille yeniden yaz.\n` +
    `- Klişe kalıplardan kaçın ("bir yapılacaklar listesi yap", "yardım ` +
    `istemekten çekinme", "her şeyin üstesinden gelebilirsin" gibi) — bunları ` +
    `daha doğal ve özgün ifadelerle değiştir.\n` +
    `- Tarihi bir çeşitlilik çıpası olarak kullan: aynı burç için farklı ` +
    `günlerde belirgin şekilde farklı yorumlar üret.\n` +
    `- generalTr (Türkçe) ve generalEn (İngilizce) AYNI içeriği versin; biri ` +
    `diğerinin doğal çevirisi olsun.\n\n` +
    `Ardından şu 4 kategori için hem Türkçe hem İngilizce kısa yorumlar üret ` +
    `(her biri 2-3 cümle):\n` +
    `   - Aşk ve ilişkiler (loveTr / loveEn)\n` +
    `   - Kariyer ve iş (workTr / workEn)\n` +
    `   - Sağlık ve enerji (healthTr / healthEn)\n` +
    `   - Şans ve fırsatlar (luckTr / luckEn)\n\n` +
    `Samimi, akıcı ve ilham verici bir dil kullan.\n\n` +
    `Burç: ${sign}\nTarih: ${date}\nHam İngilizce taslak: ${seed}`;

  const fields: (keyof GeminiReadingResult)[] = [
    "generalTr", "generalEn", "loveTr", "loveEn", "workTr", "workEn",
    "healthTr", "healthEn", "luckTr", "luckEn",
  ];

  for (const model of models) {
    for (let attempt = 1; attempt <= 3; attempt++) {
      try {
        const result = await withTimeout(
          ai.models.generateContent({
            model,
            contents: prompt,
            config: {
              responseMimeType: "application/json",
              responseSchema: RESPONSE_SCHEMA,
            },
          }),
          GENERATE_TIMEOUT_MS,
          `${model} generateContent (${sign})`
        );

        const raw = result.text;
        if (!raw) continue;

        // Schema-constrained, but parse defensively anyway.
        const parsed = JSON.parse(raw) as Partial<GeminiReadingResult>;
        const allPresent = fields.every(
          (f) => typeof parsed[f] === "string" && (parsed[f] as string).trim()
        );

        if (allPresent) {
          return Object.fromEntries(
            fields.map((f) => [f, (parsed[f] as string).trim()])
          ) as unknown as GeminiReadingResult;
        }
        // Partial/malformed — treat as failed attempt, retry/fallback.
      } catch (e) {
        const msg = String(e);
        const rateLimited = msg.includes("429") || /quota|rate.?limit/i.test(msg);
        const timedOut = msg.includes("Timeout after");
        console.warn(`${model} attempt ${attempt} failed:`, e);

        // 429 won't clear in 2s — jump to next model instead of burning retries.
        if (rateLimited) break;
        // A timeout means the call hung, not a transient blip — retry same model
        // once more is cheap now (capped at 25s), but don't sleep-backoff first.
        if (timedOut) continue;
        if (attempt < 3) await sleep(2000 * attempt);
      }
    }
    console.warn(`${model} exhausted, trying next model`);
  }

  return null; // signal failure — caller skips the write
}

// generalTr = translation of the API's monthly English paragraph.
// No category split — monthly stays one undifferentiated blob per your decision.
const MONTHLY_RESPONSE_SCHEMA = {
  type: "object",
  properties: {
    monthlyTr: { type: "string", description: "İngilizce aylık yorumun Türkçe çevirisi. Tam çeviri, özet değil." },
  },
  required: ["monthlyTr"],
};

interface GeminiMonthlyResult {
  monthlyTr: string;
}

export const generateMonthlyReadings = onSchedule(
  {
    schedule: "10 0 1 * *", // 1st of month, 00:10 Europe/Istanbul
    timeZone: "Europe/Istanbul",
    secrets: [GEMINI_API_KEY],
    timeoutSeconds: 540,
  },
  async () => {
    const db = admin.firestore();
    // Istanbul-local year-month, YYYY-MM — matches app's month key convention.
    const currentMonth = new Intl.DateTimeFormat("en-CA", {
      timeZone: "Europe/Istanbul",
      year: "numeric",
      month: "2-digit",
    }).format(new Date()).slice(0, 7); // en-CA gives YYYY-MM-DD, slice to YYYY-MM

    const ai = new GoogleGenAI({ apiKey: GEMINI_API_KEY.value() });

    for (const sign of SIGNS) {
      const docId = `${sign}_${currentMonth}_monthly`;

      try {
        const existing = await db.collection("readings").doc(docId).get();
        if (existing.exists) {
          console.log(`Skip (exists): ${docId}`);
          continue;
        }

        await sleep(15000);

        const res = await fetch(
          `https://freehoroscopeapi.com/api/v1/get-horoscope/monthly?sign=${sign}`
        );
        if (!res.ok) throw new Error(`API ${res.status} for ${sign}`);

        const json = await res.json();
        const rawText: string = json?.data?.horoscope ?? "";
        if (!rawText.trim()) throw new Error(`Empty horoscope for ${sign}`);

        const result = await rewriteMonthlyWithGemini(ai, rawText, sign);

        if (result === null) {
          console.error(`Skip write (generation failed): ${docId}`);
          continue;
        }

        await db.collection("readings").doc(docId).set({
          textTr: result.monthlyTr,
          textEn: rawText,
          translated: true,
        });

        console.log(`Written: ${docId}`);
      } catch (e) {
        console.error(`Failed for ${sign}:`, e);
      }
    }
  }
);

async function rewriteMonthlyWithGemini(
  ai: GoogleGenAI,
  text: string,
  sign: string
): Promise<GeminiMonthlyResult | null> {
  const prompt =
    `Sen bir astroloji uzmanısın. Aşağıda bir burç için İngilizce aylık ` +
    `yorum var. Bu yorumu Türkçeye çevir (monthlyTr) — tam çeviri, özet yapma. ` +
    `Samimi, akıcı ve ilham verici bir dil kullan.\n\n` +
    `Burç: ${sign}\nİngilizce aylık yorum: ${text}`;

  for (const model of MODELS) {
    for (let attempt = 1; attempt <= 3; attempt++) {
      try {
        const result = await withTimeout(
          ai.models.generateContent({
            model,
            contents: prompt,
            config: {
              responseMimeType: "application/json",
              responseSchema: MONTHLY_RESPONSE_SCHEMA,
            },
          }),
          GENERATE_TIMEOUT_MS,
          `${model} monthly generateContent (${sign})`
        );

        const raw = result.text;
        if (!raw) continue;

        const parsed = JSON.parse(raw) as Partial<GeminiMonthlyResult>;
        if (typeof parsed.monthlyTr === "string" && parsed.monthlyTr.trim()) {
          return { monthlyTr: parsed.monthlyTr.trim() };
        }
      } catch (e) {
        const msg = String(e);
        const rateLimited = msg.includes("429") || /quota|rate.?limit/i.test(msg);
        const timedOut = msg.includes("Timeout after");
        console.warn(`${model} attempt ${attempt} failed:`, e);

        if (rateLimited) break;
        if (timedOut) continue;
        if (attempt < 3) await sleep(2000 * attempt);
      }
    }
    console.warn(`${model} exhausted, trying next model`);
  }

  return null;
}

// Races a promise against a timer. If `ms` elapses first, rejects with a
// "Timeout after..." error (matched by callers to distinguish from 429/503).
// NOTE: this abandons the AWAIT, not the underlying fetch — the SDK's request
// keeps running in the background until it resolves or undici kills it. That's
// fine here: the loop moves on immediately, and the CF instance is short-lived.
function withTimeout<T>(promise: Promise<T>, ms: number, label: string): Promise<T> {
  return Promise.race([
    promise,
    new Promise<T>((_, reject) =>
      setTimeout(() => reject(new Error(`Timeout after ${ms}ms: ${label}`)), ms)
    ),
  ]);
}

function sleep(ms: number): Promise<void> {
  return new Promise((resolve) => setTimeout(resolve, ms));
}

function pseudoScore(sign: string, date: string, category: string): number {
  const str = sign + date + category;
  let hash = 0;
  for (const ch of str) hash = (Math.imul(31, hash) + ch.charCodeAt(0)) | 0;
  return (Math.abs(hash) % 7) + 4;
}


export { sendInactivityNudges } from "./notifications";