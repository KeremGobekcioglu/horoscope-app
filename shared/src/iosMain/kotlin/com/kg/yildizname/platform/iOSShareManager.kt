package com.kg.yildizname.platform

import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * iOS implementation of [ShareManager].
 *
 * ## Why this looks so different from AndroidShareManager
 *
 * Android hands a payload to a named app with ACTION_SEND + setPackage, and startActivity()
 * returns immediately — the OS takes over and we never learn what the user did. iOS has no
 * equivalent. Its URL schemes carry text only, never binary, so an image cannot be handed to
 * a specific app. Everything goes through UIActivityViewController, where *the user* picks
 * the destination. Instagram Stories is the sole exception, and only because Meta built a
 * pasteboard side channel for it — write the image to a magic pasteboard key, open
 * instagram-stories://share, and Instagram reads it on launch.
 *
 * That's why isAvailable() returns false for WhatsApp/Facebook/InstagramFeed here: they aren't
 * unavailable in the sense of "not installed", they're not addressable targets on this platform
 * at all. The iOS ShareOption list offers only Stories and the system sheet.
 *
 * ## Why the work happens in Swift
 *
 * Presenting a UIActivityViewController requires a live UIViewController to present *from*.
 * Reaching one from Kotlin/Native means UIApplication.keyWindow — deprecated since iOS 13 and
 * unreliable once an app has multiple scenes. So the actual UIKit calls live in Swift, and
 * this class delegates to [swiftShareBridge], a var Swift assigns once at startup in
 * iOSApp.swift. Same pattern as SwiftUrlOpenerBridge and the other bridges in this project.
 *
 * ## Why every method uses suspendCancellableCoroutine
 *
 * The UIKit and Photos APIs behind these calls are asynchronous in a way Android's aren't.
 * When Swift shows the share sheet, the call returns straight away — but the sheet is still on
 * screen and the user hasn't chosen anything. The outcome only exists seconds later, when they
 * tap a destination or cancel. Same for saving: PHPhotoLibrary can trigger a permission prompt
 * mid-call, so the result depends on the user answering it.
 *
 * So Swift can't return a result; it can only call back later. But ShareManager.share() is a
 * suspend fun that has to *return* a ShareResult, because ShareFlowHost wants to write:
 *
 *     val result = shareManager.share(png, target)
 *     when (result) { ... }
 *
 * suspendCancellableCoroutine bridges those two shapes. It parks the coroutine — the thread is
 * released to do other work, and the paused state is stored — and hands out a `continuation`,
 * which is effectively a resume button with a slot for a value. Whoever calls
 * continuation.resume(x) un-parks the coroutine, and x becomes the value the whole
 * suspendCancellableCoroutine expression evaluates to. Here, the resume button gets pressed
 * from inside the callback we hand to Swift.
 *
 * ## Two rules the Swift side must obey
 *
 * A continuation resumes exactly once. Calling resume() twice throws "Already resumed" — worth
 * watching for, because UIKit completion handlers can fire on both dismissal and completion if
 * they're wired carelessly.
 *
 * Resuming *zero* times is worse and quieter: if a Swift path returns without invoking the
 * callback (an early `guard` that just bails, say), this coroutine stays parked forever.
 * ShareFlowHost's isWorking flag never clears, every button in the sheet stays disabled, and
 * the sheet is stuck until dismissed. No crash, no log. Every Swift path — including error
 * paths — must call onResult exactly once.
 */
class iOSShareManager : ShareManager {

    private val bridge: SwiftShareBridge?
        get() = swiftShareBridge

    override fun isAvailable(target: ShareTarget): Boolean = when (target) {
        ShareTarget.SystemSheet -> true
        ShareTarget.InstagramStories -> bridge?.isInstagramAvailable() ?: false
        // Not "not installed" — not addressable. See the class doc above.
        ShareTarget.InstagramFeed, ShareTarget.Facebook, ShareTarget.WhatsApp -> false
    }

    override suspend fun share(png: ByteArray, target: ShareTarget): ShareResult {
        val bridge = bridge ?: return ShareResult.Failed(
            IllegalStateException("swiftShareBridge is not initialized")
        )

        // Not addressable on iOS — nothing to hand off to. Checked before the when so the
        // branches below all return the same shape and type inference doesn't narrow.
        if (target == ShareTarget.InstagramStories && !bridge.isInstagramAvailable()) {
            return ShareResult.TargetUnavailable
        }

        return when (target) {
            ShareTarget.InstagramStories -> suspendCancellableCoroutine { continuation ->
                // Returns immediately; Instagram is still launching. The lambda below is what
                // Swift calls once the handoff has actually been attempted.
                bridge.shareToInstagramStories(png) { result ->
                    continuation.resume(result)
                }
            }
            // No named-app targeting on iOS: everything else opens the system sheet and lets
            // the user choose. ShareFlowHost's TargetUnavailable fallback never fires here,
            // because this *is* the fallback.
            else -> suspendCancellableCoroutine { continuation ->
                bridge.shareImage(png) { result ->
                    continuation.resume(result)
                }
            }
        }
    }

    override suspend fun shareText(text: String, target: ShareTarget): ShareResult {
        val bridge = bridge ?: return ShareResult.Failed(
            IllegalStateException("swiftShareBridge is not initialized")
        )

        return suspendCancellableCoroutine { continuation ->
            bridge.shareText(text) { result ->
                continuation.resume(result)
            }
        }
    }

    override suspend fun saveToGallery(png: ByteArray): ShareResult {
        val bridge = bridge ?: return ShareResult.Failed(
            IllegalStateException("swiftShareBridge is not initialized")
        )

        // Unlike Android, no permission is requested beforehand. NSPhotoLibraryAddUsageDescription
        // in Info.plist lets the write itself trigger the system prompt on first use, and a
        // denial comes back through this callback as a failure rather than an exception.
        return suspendCancellableCoroutine { continuation ->
            bridge.saveToPhotos(png) { result ->
                continuation.resume(result)
            }
        }
    }
}