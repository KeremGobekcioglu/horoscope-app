import Shared

class RealSettingsOpenerBridge: SwiftSettingsOpenerBridge {
    func open() {
        guard let url = URL(string: UIApplication.openSettingsURLString) else { return }
        UIApplication.shared.open(url)
    }
}