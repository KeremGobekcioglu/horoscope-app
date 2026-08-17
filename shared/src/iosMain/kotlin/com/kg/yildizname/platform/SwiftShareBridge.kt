package com.kg.yildizname.platform

interface SwiftShareBridge {
    /**
     * Presents the system share sheet for [png]. [onResult] fires with false if the user
     * cancels — cancellation is a normal outcome here, not an error.
     */
    fun shareImage(png: ByteArray, onResult: (ShareResult) -> Unit)
    /** Presents the system share sheet for plain [text]. */
    fun shareText(text: String, onResult: (ShareResult) -> Unit)
    /**
     * Writes [png] into the pasteboard slot Instagram reads on launch, then opens
     * instagram-stories://share. See IosShareManager for why this is a pasteboard handoff
     * rather than a normal share.
     */
    fun shareToInstagramStories(png: ByteArray, onResult: (ShareResult) -> Unit)
    /**
     * Saves [png] to the user's photo library. Unlike Android, the permission prompt is
     * triggered by the write itself rather than requested beforehand, so a denial surfaces
     * here as onResult(false).
     */
    fun saveToPhotos(png: ByteArray, onResult: (ShareResult) -> Unit)
    /** Copies [png] to the general pasteboard. */
    fun copyImage(png: ByteArray, onResult: (ShareResult) -> Unit)
    /** Whether Instagram is installed — LSApplicationQueriesSchemes must list the scheme. */
    fun isInstagramAvailable(): Boolean
}

/** Assigned from iOSApp.swift at startup, before any composition. */
var swiftShareBridge: SwiftShareBridge? = null