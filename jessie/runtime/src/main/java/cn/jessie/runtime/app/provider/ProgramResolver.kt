package cn.jessie.runtime.app.provider

import android.content.ContentResolver
import android.content.Context
import android.content.IContentProvider
import android.net.Uri
import cn.jessie.etc.JCLogger
import cn.jessie.etc.Reflections
import cn.jessie.main.JessieServices
import cn.jessie.main.MainAppContext
import cn.jessie.main.Processes
import java.util.*

/**
 * 插件的 ContentResolver
 *
 * 自动将插件的 uri 包装成 [JessieStubProvider] 的 URI。
 * @author 7hens
 */
@Suppress("unused")
internal class ProgramResolver(context: Context) : ContentResolver(context) {
    private val base: ContentResolver by lazy { MainAppContext.get().contentResolver }
    private val programManager get() = JessieServices.programManager

    private fun getStubProvider(context: Context, authority: String): IContentProvider? {
        val providerInfo = programManager.resolveContentProvider(authority, 0) ?: return null
        if (!programManager.containsProgram(providerInfo.packageName)) {
            return Reflections.invoke(base, "acquireUnstableProvider", context, authority) as IContentProvider
        }
        val processName = Processes.processName(providerInfo)
        val processIndex = programManager.requireProcessIndex(processName)
        val stubAuthority = JessieStubProvider.authority(processIndex)
        val stubProvider = Reflections.invoke(base, "acquireUnstableProvider", context, stubAuthority)
        return Reflections.proxy { _, method, args ->
            if (args == null || args.isEmpty()) {
                JCLogger.debug("provider.${method.name}()")
                method.invoke(stubProvider)
            } else {
                val wrappedArgs = args.map { arg ->
                    if (arg !is Uri) arg else JessieStubProvider.boxUri(arg, providerInfo.packageName, stubAuthority)
                }.toTypedArray()
                JCLogger.debug("provider.${method.name}(${wrappedArgs.contentToString()})")
                method.invoke(stubProvider, *wrappedArgs)
            }
        }
    }

    fun acquireProvider(context: Context, name: String): IContentProvider? {
        return try {
            getStubProvider(context, name)
                    ?: Reflections.invoke(base, "acquireProvider", context, name) as? IContentProvider
        } catch (e: Throwable) {
            JCLogger.error(e)
            null
        }
    }

    fun acquireUnstableProvider(context: Context, name: String): IContentProvider? {
        return try {
            getStubProvider(context, name)
                    ?: Reflections.invoke(base, "acquireUnstableProvider", context, name) as? IContentProvider
        } catch (e: Throwable) {
            JCLogger.error(e)
            null
        }
    }

    fun releaseProvider(icp: IContentProvider): Boolean {
        return try {
            return Reflections.invoke(base, "releaseProvider", icp) as Boolean
        } catch (e: Throwable) {
            JCLogger.error(e)
            false
        }
    }

    fun releaseUnstableProvider(icp: IContentProvider): Boolean {
        return try {
            return Reflections.invoke(base, "releaseUnstableProvider", icp) as Boolean
        } catch (e: Throwable) {
            JCLogger.error(e)
            false
        }
    }

    fun unstableProviderDied(icp: IContentProvider) {
        try {
            Reflections.invoke(base, "unstableProviderDied", icp)
        } catch (e: Throwable) {
            JCLogger.error(e)
        }
    }

    companion object {
        private val instance by lazy { ProgramResolver(MainAppContext.get()) }

        fun get() = instance
    }
}