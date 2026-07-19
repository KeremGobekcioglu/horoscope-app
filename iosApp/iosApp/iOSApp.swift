import SwiftUI
import Shared
import FirebaseCore

@main
struct iOSApp: App {
    init() {
        FirebaseApp.configure()
        MainViewControllerKt.doInitKoin()
        SwiftPushTokenBridgeKt.swiftPushTokenBridge = RealPushTokenBridge()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
