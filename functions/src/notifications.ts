// notifications.ts
import { onSchedule } from "firebase-functions/v2/scheduler";
import * as admin from "firebase-admin";

// Days of inactivity before a device gets a nudge.
// Set to 0 for testing — nudges every device on every run.
const INACTIVE_DAYS = 0;

// Minimum days between nudges to the same device, so a long-lapsed user
// isn't pinged on every daily run. Set to 0 for testing.
const NUDGE_COOLDOWN_DAYS = 0;

// FCM accepts at most 500 tokens per sendEachForMulticast call.
const BATCH_SIZE = 500;

const NOTIFICATION_TITLE = "Yıldızname";
const NOTIFICATION_BODY = "Bugünün yorumu seni bekliyor ✨";

// Must match the channel created in MainActivity, or Android silently
// drops the notification.
const ANDROID_CHANNEL_ID = "daily_readings";

export const sendInactivityNudges = onSchedule(
  {
      // ⚠️ TESTING SCHEDULE — revert to "0 11 * * *" before production.
    schedule: "*/15 * * * *",
    timeZone: "Europe/Istanbul",
    timeoutSeconds: 540,
  },
  async () => {
    const db = admin.firestore();
    const now = Date.now();
    const inactiveBefore = new Date(now - INACTIVE_DAYS * 86400_000);
    const cooldownBefore = new Date(now - NUDGE_COOLDOWN_DAYS * 86400_000);

    // lastUpdatedAt is written on every app launch (saveDeviceToken), so it
    // doubles as "last time this device opened the app".
    const snap = await db
      .collection("users")
      .where("lastUpdatedAt", "<", inactiveBefore)
      .get();

    // Filter out devices nudged too recently. Done in memory rather than as a
    // second where() clause because Firestore can't range-filter two different
    // fields in one query, and docs never nudged before have no lastNotifiedAt.
    const targets = snap.docs.filter((doc) => {
      const lastNotified = doc.get("lastNotifiedAt");
      if (!lastNotified) return true; // never nudged
      return lastNotified.toDate() < cooldownBefore;
    });

    if (targets.length === 0) {
      console.log("No devices to nudge.");
      return;
    }

    console.log(`Nudging ${targets.length} device(s).`);

    for (let i = 0; i < targets.length; i += BATCH_SIZE) {
      const chunk = targets.slice(i, i + BATCH_SIZE);
      await sendChunk(db, chunk);
    }
  }
);

async function sendChunk(
  db: admin.firestore.Firestore,
  docs: admin.firestore.QueryDocumentSnapshot[]
): Promise<void> {
  const tokens = docs.map((d) => d.get("fcmToken") as string);

  const response = await admin.messaging().sendEachForMulticast({
    tokens,
    notification: {
      title: NOTIFICATION_TITLE,
      body: NOTIFICATION_BODY,
    },
    android: {
      notification: { channelId: ANDROID_CHANNEL_ID },
    },
  });

  // Results come back in the same order as the tokens array.
  const batch = db.batch();
  let deleted = 0;
  let nudged = 0;

  response.responses.forEach((result, idx) => {
    const doc = docs[idx];

    if (result.success) {
      batch.update(doc.ref, {
        lastNotifiedAt: admin.firestore.FieldValue.serverTimestamp(),
      });
      nudged++;
      return;
    }

    // These two codes mean the token is permanently dead (app uninstalled,
    // data cleared, token rotated). Delete the doc — it can never receive
    // anything again. Other errors (network, quota) are transient: leave the
    // doc alone so the next run retries.
    const code = result.error?.code ?? "";
    if (
      code === "messaging/registration-token-not-registered" ||
      code === "messaging/invalid-registration-token"
    ) {
      batch.delete(doc.ref);
      deleted++;
    } else {
      console.warn(`Send failed for ${doc.id}:`, result.error?.message);
    }
  });

  await batch.commit();
  console.log(`Chunk done — nudged: ${nudged}, cleaned up dead tokens: ${deleted}`);
}