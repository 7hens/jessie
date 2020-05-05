package cn.jessie.runtime.program

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.res.Resources
import cn.jessie.runtime.main.MainAppContext

/**
 * @author 7hens
 */
internal class AppProgram(private val app: Context) : AbstractProgram() {
    override val packageName: String by lazy { app.packageName }
    override val classLoader: ClassLoader = app.classLoader
    override val resources: Resources = app.resources
    override val dexInfo: DexInfo by lazy {
        app.applicationInfo.run { DexInfo(sourceDir, "", nativeLibraryDir) }
    }

    override val packageInfo: PackageInfo by lazy { AndroidPackageInfo.get(app) }

    override val packageComponents: AndroidPackageComponents by lazy {
        AndroidPackageComponents.parse(packageInfo, resources.assets)
    }

    override fun preload() {
        packageComponents
    }

    override fun start() {
        val launcherActivity = packageComponents.launcherActivities
                .map { it.component }
                .filter { it.processName == packageInfo.applicationInfo.processName && it.isEnabled }
                .map { it.name }
                .getOrElse(0) { "" }
        val intent = Intent()
                .setComponent(ComponentName(packageName, launcherActivity))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
        MainAppContext.get().startActivity(intent)
    }

    override fun stop() {
        throw IllegalAccessException("cannot stop host app")
    }

}