package com.kg.yildizname.platform

import android.content.Context
import android.net.Uri
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
        TODO("Not yet implemented")
    }

    override suspend fun saveToGallery(png: ByteArray): ShareResult {
        TODO("Not yet implemented")
    }

    private fun writeToShareCache(png: ByteArray) : Uri
    {
        val dir = File(context.cacheDir, "share").apply { mkdirs() }
        // Unique filename per share so a target still reading the previous export isn't
        // pulled out from under; age-based pruning keeps the dir from growing.
        val now = System.currentTimeMillis()
    }
    private fun resolvePackage(target: ShareTarget) : String?
    {
        return when(target)
        {
            ShareTarget.InstagramStories, ShareTarget.InstagramFeed -> INSTAGRAM_PACKAGE
            ShareTarget.WhatApp ->
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

    private fun isInstalled(pkg: String) : Boolean
    {
        return runCatching { context.packageManager.getPackageInfo(pkg, 0) }.isSuccess
    }
}