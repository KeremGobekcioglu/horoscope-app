import UserNotifications
import UIKit
import Shared

class RealNotificationPermissionBridge: SwiftNotificationPermissionBridge {
    @MainActor
    func requestPermission() async throws -> KotlinBoolean {
        let center = UNUserNotificationCenter.current()
        let granted = try await center.requestAuthorization(options: [.alert, .badge, .sound])

        if granted {
            UIApplication.shared.registerForRemoteNotifications()
        }

        return KotlinBoolean(bool: granted)
    }
}