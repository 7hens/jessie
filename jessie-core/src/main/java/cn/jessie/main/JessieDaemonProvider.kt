package cn.jessie.main

import android.content.ContentProvider
import android.content.ContentProviderWrapper
import android.content.Context
import android.content.pm.ProviderInfo
import cn.jessie.JessieRuntime

/**
 * 守护者 Provider
 *
 * 用来和其他进程交互，并传递 Binder。
 */
class JessieDaemonProvider : ContentProviderWrapper() {
    override val baseProvider: ContentProvider by lazy { JessieRuntime.daemonProvider }

    override fun attachInfo(context: Context?, info: ProviderInfo?) {
        JessieRuntime.initialize(context!!)
        super.attachInfo(context, info)
    }
}