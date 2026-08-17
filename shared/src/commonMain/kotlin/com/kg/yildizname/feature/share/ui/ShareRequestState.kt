package com.kg.yildizname.feature.share.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.kg.yildizname.feature.share.ui.domain.CompatibilityDetailedShareCardRequest
import com.kg.yildizname.feature.share.ui.domain.CompatibilityShareCardRequest
import com.kg.yildizname.feature.share.ui.domain.ShareCardRequest
import com.kg.yildizname.feature.share.ui.domain.ShareRequest

@Stable
class ShareFlowState internal constructor() {
    var request by mutableStateOf<ShareRequest?>(null)
        private set

    fun open(request: ShareCardRequest) {
        this.request = ShareRequest.Horoscope(request)
    }

    fun open(request: CompatibilityShareCardRequest) {
        this.request = ShareRequest.Compatibility(request)
    }

    fun open(request: CompatibilityDetailedShareCardRequest) {
        this.request = ShareRequest.CompatibilityDetailed(request)
    }

    fun dismiss() {
        request = null
    }
}

@Composable
fun rememberShareFlowState(): ShareFlowState = remember { ShareFlowState() }