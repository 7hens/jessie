package cn.jessie.program

import cn.jessie.etc.Files
import cn.jessie.etc.Logdog
import cn.jessie.main.MainAppContext
import cn.jessie.main.Processes
import java.io.File
import java.io.FileNotFoundException
import java.util.concurrent.ConcurrentHashMap

/**
 * @author 7hens
 */
class PluginManager(private val pluginDir: File) {

    private val plugins: MutableMap<String, Program> = ConcurrentHashMap()

    fun all(): Map<String, Program> {
        Processes.checkDaemon()
        return plugins
    }

    fun install(apk: File): Program {
        val apkPath = apk.absolutePath
        val context = MainAppContext.get()
        val packageManager = context.packageManager

        if (!apk.exists() || !apk.isFile) {
            throw FileNotFoundException("install error, file($apkPath) not exists")
        }

        val packageInfo = packageManager.getPackageArchiveInfo(apkPath, 0)
                ?: throw RuntimeException("install error, packageInfo is null, apkPath: $apkPath")
        val packageName = packageInfo.packageName
        val appDir = Files.dir(File(pluginDir, packageName))
        val newApk = File(appDir, BASK_APK)
        if (!newApk.exists()) newApk.createNewFile()
        val newApkPath = newApk.absolutePath
        val isNewApk = apkPath != newApkPath
        val isSameApk = !isNewApk || apk.length() == newApk.length()
        if (plugins.containsKey(packageName) && isSameApk) {
            return plugins.getValue(packageName)
        }
        if (isNewApk) apk.copyTo(newApk, true)
        val program = PluginProgram(packageName, DexInstaller.install(newApk, isNewApk))
        plugins[packageName] = program
        return program
    }

    fun installAll() {
        for (appDir in pluginDir.listFiles()) {
            try {
                val packageName = appDir.name
                val apkFile = File(appDir, BASK_APK)
                if (!(apkFile.exists() && apkFile.isFile && apkFile.length() > 0L)) {
                    val result = Files.delete(appDir)
                    Logdog.error("program($packageName) is missing, removed $result")
                    continue
                }
                val dexInfo = DexInstaller.install(apkFile, false)
                plugins[packageName] = PluginProgram(packageName, dexInfo)
//                Logdog.debug("install inner program($packageName)")
            } catch (e: Throwable) {
                Logdog.error(e)
            }
        }
//        Logdog.debug("install inner complete")
    }

    fun uninstall(packageName: String): Boolean {
        val appDir = File(pluginDir, packageName)
        if (!appDir.exists()) return true
        return Files.delete(appDir)
    }

    fun uninstallAll(): Boolean {
        var result = true
        for (appDir in pluginDir.listFiles()) {
            result = result && Files.delete(appDir)
        }
        return result
    }

    companion object {
        private const val BASK_APK = "base.jar"
    }
}