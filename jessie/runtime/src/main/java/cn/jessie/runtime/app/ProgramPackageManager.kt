package cn.jessie.runtime.app

import android.annotation.TargetApi
import android.content.ComponentName
import android.content.Intent
import android.content.pm.*
import android.content.res.XmlResourceParser
import android.os.Build
import cn.jessie.main.JessieServices
import cn.jessie.main.MainAppContext
import cn.jessie.program.Program

internal class ProgramPackageManager(base: PackageManager) : PackageManagerWrapper(base) {
    private val program: Program = MyProgram
    private val programManager get() = JessieServices.programManager

    override fun getXml(packageName: String, resid: Int, appInfo: ApplicationInfo): XmlResourceParser {
        return try {
            program.resources.getXml(resid)
        } catch (e: Throwable) {
            super.getXml(packageName, resid, appInfo)
        }
    }

    override fun getPackageInfo(packageName: String?, flags: Int): PackageInfo {
        if (packageName == program.packageName) return program.packageInfo
        return super.getPackageInfo(packageName, flags)
    }

    override fun getApplicationLabel(info: ApplicationInfo?): CharSequence {
        return try {
            program.resources.getString(program.packageInfo.applicationInfo.labelRes)
        } catch (e: Throwable) {
            super.getApplicationLabel(info)
        }
    }

    override fun getApplicationInfo(packageName: String?, flags: Int): ApplicationInfo {
        if (packageName == program.packageName) return program.packageInfo.applicationInfo
        return super.getApplicationInfo(packageName, flags)
    }

    override fun getActivityInfo(component: ComponentName?, flags: Int): ActivityInfo {
        if (component != null && component.packageName == program.packageName) {
            val activity = program.packageComponents.activities[component.className]
            if (activity != null) return activity.component
        }
        return super.getActivityInfo(component, flags)
    }

    override fun queryIntentActivities(intent: Intent?, flags: Int): MutableList<ResolveInfo> {
        return programManager.queryIntentActivities(intent!!, flags)
    }

    override fun queryIntentServices(intent: Intent?, flags: Int): MutableList<ResolveInfo> {
        return programManager.queryIntentServices(intent!!, flags)
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    override fun queryIntentContentProviders(intent: Intent?, flags: Int): MutableList<ResolveInfo> {
        return programManager.queryIntentContentProviders(intent!!, flags)
    }

    override fun resolveActivity(intent: Intent?, flags: Int): ResolveInfo? {
        return programManager.resolveActivity(intent!!, flags)
    }

    override fun resolveService(intent: Intent?, flags: Int): ResolveInfo? {
        return programManager.resolveService(intent!!, flags)
    }

    override fun resolveContentProvider(name: String, flags: Int): ProviderInfo? {
        return programManager.resolveContentProvider(name, flags)
    }

    override fun getComponentEnabledSetting(componentName: ComponentName?): Int {
        return programManager.getComponentEnabledSetting(componentName!!)
    }

    override fun setComponentEnabledSetting(componentName: ComponentName, newState: Int, flags: Int) {
        programManager.setComponentEnabledSetting(componentName, newState, flags)
    }

    companion object {
        private val instance by lazy { ProgramPackageManager(MainAppContext.get().packageManager) }

        fun get() = instance
    }
}