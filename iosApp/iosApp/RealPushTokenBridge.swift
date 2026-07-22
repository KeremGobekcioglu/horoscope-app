import FirebaseMessaging
import Shared

class RealPushTokenBridge: SwiftPushTokenBridge {
    func getToken() async throws -> String? {
        // Wait for APNs token (or simulator bypass / timeout)
        await APNsTokenWaiter.shared.waitForToken()

        // Instead of actively calling Messaging.messaging().token(),
        // wait for the token Firebase delivers on its own via
        // MessagingDelegate.didReceiveRegistrationToken (captured in
        // AppDelegate, surfaced here through FCMTokenWaiter). Calling
        // Messaging.messaging().token() directly here caused a
        // TOO_MANY_REGISTRATIONS conflict with that same delegate callback.
        return await FCMTokenWaiter.shared.waitForToken()
    }
}