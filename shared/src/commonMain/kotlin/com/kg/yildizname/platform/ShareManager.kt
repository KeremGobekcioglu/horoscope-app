package com.kg.yildizname.platform

enum class ShareTarget{
    InstagramStories,
    InstagramFeed,
    WhatsApp,
    Facebook,
    SystemSheet
}

/**
 * Outcome of a share attempt. Modelled explicitly rather than as Boolean/exception because the
 * UI reacts differently to each: [Success] is silent, [TargetUnavailable] falls back to the
 * system sheet, [Failed] shows a snackbar.
 */
sealed interface ShareResult{
    data object Success : ShareResult
    data object TargetUnavailable: ShareResult
    data class Failed(val cause: Throwable?) : ShareResult
}

/**
 * Platform handoff for an already-rendered PNG. Implementations live in androidMain/iosMain and
 * are provided through Koin — deliberately an interface rather than `expect class`, because
 * each platform needs constructor dependencies (Android: Context; iOS: root UIViewController)
 * that an expect class's shared constructor signature can't express.
 */
interface ShareManager
{
    /** Cheap installed-check so the sheet can grey out or hide unavailable targets. */
    fun isAvailable(target: ShareTarget) : Boolean
    /** Writes [png] somewhere the target can read it and launches the handoff. */
    suspend fun share( png: ByteArray, target: ShareTarget) : ShareResult
    /** Saves [png] to the device's photo gallery. May require a runtime permission. */
    suspend fun saveToGallery(png: ByteArray) : ShareResult
    suspend fun shareText(text: String, target: ShareTarget): ShareResult
}
