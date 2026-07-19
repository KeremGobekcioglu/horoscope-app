import FirebaseMessaging
import Shared

class RealPushTokenBridge: SwiftPushTokenBridge {
    func getToken() async throws -> String? {
        try await Messaging.messaging().token()
    }
}