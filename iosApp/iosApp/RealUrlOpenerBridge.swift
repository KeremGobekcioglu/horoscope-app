import Shared
import UIKit

class RealUrlOpenerBridge: SwiftUrlOpenerBridge {
    func open(url: String) {
        guard let nsUrl = URL(string: url) else { return }
        UIApplication.shared.open(nsUrl)
    }
}