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

    // RealNotificationPermissionBridge.swift
    func currentStatus() async throws -> KotlinInt {
        let settings = await UNUserNotificationCenter.current().notificationSettings()
        switch settings.authorizationStatus {
        case .authorized, .provisional, .ephemeral:
            return 2 // GRANTED
        case .denied:
            return 1 // DENIED
        case .notDetermined:
            return 0 // NOT_DETERMINED
        @unknown default:
            return 0
        }
    }
}