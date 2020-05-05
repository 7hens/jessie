package cn.jessie.runtime.program

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import cn.jessie.runtime.etc.Files
import cn.jessie.runtime.main.MainAppContext
import java.io.File

object AndroidPackageInfo {

    private val FLAG_GET_SIGNING_CERTIFICATES = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.P -> PackageManager.GET_SIGNING_CERTIFICATES
        else -> PackageManager.GET_SIGNATURES
    }

    private val FLAG_MATCH_UNINSTALLED_PACKAGES = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> PackageManager.MATCH_UNINSTALLED_PACKAGES
        else -> PackageManager.GET_UNINSTALLED_PACKAGES
    }

    private val FLAG_MATCH_DISABLED_COMPONENTS = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> PackageManager.MATCH_DISABLED_COMPONENTS
        else -> PackageManager.GET_DISABLED_COMPONENTS
    }

    private val FLAG_MATCH_DISABLED_UNTIL_USED_COMPONENTS = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> PackageManager.MATCH_DISABLED_UNTIL_USED_COMPONENTS
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 -> PackageManager.GET_DISABLED_UNTIL_USED_COMPONENTS
        else -> 0
    }

    private val packageInfoFlags = 0 or
            PackageManager.GET_ACTIVITIES or
            PackageManager.GET_SERVICES or
            PackageManager.GET_PROVIDERS or
            PackageManager.GET_RECEIVERS or
            PackageManager.GET_META_DATA or
            PackageManager.GET_INTENT_FILTERS or
            PackageManager.GET_URI_PERMISSION_PATTERNS or
            PackageManager.GET_SHARED_LIBRARY_FILES or
            PackageManager.GET_PERMISSIONS or
            PackageManager.GET_INSTRUMENTATION or
            FLAG_GET_SIGNING_CERTIFICATES or
            FLAG_MATCH_DISABLED_COMPONENTS or
            FLAG_MATCH_DISABLED_UNTIL_USED_COMPONENTS or
            FLAG_MATCH_UNINSTALLED_PACKAGES

    fun get(dexInfo: DexInfo): PackageInfo {
        val context = MainAppContext.get()
        val packageManager = context.packageManager
        val apkPath = dexInfo.dexPath
        val packageInfo = packageManager.getPackageArchiveInfo(apkPath, packageInfoFlags).check()
        val applicationInfo = packageInfo.applicationInfo
        applicationInfo.sourceDir = apkPath
        applicationInfo.publicSourceDir = apkPath
        applicationInfo.nativeLibraryDir = dexInfo.libraryDirectory

        val parentDir = File(apkPath).parentFile
        val dataDir = Files.dir(File(parentDir, "data"))
        applicationInfo.dataDir = dataDir.absolutePath
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            applicationInfo.deviceProtectedDataDir = dataDir.absolutePath
        }
        return packageInfo
    }

    fun get(context: Context): PackageInfo {
        return context.packageManager.getPackageInfo(context.packageName,
                packageInfoFlags and FLAG_GET_SIGNING_CERTIFICATES.inv())
                .check()
    }

    private fun PackageInfo.check(): PackageInfo {
        if (activities == null) activities = arrayOf()
        if (services == null) services = arrayOf()
        if (receivers == null) receivers = arrayOf()
        if (providers == null) providers = arrayOf()
        return this
    }
}