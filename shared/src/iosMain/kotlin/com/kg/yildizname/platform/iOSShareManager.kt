package com.kg.yildizname.platform

class iOSShareManager() : ShareManager
{
    override fun isAvailable(target: ShareTarget): Boolean {
        return true
    }

    override suspend fun share(
        png: ByteArray,
        target: ShareTarget
    ): ShareResult {
        return ShareResult.Failed(null)
    }

    override suspend fun saveToGallery(png: ByteArray): ShareResult {
        return ShareResult.Failed(null)
    }

    override suspend fun shareText(
        text: String,
        target: ShareTarget
    ): ShareResult {
        return ShareResult.Failed(null)
    }
}