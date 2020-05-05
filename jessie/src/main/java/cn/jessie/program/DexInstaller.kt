package cn.jessie.program

import android.os.Build
import cn.jessie.etc.AndroidVM
import cn.jessie.etc.FilePermissions
import cn.jessie.etc.Files
import cn.jessie.etc.JCLogger
import java.io.File
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

@Suppress("SpellCheckingInspection")
object DexInstaller {
    private const val BASE_DEX = "base.dex"
    private const val LIB_DIR = "lib"
    private const val OAT_DIR = "oat"
    private const val ODEX_DIR = "odex"
    private const val CPU_ARMEABI = "armeabi"
    @Suppress("DEPRECATION")
    private val DEFAULT_SUPPORTED_ABIS = arrayOf(Build.CPU_ABI, Build.CPU_ABI2, CPU_ARMEABI)

    fun install(apk: File, isNewApk: Boolean): DexInfo {
        val appDir = apk.parentFile
        val libDir = Files.dir(File(appDir, LIB_DIR))
        val odexDir = Files.dir(File(appDir, if (AndroidVM.isArt) OAT_DIR else ODEX_DIR))
        val baseDex = File(odexDir, BASE_DEX)
        arrayOf(appDir, apk, libDir, odexDir).forEach { setFileExecutable(it) }
        if (isNewApk) copyNativeBinaries(ZipFile(apk), libDir)
        return DexInfo(apk.absolutePath, odexDir.absolutePath, libDir.absolutePath)
    }

    private fun copyNativeBinaries(zip: ZipFile, libDir: File) {
        try {
            var debugText = supportedAbis.contentToString() + "\n"
            getMappedLibEntries(zip).forEach { (soName, entryMap) ->
                for (abi in supportedAbis) {
                    if (entryMap.containsKey(abi)) {
                        val zipEntry = entryMap.getValue(abi)
                        if (zipEntry.size > 0) {
                            val soFile = File(libDir, soName)
                            if (!soFile.exists() || soFile.length() != zipEntry.size) {
                                zip.getInputStream(zipEntry).copyTo(soFile.outputStream())
                                setFileExecutable(soFile)
                            }
                            debugText += "copy ${zipEntry.name}\n"
                            break
                        }
                    }
                }
            }
            JCLogger.debug(debugText)
        } catch (e: Throwable) {
            JCLogger.error(e)
            Files.delete(libDir)
        } finally {
            zip.close()
        }
    }

    private fun setFileExecutable(file: File) {
        FilePermissions.run { set(file.absolutePath, U7 or G7 or OX) }
    }

    private val supportedAbis: Array<String> by lazy {
        val result = linkedSetOf<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val abis = if (AndroidVM.is64Bit) {
                Build.SUPPORTED_64_BIT_ABIS
            } else {
                Build.SUPPORTED_32_BIT_ABIS
            } ?: emptyArray()
            result.addAll(abis)
        }
        result.addAll(DEFAULT_SUPPORTED_ABIS)
        result.filter { it.isNotBlank() }.toTypedArray()
    }

    /**
     * mapOf("libxx.so" to mapOf("abi" to zipEntry))
     */
    private fun getMappedLibEntries(zip: ZipFile): Map<String, Map<String, ZipEntry>> {
        val libEntries = HashMap<String, HashMap<String, ZipEntry>>()
        val entries = zip.entries()
        while (entries.hasMoreElements()) {
            val entry = entries.nextElement()
            val entryPath = entry.name
            if (entryPath.contains("../") || entry.isDirectory) continue
            if (entryPath.startsWith("lib/") && entryPath.endsWith(".so")) {
                val firstIndexOfSlash = entryPath.indexOf("/")
                val lastIndexOfSlash = entryPath.lastIndexOf("/")
                val abi = entryPath.substring(firstIndexOfSlash + 1, lastIndexOfSlash)
                val soName = entryPath.substring(lastIndexOfSlash + 1)
                if (!libEntries.containsKey(soName)) {
                    libEntries[soName] = hashMapOf(abi to entry)
                } else {
                    libEntries[soName]!![abi] = entry
                }
            }
        }
        return libEntries
    }
}