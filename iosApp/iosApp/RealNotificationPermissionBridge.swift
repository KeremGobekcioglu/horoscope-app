// Swift, in iosApp
import UserNotifications
import UIKit
import Shared

class RealNotificationPermissionBridge: SwiftNotificationPermissionBridge {
    func requestPermission() async throws -> KotlinBoolean {
        let center = UNUserNotificationCenter.current()
        let granted = try await center.requestAuthorization(options: [.alert, .badge, .sound])
        if granted {
            await MainActor.run {
                UIApplication.shared.registerForRemoteNotifications()
            }
        }
        return KotlinBoolean(bool: granted)
    }
}