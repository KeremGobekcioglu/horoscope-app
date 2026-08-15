import Shared
import SafariServices
import UIKit

class RealUrlOpenerBridge: SwiftUrlOpenerBridge {
    func open(url: String) {
        guard let nsUrl = URL(string: url) else { return }
        guard let rootVC = UIApplication.shared.connectedScenes
            .compactMap({ $0 as? UIWindowScene })
            .flatMap({ $0.windows })
            .first(where: { $0.isKeyWindow })?.rootViewController else { return }
        topViewController(from: rootVC).present(SFSafariViewController(url: nsUrl), animated: true)
    }

    private func topViewController(from base: UIViewController) -> UIViewController {
        if let nav = base as? UINavigationController, let visible = nav.visibleViewController {
            return topViewController(from: visible)
        }
        if let tab = base as? UITabBarController, let selected = tab.selectedViewController {
            return topViewController(from: selected)
        }
        if let presented = base.presentedViewController {
            return topViewController(from: presented)
        }
        return base
    }
}