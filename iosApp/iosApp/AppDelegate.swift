import UIKit
import FirebaseMessaging
import FirebaseCore
import UserNotifications
// Helper property placed OUTSIDE functions to prevent parser issues
private var isRunningOnSimulator: Bool {
    #if targetEnvironment(simulator)
    return true
    #else
    return false
    #endif
}
class AppDelegate: NSObject, UIApplicationDelegate, UNUserNotificationCenterDelegate, MessagingDelegate {
    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil
    ) -> Bool {
        UNUserNotificationCenter.current().delegate = self
        Messaging.messaging().delegate = self

        // Standard Swift 'if' statement - no '#if' parser issues inside function
        if isRunningOnSimulator {
            let dummyApnsToken = Data(repeating: 0, count: 32)
            Messaging.messaging().setAPNSToken(dummyApnsToken, type: .sandbox)
        }

        return true
    }
// Called when APNs successfully registers device
    func application(
        _ application: UIApplication,
        didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data
    ) {
        print("APNs Token Received: \(deviceToken.map { String(format: "%02.2hhx", $0) }.joined())")
        // Pass APNs token directly to FCM
        Messaging.messaging().apnsToken = deviceToken

        // Unblock your custom KMP waiter
        APNsTokenWaiter.shared.markTokenReceived()
    }
    // Required MessagingDelegate implementation
    func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?) {
        print("FCM Token updated: \(fcmToken ?? "nil")")
        FCMTokenWaiter.shared.receiveToken(fcmToken)
    }
    func application(
        _ application: UIApplication,
        didFailToRegisterForRemoteNotificationsWithError error: Error
    ) {
        print("Failed to register for APNs: \(error.localizedDescription)")
    }
}