package cn.jessie.main

import android.content.ContentProvider
import android.content.ContentProviderWrapper
import android.content.Context
import android.content.pm.ProviderInfo
import cn.jessie.JessieRuntime

class MainInitProvider : ContentProviderWrapper() {
    override val baseProvider: ContentProvider by lazy { JessieRuntime.mainProvider }

    override fun attachInfo(context: Context?, info: ProviderInfo?) {
        JessieRuntime.initialize(context!!)
        super.attachInfo(context, info)
    }
}