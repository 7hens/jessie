package cn.jessie.app.provider

import android.content.ContentProvider
import android.content.pm.ProviderInfo
import android.net.Uri
import cn.jessie.app.MyProgram
import cn.jessie.app.application.MyProgramApp
import cn.jessie.etc.JCLogger
import cn.jessie.main.Processes

internal object MyProgramProviders {
    private val cache = mutableMapOf<String, ContentProvider>()

    fun get(authority: String): ContentProvider? {
        return cache[authority]
    }

    fun require(uri: Uri): ContentProvider {
        val authority = uri.authority ?: ""
        if (cache.containsKey(authority)) {
            return get(authority)!!
        }
        val providerInfo = MyProgram.packageComponents.providers.getValue(authority).component
        return create(providerInfo)
    }

    fun create(providerInfo: ProviderInfo): ContentProvider {
        JCLogger.debug("""
            provider = ${providerInfo.name}
            authority = ${providerInfo.authority}
            processName = ${providerInfo.processName}
        """.trimIndent())
        Processes.checkProgram(providerInfo)
        val cContentProvider = MyProgram.classLoader.loadClass(providerInfo.name)
        val provider = cContentProvider.newInstance() as ContentProvider
        provider.attachInfo(MyProgramApp.get(), providerInfo)
        cache[providerInfo.authority] = provider
        return provider
    }

    fun contains(authority: String): Boolean {
        return cache.containsKey(authority)
    }

    fun each(fn: (ContentProvider) -> Unit) {
        cache.values.forEach { fn(it) }
    }
}