import SwiftUI
import Shared
import FirebaseCore

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate
    init() {
        FirebaseApp.configure()
        MainViewControllerKt.doInitKoin()
        SwiftPushTokenBridgeKt.swiftPushTokenBridge = RealPushTokenBridge()
        SwiftNotificationPermissionBridgeKt.swiftNotificationPermissionBridge = RealNotificationPermissionBridge()
        SwiftSettingsOpenerBridgeKt.swiftSettingsOpenerBridge = RealSettingsOpenerBridge()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
