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

// generalTr = translation of the API's English general text.
// generalEn is NOT generated — the raw API paragraph is stored as textEn (free).
// The 4 categories are generated bilingually (no API source exists for them).
const RESPONSE_SCHEMA = {
  type: "object",
  properties: {
    generalTr: { type: "string", description: "İngilizce genel yorumun Türkçe çevirisi. Tam çeviri, özet değil." },
    loveTr:   { type: "string", description: "Aşk ve ilişkiler, Türkçe. 2-3 cümle." },
    loveEn:   { type: "string", description: "Love and relationships, English. 2-3 sentences." },
    workTr:   { type: "string", description: "Kariyer ve iş, Türkçe. 2-3 cümle." },
    workEn:   { type: "string", description: "Career and work, English. 2-3 sentences." },
    healthTr: { type: "string", description: "Sağlık ve enerji, Türkçe. 2-3 cümle." },
    healthEn: { type: "string", description: "Health and energy, English. 2-3 sentences." },
    luckTr:   { type: "string", description: "Şans ve fırsatlar, Türkçe. 2-3 cümle." },
    luckEn:   { type: "string", description: "Luck and opportunities, English. 2-3 sentences." },
  },
  required: ["generalTr","loveTr","loveEn","workTr","workEn","healthTr","healthEn","luckTr","luckEn"],
};

interface GeminiReadingResult {
  generalTr: string;
  loveTr: string;   loveEn: string;
  workTr: string;   workEn: string;
  healthTr: string; healthEn: string;
  luckTr: string;   luckEn: string;
}

export const generateDailyReadings = onSchedule(
  {
    schedule: "0 3 * * *",
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

    for (const sign of SIGNS) {
      const docId = `${sign}_${today}_daily`;

      try {
        // Skip if already generated — fast path on scheduler retries (no sleep).
        const existing = await db.collection("readings").doc(docId).get();
        if (existing.exists) {
          console.log(`Skip (exists): ${docId}`);
          continue;
        }

        // Free-tier rate limit (5/min) — gap only when about to call Gemini.
        await sleep(15000);

        // 1. Fetch from freehoroscopeapi — guard against bad responses.
        const res = await fetch(
          `https://freehoroscopeapi.com/api/v1/get-horoscope/daily?sign=${sign}`
        );
        if (!res.ok) throw new Error(`API ${res.status} for ${sign}`);

        const json = await res.json();
        const rawText: string = json?.data?.horoscope ?? "";
        if (!rawText.trim()) throw new Error(`Empty horoscope for ${sign}`);

        // 2. Translate general + generate 4 bilingual categories in one call.
        const result = await rewriteWithGemini(ai, rawText, sign);

        // 3. On failure, do NOT write — never store partial as final, never
        //    clobber a good doc. App fallback handles the gap.
        if (result === null) {
          console.error(`Skip write (generation failed): ${docId}`);
          continue;
        }

        // 4. Write to Firestore. textEn = raw API English (free, no Gemini).
        await db.collection("readings").doc(docId).set({
          textTr: result.generalTr,
          textEn: rawText,
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
  }
);

// Returns the parsed result, or null if every model + attempt fails.
async function rewriteWithGemini(
  ai: GoogleGenAI,
  text: string,
  sign: string
): Promise<GeminiReadingResult | null> {
  const prompt =
    `Sen bir astroloji uzmanısın. Aşağıda bir burç için İngilizce günlük ` +
    `yorum var.\n\n` +
    `1. Bu İngilizce genel yorumu Türkçeye çevir (generalTr) — tam çeviri, ` +
    `özet yapma.\n` +
    `2. Aynı yorumu temel alarak şu kategoriler için hem Türkçe hem İngilizce ` +
    `kısa yorumlar üret (her biri 2-3 cümle):\n` +
    `   - Aşk ve ilişkiler (loveTr / loveEn)\n` +
    `   - Kariyer ve iş (workTr / workEn)\n` +
    `   - Sağlık ve enerji (healthTr / healthEn)\n` +
    `   - Şans ve fırsatlar (luckTr / luckEn)\n\n` +
    `Samimi, akıcı ve ilham verici bir dil kullan.\n\n` +
    `Burç: ${sign}\nİngilizce genel yorum: ${text}`;

  const fields: (keyof GeminiReadingResult)[] = [
    "generalTr", "loveTr", "loveEn", "workTr", "workEn",
    "healthTr", "healthEn", "luckTr", "luckEn",
  ];

  for (const model of MODELS) {
    for (let attempt = 1; attempt <= 3; attempt++) {
      try {
        const result = await ai.models.generateContent({
          model,
          contents: prompt,
          config: {
            responseMimeType: "application/json",
            responseSchema: RESPONSE_SCHEMA,
          },
        });

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
        console.warn(`${model} attempt ${attempt} failed:`, e);

        // 429 won't clear in 2s — jump to next model instead of burning retries.
        if (rateLimited) break;
        if (attempt < 3) await sleep(2000 * attempt);
      }
    }
    console.warn(`${model} exhausted, trying next model`);
  }

  return null; // signal failure — caller skips the write
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