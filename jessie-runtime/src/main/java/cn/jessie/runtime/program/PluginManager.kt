package cn.jessie.runtime.program

import cn.jessie.program.DexInstaller
import cn.jessie.program.Program
import cn.jessie.runtime.etc.Files
import cn.jessie.runtime.etc.JCLogger
import cn.jessie.runtime.main.MainAppContext
import cn.jessie.runtime.main.Processes
import java.io.File
import java.io.FileNotFoundException
import java.util.concurrent.ConcurrentHashMap

/**
 * @author 7hens
 */
class PluginManager(private val programRootDir: File) {

    private val programs: MutableMap<String, Program> = ConcurrentHashMap()

    fun all(): Map<String, Program> {
        Processes.checkDaemon()
        return programs
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
        val programDir = Files.dir(File(programRootDir, packageName))
        val newApk = File(programDir, BASK_APK)
        if (!newApk.exists()) newApk.createNewFile()
        val newApkPath = newApk.absolutePath
        val isNewApk = apkPath != newApkPath
        val isSameApk = !isNewApk || apk.length() == newApk.length()
        if (programs.containsKey(packageName) && isSameApk) {
            return programs.getValue(packageName)
        }
        if (isNewApk) apk.copyTo(newApk, true)
        val program = PluginProgram(packageName, DexInstaller.install(newApk, isNewApk))
        programs[packageName] = program
        return program
    }

    fun installAll() {
        for (programDir in programDirs) {
            try {
                val packageName = programDir.name
                val apkFile = File(programDir, BASK_APK)
                if (!(apkFile.exists() && apkFile.isFile && apkFile.length() > 0L)) {
                    val result = Files.delete(programDir)
                    JCLogger.error("program($packageName) is missing, removed $result")
                    continue
                }
                val dexInfo = DexInstaller.install(apkFile, false)
                programs[packageName] = PluginProgram(packageName, dexInfo)
//                Logdog.debug("install inner program($packageName)")
            } catch (e: Throwable) {
                JCLogger.error(e)
            }
        }
//        Logdog.debug("install inner complete")
    }

    fun uninstall(packageName: String): Boolean {
        val programDir = File(programRootDir, packageName)
        if (!programDir.exists()) return true
        return Files.delete(programDir)
    }

    fun uninstallAll(): Boolean {
        var result = true
        for (programDir in programDirs) {
            result = result && Files.delete(programDir)
        }
        return result
    }

    private val programDirs get() = programRootDir.listFiles() ?: emptyArray()

    companion object {
        private const val BASK_APK = "base.jar"
    }
}