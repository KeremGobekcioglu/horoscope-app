import Foundation

final class FCMTokenWaiter {
    static let shared = FCMTokenWaiter()

    private var continuation: CheckedContinuation<String?, Never>?
    private var capturedToken: String?

    // Called by AppDelegate whenever Firebase hands us a token
    func receiveToken(_ token: String?) {
        capturedToken = token
        continuation?.resume(returning: token)
        continuation = nil
    }

    // Called by RealPushTokenBridge to wait for a token to become available
    func waitForToken() async -> String? {
        if let token = capturedToken {
            return token
        }
        return await withCheckedContinuation { cont in
            self.continuation = cont
        }
    }
}