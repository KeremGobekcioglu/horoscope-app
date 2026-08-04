package com.kg.yildizname.platform

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

private const val FACEBOOK_APP_ID = "2197763431069255"   // numeric here; "fb"-prefixed in the manifest
private const val INSTAGRAM_PACKAGE = "com.instagram.android"
private const val WHATSAPP_PACKAGE = "com.whatsapp"
private const val WHATSAPP_BUSINESS_PACKAGE = "com.whatsapp.w4b"
private const val FACEBOOK_PACKAGE = "com.facebook.katana"

/** Exported PNGs older than this are pruned on the next share. */
private const val SHARE_CACHE_TTL_MS = 10 * 60 * 1000L

class AndroidShareManager(private val context: Context) : ShareManager
{
    private var lastGrantedUri : Uri? = null

    override fun isAvailable(target: ShareTarget): Boolean {
        return resolvePackage(target)?.let { isInstalled(it) } ?: true
    }

    override suspend fun share(
        png: ByteArray,
        target: ShareTarget
    ): ShareResult {
        return withContext(Dispatchers.IO)
        {
            try {
                val uri = writeToShareCache(png)
                val pkg = resolvePackage(target)

                val intent = when(target)
                {
                    ShareTarget.InstagramStories -> storiesIntent(uri)
                    ShareTarget.SystemSheet -> {
                        Intent.createChooser(sendIntent(uri, null), null)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    else -> sendIntent(uri, pkg)
                }
                if(intent.resolveActivity(context.packageManager) == null)
                {
                    ShareResult.TargetUnavailable
                }
                else
                {
                    grantTo(pkg, uri)
                    context.startActivity(intent)
                    ShareResult.Success
                }
            }
            catch (e: Exception)
            {
                ShareResult.Failed(e)
            }
        }
    }


    override suspend fun saveToGallery(png: ByteArray): ShareResult =
        TODO("step 1c — MediaStore + the API<=28 permission branch")

    // ACTION_SEND puts the payload in the extras bundle under EXTRA_STREAM. Every generic
    // "share to me" receiver (WhatsApp, Facebook, the system chooser, Instagram feed) reads
    // from this extra by convention. Compare storiesIntent below, which reads a different slot.
    private fun sendIntent(uri: Uri, pkg: String?): Intent {
        return Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            pkg?.let { setPackage(it) }
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            // We hold an application Context here, not an Activity. An Activity already
            // belongs to a back-stack task, so startActivity() can push the new screen onto
            // it implicitly. A bare Context belongs to no task at all — without this flag,
            // startActivity() throws AndroidRuntimeException at the moment it actually runs,
            // not at compile time, because nothing about the call looks wrong until Android
            // tries to find a task to attach the new Activity to and finds none.
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    // ADD_TO_STORY is not part of the generic Android share system — it's a bespoke action
    // Meta's app declares an intent-filter for. Its convention reads the payload from the
    // intent's primary *data* field (setDataAndType), the same slot ACTION_VIEW uses to open
    // a URL — NOT from EXTRA_STREAM. Put the image in the wrong slot and Instagram still
    // launches successfully (resolveActivity passes, no exception anywhere), but its story
    // composer finds nothing in intent.data and opens a blank canvas. This is the second of
    // two ways to get a silently-empty Instagram Story — see grantTo() below for the first.
    private fun storiesIntent(uri: Uri): Intent {
        return Intent("com.instagram.share.ADD_TO_STORY").apply {
            setDataAndType(uri, "image/png")
            putExtra("source_application", FACEBOOK_APP_ID)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }
    /**
     * The intent flag alone grants read access only to the activity the system launches, for
     * that activity's lifetime. Instagram's story composer frequently reads the URI from a
     * different process, or after the receiving activity has finished — by which point the
     * flag-based grant is gone and the story canvas silently comes up empty. This is the first
     * of two ways to get a silently-empty Story; see storiesIntent() above for the second.
     *
     * A package-level grant persists until revoked or reboot, so any Instagram component can
     * read it whenever it gets around to it. We keep the intent flag too; it costs nothing.
     *
     * Package grants don't expire on their own, so lastGrantedUri tracks the one we handed out
     * last time and revokes it before issuing a new one — otherwise every share this process
     * lifetime leaves behind a permanent, unused grant to an already-deleted temp file.
     */
    private fun grantTo(pkg: String?, uri: Uri) {
        lastGrantedUri?.let {
            context.revokeUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        lastGrantedUri = null

        // SystemSheet resolves to pkg == null: we don't know which app the user will pick
        // from the chooser, so there's no specific package to grant to. The chooser mechanism
        // grants the picked app access itself, via the intent flag already set in sendIntent.
        if (pkg == null) return

        context.grantUriPermission(pkg, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        lastGrantedUri = uri
    }
    private fun writeToShareCache(png: ByteArray) : Uri
    {
        val dir = File(context.cacheDir, "share").apply { mkdirs() }
        // Unique filename per share so a target still reading the previous export isn't
        // pulled out from under; age-based pruning keeps the dir from growing.
        val now = System.currentTimeMillis()
        // cleanup 10 minutes
        dir.listFiles()?.forEach {
            if(now - it.lastModified() > SHARE_CACHE_TTL_MS)
            {
                it.delete()
            }
        }
        // every name is unique because if we use same name we can delete and replace the file which is at mid-read by another app.
        val file = File(dir, "yildizname_$now.png")
        // this must match provider in manifest. android authorities. the fileprovider suffix must match android authorities value.
        return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    }
    private fun resolvePackage(target: ShareTarget) : String?
    {
        return when(target)
        {
            ShareTarget.InstagramStories, ShareTarget.InstagramFeed -> INSTAGRAM_PACKAGE
            ShareTarget.WhatsApp ->
            {
                if(isInstalled(WHATSAPP_PACKAGE))
                {
                    WHATSAPP_PACKAGE
                }
                else WHATSAPP_BUSINESS_PACKAGE
            }

            ShareTarget.Facebook -> FACEBOOK_PACKAGE
            ShareTarget.SystemSheet -> null
        }
    }

    // On API 30+, getPackageInfo() throws NameNotFoundException — not "returns not-found" —
    // for any package outside the app's declared <queries> visibility, even if it's genuinely
    // installed. runCatching{}.isSuccess turns that exception into a plain boolean so callers
    // don't need to know package visibility is involved at all. Our <queries> block already
    // declares all four packages this file checks, so this resolves correctly for us; without
    // that manifest entry, this would report "not installed" for an app that plainly is.
    private fun isInstalled(pkg: String): Boolean {
        return runCatching { context.packageManager.getPackageInfo(pkg, 0) }.isSuccess
    }
}