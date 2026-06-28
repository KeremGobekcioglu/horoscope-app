package com.kg.yildizname.platform

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner

actual class ForegroundObserver actual constructor(
    private val onForeground: () -> Unit,
) : DefaultLifecycleObserver {

    private val lifecycle: Lifecycle
        get() = ProcessLifecycleOwner.get().lifecycle

    actual fun start() { lifecycle.addObserver(this) }
    actual fun stop()  { lifecycle.removeObserver(this) }

    override fun onStart(owner: LifecycleOwner) { onForeground() }
}
