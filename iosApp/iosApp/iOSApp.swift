import SwiftUI
import Shared
import FirebaseCore

@main
struct iOSApp: App {
    init() {
        FirebaseApp.configure()
        MainViewControllerKt.doInitKoin()
        SwiftPushTokenBridgeKt.swiftPushTokenBridge = RealPushTokenBridge()
        SwiftNotificationPermissionBridgeKt.swiftNotificationPermissionBridge = RealNotificationPermissionBridge()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
