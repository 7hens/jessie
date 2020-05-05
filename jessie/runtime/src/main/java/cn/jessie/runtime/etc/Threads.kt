package cn.jessie.runtime.etc

import android.os.Handler
import android.os.Looper

@Suppress("HasPlatformType", "MemberVisibilityCanBePrivate")
internal object Threads {
    val main get() = Looper.getMainLooper().thread
    val current get() = Thread.currentThread()

    inline fun runOnMainThread(crossinline fn: () -> Unit) {
        val mainLooper = Looper.getMainLooper()
        if (mainLooper.thread == Thread.currentThread()) {
            fn()
        } else {
            Handler(mainLooper).post { fn() }
        }
    }

    fun checkMain() {
        if (current == main) return
        throw RuntimeException("wrong thread, expected main but actual (${current.name})")
    }
}