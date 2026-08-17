//
// Created by Kerem Göbekcioğlu on 17.08.2026.
//

import UIKit
import Photos
import Shared

/// Swift implementation of the share operations Kotlin declares in SwiftShareBridge.
///
/// Every method must call `onResult` exactly once on every path. Twice throws
/// "Already resumed" in Kotlin; zero times leaves the coroutine parked forever, which
/// silently bricks the share sheet (isWorking never clears).
class RealShareBridge: SwiftShareBridge {
    func doCopyImage(png: KotlinByteArray, onResult: @escaping (any ShareResult) -> Void) {
        guard let image = toImage(png) else {
            onResult(ShareResultFailed(cause: nil))
            return
        }
        UIPasteboard.general.image = image
        onResult(ShareResultSuccess())
    }


    // MARK: - Helpers

    /// Kotlin hands us a KotlinByteArray; UIKit wants Data / UIImage.
    private func toImage(_ png: KotlinByteArray) -> UIImage? {
        var bytes = [UInt8]()
        bytes.reserveCapacity(Int(png.size))
        for i in 0..<png.size {
            bytes.append(UInt8(bitPattern: png.get(index: i)))
        }
        return UIImage(data: Data(bytes))
    }

    /// The view controller to present sheets from. Walks past anything already presented,
    /// otherwise presenting over an existing modal silently does nothing.
    private func topViewController() -> UIViewController? {
        let scene = UIApplication.shared.connectedScenes
        .compactMap { $0 as? UIWindowScene }
        .first { $0.activationState == .foregroundActive }

        guard var top = scene?.windows.first(where: { $0.isKeyWindow })?.rootViewController else {
            return nil
        }
        while let presented = top.presentedViewController {
            top = presented
        }
        return top
    }

    private func presentActivitySheet(items: [Any], onResult: @escaping (ShareResult) -> Void) {
        guard let top = topViewController() else {
            onResult(ShareResultFailed(cause: nil))
            return
        }

        let vc = UIActivityViewController(activityItems: items, applicationActivities: nil)

        // iPad presents this as a popover and *crashes* without an anchor. The app targets
        // iPhone only, so iPad runs it in compatibility mode where this is never consulted —
        // but leaving it unset means enabling iPad support later ships a hard crash.
        if let popover = vc.popoverPresentationController {
            popover.sourceView = top.view
            popover.sourceRect = CGRect(
                x: top.view.bounds.midX,
                y: top.view.bounds.maxY,
                width: 0,
                height: 0
            )
            popover.permittedArrowDirections = []
        }

        vc.completionWithItemsHandler = { _, completed, _, error in
            if let error = error {
                onResult(ShareResultFailed(cause: KotlinThrowable(message: error.localizedDescription)))
            } else if completed {
                onResult(ShareResultSuccess())
            } else {
                // User dismissed without picking — deliberate, not an error.
                onResult(ShareResultCancelled())
            }
        }

        top.present(vc, animated: true)
    }

    // MARK: - SwiftShareBridge

    func shareImage(png: KotlinByteArray, onResult: @escaping (ShareResult) -> Void) {
        guard let image = toImage(png) else {
            onResult(ShareResultFailed(cause: nil))
            return
        }
        presentActivitySheet(items: [image], onResult: onResult)
    }

    func shareText(text: String, onResult: @escaping (ShareResult) -> Void) {
        presentActivitySheet(items: [text], onResult: onResult)
    }

    func copyImage(png: KotlinByteArray, onResult: @escaping (ShareResult) -> Void) {
        guard let image = toImage(png) else {
            onResult(ShareResultFailed(cause: nil))
            return
        }
        UIPasteboard.general.image = image
        onResult(ShareResultSuccess())
    }

    func isInstagramAvailable() -> Bool {
        guard let url = URL(string: "instagram-stories://share") else { return false }
        return UIApplication.shared.canOpenURL(url)
    }

    func shareToInstagramStories(png: KotlinByteArray, onResult: @escaping (ShareResult) -> Void) {
        guard let image = toImage(png),
              let data = image.pngData(),
              let url = URL(string: "instagram-stories://share?source_application=\(Self.facebookAppID)")
        else {
            onResult(ShareResultFailed(cause: nil))
            return
        }

        // iOS URL schemes can't carry binary data, so Meta uses the pasteboard as a side
        // channel: write the image under their agreed key, then open the scheme. Instagram
        // reads the pasteboard on launch. Nothing else on iOS works this way.
        let items: [String: Any] = [
            "com.instagram.sharedSticker.backgroundImage": data
        ]
        // The expiry matters — without it the payload sits on the pasteboard indefinitely,
        // readable by any app the user switches to next.
        let options: [UIPasteboard.OptionsKey: Any] = [
            .expirationDate: Date().addingTimeInterval(60 * 5)
        ]
        UIPasteboard.general.setItems([items], options: options)

        UIApplication.shared.open(url, options: [:]) { opened in
            onResult(opened ? ShareResultSuccess() : ShareResultFailed(cause: nil))
        }
    }

    func saveToPhotos(png: KotlinByteArray, onResult: @escaping (any ShareResult) -> Void) {
        guard let image = toImage(png) else {
            onResult(ShareResultFailed(cause: nil))
            return
        }

        PHPhotoLibrary.requestAuthorization(for: .addOnly) { status in
            guard status == .authorized || status == .limited else {
                // Denied — but the user can still save through UIActivityViewController's
                // built-in "Save Image", which writes via the system's own Photos grant rather
                // than ours. Reporting TargetUnavailable lets ShareFlowHost fall through to the
                // share sheet, so the tap still accomplishes what they asked for. There's no
                // public way to deep-link to the Photos row in Settings anyway.
                onResult(ShareResultTargetUnavailable())
                return
            }
            PHPhotoLibrary.shared().performChanges {
                PHAssetChangeRequest.creationRequestForAsset(from: image)
            } completionHandler: { success, error in
                if success {
                    onResult(ShareResultSuccess())
                } else {
                    // A real write failure, not a permission problem — no fallback would help.
                    onResult(ShareResultFailed(
                        cause: error.map { KotlinThrowable(message: $0.localizedDescription) }
                    ))
                }
            }
        }
    }

    private static let facebookAppID = "2197763431069255"
}
