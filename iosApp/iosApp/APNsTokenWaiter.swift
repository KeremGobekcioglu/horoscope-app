// A small shared coordinator both the AppDelegate and the token bridge use

// WARNING: This waiter has no timeout and no safety check of its own.
// It fully trusts that getToken() is only ever called after notification
// permission has been granted (which is what triggers
// registerForRemoteNotifications(), which is what eventually fires
// didRegisterForRemoteNotificationsWithDeviceToken and calls markTokenReceived()).
//
// If getToken() is ever called when permission was NOT granted, this waiter
// has no way to know that — registerForRemoteNotifications() was never called,
// so markTokenReceived() will never fire, and waitForToken() will hang forever.
//
// This guarantee is currently enforced by RegisterDeviceForNotificationsUseCase
// in commonMain (Kotlin), which checks permission before ever calling
// currentToken(). If a new call path to getToken() is ever added elsewhere,
// it must preserve that same check first.

import Foundation

final class APNsTokenWaiter {
    static let shared = APNsTokenWaiter()

    private var continuation: CheckedContinuation<Void, Never>?
    private var didReceiveToken = false

    // Clean preprocessor check defined OUTSIDE the async function
    private var isSimulator: Bool {
        #if targetEnvironment(simulator)
        return true
        #else
        return false
        #endif
    }

    func markTokenReceived() {
        didReceiveToken = true
        continuation?.resume()
        continuation = nil
    }

    func waitForToken() async {
        if didReceiveToken { return }

        // Standard Swift 'if' statement - zero syntax errors
        if isSimulator {
            return
        }

        await withCheckedContinuation { cont in
            self.continuation = cont
        }
    }
}


// final class APNsTokenWaiter {
//     static let shared = APNsTokenWaiter()
//
//     private var continuation: CheckedContinuation<Void, Never>?
//     private var didReceiveToken = false
//
//     // Helper property to check simulator environment
//     private var isSimulator: Bool {
//         #if targetEnvironment(simulator)
//         return true
//         #else
//         return false
//         #endif
//     }
//
//     func markTokenReceived() {
//         didReceiveToken = true
//         continuation?.resume()
//         continuation = nil
//     }
//
//     func waitForToken() async {
//         // 1. Instant pass if token already arrived
//         if didReceiveToken { return }
//
//         // 2. Bypass wait on Simulator
//         if isSimulator {
//             print("APNsTokenWaiter: Running on Simulator — skipping APNs wait.")
//             return
//         }
//
//         // 3. Wait for APNs token on physical devices with a 5-second timeout
//         await withCheckedContinuation { cont in
//             self.continuation = cont
//
//             Task {
//                 try? await Task.sleep(nanoseconds: 5_000_000_000)
//                 if !self.didReceiveToken {
//                     print("APNsTokenWaiter: Timed out waiting for APNs token.")
//                     self.markTokenReceived()
//                 }
//             }
//         }
//     }
// }