# Incident: August monthly readings showed July content

**Date:** 2026-08-01
**Function:** `generateMonthlyReadings` (Cloud Functions, TypeScript)
**Impact:** All 12 signs' monthly Firestore docs (`{sign}_2026-08_monthly`) contained stale July content — 5 signs explicitly said "Temmuz"/"In July", the other 7 had generic-but-still-July text.

---

## Root cause

`generateMonthlyReadings` runs `"10 0 1 * *"` Europe/Istanbul = **21:10 UTC on the last day of the previous month**. `freehoroscopeapi.com`'s monthly endpoint rolls over on its own clock, which lags Istanbul. At that exact trigger time, the API was still serving July's `horoscope` text (even though this wasn't discovered until later — the API response does include a `data.date` field, e.g. `"2026-08"`, but the function never checked it).

The Gemini rewrite step made it worse: the prompt for monthly asked for **faithful translation** (*"Bu yorumu Türkçeye çevir — tam çeviri, özet yapma"*) with **no date/month parameter passed in at all**. So whatever month the seed text implied (explicitly, for 5 signs — "In August" written as "In July" that week — or implicitly for the other 7) went straight through into Turkish, and `textEn` stored the raw seed verbatim too.

**Why daily was unaffected:** `generateDailyReadings`'s prompt explicitly injects `${date}` and instructs *enrichment*, not translation ("taslağı BİREBİR ÇEVİRME... `${date}` tarihine özel"). The date anchor gave Gemini something to override the stale seed with. Monthly had no such anchor.

## The fix (code)

1. **Guard on the API's own date field** — compare `json.data.date` to the computed `currentMonth`; if they don't match, throw (skips the write, doc stays absent, next run retries):
   ```ts
   const apiMonth: string = json?.data?.date ?? "";
   if (apiMonth !== currentMonth) {
     throw new Error(`Stale seed for ${sign}: API=${apiMonth}, expected=${currentMonth}`);
   }
   ```
2. **Pass the month into the prompt** and forbid inventing/keeping a wrong one:
   ```ts
   const result = await rewriteMonthlyWithGemini(ai, rawText, sign, currentMonth);
   ```
3. **Moved the schedule off the UTC boundary** — `"10 0 1 * *"` → later in the day, to give the upstream API room to roll over before the seed is fetched.

## The second bug: deploy pipeline

Separately, `firebase deploy --only functions` was failing with `npm error code EUSAGE` / `npm ci ... Missing: X from lock file`, repeatedly, even after:
- regenerating `package-lock.json` locally
- confirming `npm ci` passed locally
- committing and pushing the lockfile (confirmed via `git show HEAD:... | grep`)
- updating `firebase-tools` to latest

**Actual cause:** `firebase-functions-test` (an unused devDependency — zero references anywhere in `src/` or any test file) pulled in `jest` → ... → `jest-resolve` → `unrs-resolver`, which fans out into ~24 platform-specific optional binary packages (`@unrs/resolver-binding-*`, `@emnapi/*`, `@napi-rs/wasm-runtime`, etc). These were present and correct in the lockfile, but Cloud Build's `npm ci` environment resolved/validated them differently than local npm 11.5.1 did, and kept rejecting the install.

**Fix:** removed the unused package entirely.
```bash
npm uninstall firebase-functions-test
```
This dropped `unrs-resolver` from the tree completely (`npm ls unrs-resolver` → empty), and the next deploy succeeded.

**Dead end that made this worse:** tried `.npmrc` with `production=true` to skip devDependencies on Cloud Build. This also stripped `typescript` locally (it's a devDependency), breaking `tsc` on the Mac. Reverted. Lesson: don't reach for global install-mode flags to dodge one bad package — find and remove the actual package.

---

## Checklist: before trusting a Cloud Function deploy again

- [ ] `npm run build` passes locally (`tsc` clean)
- [ ] `firebase deploy --only functions:<name>` — watch for **`Successful update operation`**, not `Skipped`. "Skipped (No changes detected)" means the hash matched what's already live — if you just edited the file, that's a red flag, not a pass.
- [ ] If deploy fails on `npm ci`, don't assume the lockfile is wrong — check `npm why <package>` for whatever's listed as missing, and check whether it's coming from an **unused devDependency** before regenerating anything.
- [ ] After a real deploy success, confirm with a source outside the CLI's own success message:
  ```bash
  gcloud functions describe <name> --region=us-central1 --gen2 --format="value(updateTime)"
  ```
  Timestamp should be within the last few minutes.
- [ ] For scheduled functions specifically, the real proof is a live trigger, not a local `functions:shell` call — `functions:shell` runs your *local* build against production data, which proves the logic works but not that the deployed artifact matches. If in doubt:
  ```bash
  gcloud scheduler jobs list --location=us-central1
  gcloud scheduler jobs run <job-name> --location=us-central1
  firebase functions:log --only <name> -n 20
  ```

## Checklist: any function that translates/rewrites API seed content

- [ ] Does the prompt tell the model to translate *faithfully*, or to *enrich/override*? Faithful-translation prompts propagate every flaw (wrong month, stale info, typos) straight through. Enrichment prompts with an explicit anchor (date, month, etc.) let the model correct for a bad seed.
- [ ] Is there a ground-truth field in the API response (date, id, version) that can be checked before trusting the seed content, instead of trusting prose parsing?
- [ ] If the schedule fires near a day/month/UTC boundary, assume the upstream API may not have rolled over yet. Either move the schedule later, or guard on the API's own timestamp field, or both.

## Checklist: local git/npm state before blaming Cloud Build

Before assuming a remote/CI environment mismatch:
- [ ] `git status` clean, nothing uncommitted
- [ ] `git show HEAD:path/to/file | grep <thing>` — confirm what's *actually* committed, not what's on disk
- [ ] `npm why <package>` — find true source before touching lockfiles
- [ ] Check for pre-commit hooks that might silently modify files (`.husky/`, `.git/hooks/`, `core.hooksPath`)
- [ ] `gcloud config get-value project` — confirm you're not accidentally pointed at the wrong GCP project when running `gcloud` commands directly (Firebase CLI scopes correctly on its own; raw `gcloud` does not)