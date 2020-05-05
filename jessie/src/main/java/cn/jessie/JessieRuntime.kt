package cn.jessie

import cn.jessie.etc.JCLogger
import cn.jessie.main.MainAppContext
import cn.jessie.program.DexInfo
import cn.jessie.program.DexInstaller
import java.io.File

internal object JessieRuntime {
    private const val JESSIE_IMPL = "cn.jessie.runtime.JessieImpl"
    private const val RUNTIME_JAR = "jessie/jessie-runtime.jar"
    private const val RUNTIME_DIR = "runtime"
    private const val BASE_JAR = "base.jar"

    private val runtimeDir by lazy { MainAppContext.dir(RUNTIME_DIR) }

    private fun classLoader(parent: ClassLoader): ClassLoader {
        val dexFile = File(runtimeDir, BASE_JAR)
        val dexFileSize = if (dexFile.exists()) dexFile.length() else 0L
        val shouldCreateNewDexFile = dexFileSize == 0L || run {
            MainAppContext.get().assets.open(RUNTIME_JAR)
                    .use { it.available().toLong() != dexFileSize }
        }
        if (shouldCreateNewDexFile) {
            if (dexFileSize != 0L) {
                dexFile.delete()
                dexFile.createNewFile()
            }
            JCLogger.debug("copy jessie-runtime.jar...")
            MainAppContext.get().assets.open(RUNTIME_JAR)
                    .buffered()
                    .use { it.copyTo(dexFile.outputStream()) }
        }
        val dexInfo = DexInstaller.install(dexFile, shouldCreateNewDexFile)
        return DexInfo.ClassLoader(parent, dexInfo)
    }

    val jessie by lazy {
        classLoader(MainAppContext.get().classLoader).loadClass(JESSIE_IMPL) as Jessie
    }
}