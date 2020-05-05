package cn.jessie.runtime.program

import android.app.Application
import android.app.Service
import android.content.ContentProvider
import cn.jessie.etc.JCLogger
import cn.jessie.program.DexInfo
import cn.jessie.runtime.main.MainAppContext
import java.io.File

internal object AndroidHook {
    private const val JESSIE_HOOK_JAR = "jessie/android-hook.jar"
    private const val HOOK_DIR = "jc_hook"
    private const val BASE_JAR = "base.jar"

    private val hookDir by lazy { MainAppContext.dir(HOOK_DIR) }

    val hookedClassNames = arrayOf(
            Application::class.java.name,
//            Activity::class.java.name,
            Service::class.java.name,
//            BroadcastReceiver::class.java.name,
            ContentProvider::class.java.name,
            "androidx.core.app.NotificationCompat\$Builder",
            "androidx.core.app.NotificationCompatBuilder",
            "androidx.core.app.NotificationBuilderWithBuilderAccessor",
            "android.support.v4.app.NotificationCompat\$Builder"
//            Notification::class.java.name,
//            Notification.Builder::class.java.name
    )

    fun classLoader(parent: ClassLoader): ClassLoader {
        val dexFile = File(hookDir, BASE_JAR)
        val dexFileSize = if (dexFile.exists()) dexFile.length() else 0L
        val shouldCreateNewDexFile = dexFileSize == 0L || run {
            MainAppContext.get().assets.open(JESSIE_HOOK_JAR)
                    .use { it.available().toLong() != dexFileSize }
        }
        if (shouldCreateNewDexFile) {
            if (dexFileSize != 0L) {
                dexFile.delete()
                dexFile.createNewFile()
            }
            JCLogger.debug("copy android_hook.jar...")
            MainAppContext.get().assets.open(JESSIE_HOOK_JAR)
                    .buffered()
                    .use { it.copyTo(dexFile.outputStream()) }
        }
        val dexInfo = DexInstaller.install(dexFile, shouldCreateNewDexFile)
        return HookClassLoader(parent, dexInfo, hookedClassNames)
    }

    private class HookClassLoader(
            parent: ClassLoader?, dexInfo: DexInfo,
            private val hookedClassNames: Array<String>) : DexInfo.ClassLoader(parent, dexInfo) {

        override fun loadClass(name: String, resolve: Boolean): Class<*> {
            return findLoadedClass(name) ?: run {
                val shouldHook = name in hookedClassNames
                JCLogger.onlyIf(shouldHook).debug("hook class $name")
                return try {
                    if (!shouldHook) throw RuntimeException()
                    findClass(name)!!
                } catch (e: Throwable) {
                    if (shouldHook) JCLogger.error(e)
                    parent.loadClass(name)
                }
            }
        }
    }
}