/**
 * Import function triggers from their respective submodules:
 *
 * import {onCall} from "firebase-functions/v2/https";
 * import {onDocumentWritten} from "firebase-functions/v2/firestore";
 *
 * See a full list of supported triggers at https://firebase.google.com/docs/functions
 */

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
// gemini-2.0-flash was REMOVED — it was shut down June 1, 2026 (returns 404).
// At 12 calls/day you will never be rate-limited; this cascade is just insurance.
// If you want to be future-proof on free tier, swap the 2.5 entries for
// "gemini-3.1-flash-lite". Verify live free quotas for your project in AI Studio.
const MODELS = ["gemini-3.5-flash", "gemini-2.5-flash", "gemini-2.5-flash-lite"];

// JSON schema forces a clean { text } payload — no preamble, no markdown fences.
const RESPONSE_SCHEMA = {
  type: "object",
  properties: {
    text: {
      type: "string",
      description: "Türkçe, yeniden yazılmış günlük burç yorumu. Sadece yorum metni.",
    },
  },
  required: ["text"],
};

export const generateDailyReadings = onSchedule(
  {
    schedule: "0 3 * * *",
    timeZone: "Europe/Istanbul",
    secrets: [GEMINI_API_KEY],
    timeoutSeconds: 300,
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
        await sleep(15000);
      try {
        // Skip if already generated (avoids re-spending Gemini on scheduler retries).
        const existing = await db.collection("readings").doc(docId).get();
        if (existing.exists) {
          console.log(`Skip (exists): ${docId}`);
          continue;
        }

        // 1. Fetch from freehoroscopeapi — guard against bad responses.
        const res = await fetch(
          `https://freehoroscopeapi.com/api/v1/get-horoscope/daily?sign=${sign}`
        );
        if (!res.ok) throw new Error(`API ${res.status} for ${sign}`);

        const json = await res.json();
        const rawText: string = json?.data?.horoscope ?? "";
        if (!rawText.trim()) throw new Error(`Empty horoscope for ${sign}`);

        // 2. Rewrite in Turkish via Gemini (JSON output + model fallback + retry).
        const turkishText = await rewriteWithGemini(ai, rawText, sign);

        // 3. If translation failed, do NOT write — never store English as final,
        //    never clobber a previously-good doc. App fallback handles the gap.
        if (turkishText === null) {
          console.error(`Skip write (translation failed): ${docId}`);
          continue;
        }

        // 4. Write to Firestore.
        await db.collection("readings").doc(docId).set({
          textTr: turkishText,
          textEn: rawText,        // store original English, free, no Gemini needed
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

// Returns translated Turkish text, or null if every model + attempt fails.
async function rewriteWithGemini(
  ai: GoogleGenAI,
  text: string,
  sign: string
): Promise<string | null> {
  const prompt =
    `Sen bir astroloji uzmanısın. Aşağıdaki İngilizce günlük burç ` +
    `yorumunu Türkçeye çevir ve yeniden yaz. Samimi, akıcı ve ilham ` +
    `verici bir dil kullan.\n\nBurç: ${sign}\nMetin: ${text}`;

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
        const parsed = JSON.parse(raw) as { text?: unknown };
        const out = typeof parsed.text === "string" ? parsed.text.trim() : "";
        if (out) return out;
      } catch (e) {
        const msg = String(e);
        const rateLimited = msg.includes("429") || /quota|rate.?limit/i.test(msg);
        console.warn(`${model} attempt ${attempt} failed:`, e);

        // A 429 won't clear in 2s — jump to the next model instead of burning retries.
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
// Start writing functions
// https://firebase.google.com/docs/functions/typescript

// For cost control, you can set the maximum number of containers that can be
// running at the same time. This helps mitigate the impact of unexpected
// traffic spikes by instead downgrading performance. This limit is a
// per-function limit. You can override the limit for each function using the
// `maxInstances` option in the function's options, e.g.
// `onRequest({ maxInstances: 5 }, (req, res) => { ... })`.
// NOTE: setGlobalOptions does not apply to functions using the v1 API. V1
// functions should each use functions.runWith({ maxInstances: 10 }) instead.
// In the v1 API, each function can only serve one request per container, so
// this will be the maximum concurrent request count.
//setGlobalOptions({ maxInstances: 10 });

// export const helloWorld = onRequest((request, response) => {
//   logger.info("Hello logs!", {structuredData: true});
//   response.send("Hello from Firebase!");
// });
