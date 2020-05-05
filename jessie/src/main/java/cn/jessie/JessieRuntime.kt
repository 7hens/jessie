package cn.jessie

import android.content.ContentProvider
import android.content.Context
import cn.jessie.program.DexInfo
import cn.jessie.program.DexInstaller
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean

internal object JessieRuntime {
    private const val JESSIE_IMPL = "cn.jessie.runtime.JessieImpl"
    private const val MAIN_PROVIDER = "cn.jessie.runtime.MainInitProvider"
    private const val DAEMON_PROVIDER = "cn.jessie.runtime.JessieDaemonProvider"

    private const val RUNTIME_JAR = "jessie/jessie-runtime.jar"
    private const val RUNTIME_DIR = "runtime"
    private const val BASE_JAR = "base.jar"

    lateinit var context: Context

    private fun classLoader(context: Context): ClassLoader {
        val runtimeDir = context.getDir(RUNTIME_DIR, Context.MODE_PRIVATE)
        val dexFile = File(runtimeDir, BASE_JAR)
        val dexFileSize = if (dexFile.exists()) dexFile.length() else 0L
        val shouldCreateNewDexFile = dexFileSize == 0L || run {
            context.assets.open(RUNTIME_JAR)
                    .use { it.available().toLong() != dexFileSize }
        }
        if (shouldCreateNewDexFile) {
            if (dexFileSize != 0L) {
                dexFile.delete()
                dexFile.createNewFile()
            }
            context.assets.open(RUNTIME_JAR)
                    .buffered()
                    .use { it.copyTo(dexFile.outputStream()) }
        }
        val dexInfo = DexInstaller.install(dexFile, shouldCreateNewDexFile)
        return DexInfo.ClassLoader(context.classLoader, dexInfo)
    }

    private val isInitialized = AtomicBoolean(false)
    private lateinit var classLoader: ClassLoader

    fun initialize(context: Context) {
        if (isInitialized.compareAndSet(false, true)) {
            this.context = context
            classLoader = classLoader(context)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> get(name: String): T {
        return classLoader.loadClass(name).getField("INSTANCE").get(null) as T
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> create(name: String): T {
        return classLoader.loadClass(name).newInstance() as T
    }

    val mainProvider by lazy { get<ContentProvider>(MAIN_PROVIDER) }

    val daemonProvider by lazy { get<ContentProvider>(DAEMON_PROVIDER) }

    val jessie by lazy { get<Jessie>(JESSIE_IMPL) }
}