package cn.jessie.runtime.main

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.ProviderInfo
import android.content.pm.ResolveInfo
import cn.jessie.runtime.program.DexInfo
import cn.thens.okbinder.OkBinder

@OkBinder.Interface
interface IJessieProgramManager {
    fun getProgramPackageNames(): Array<String>
    fun containsProgram(packageName: String): Boolean
    fun install(dexPath: String): String
    fun uninstall(program: String): Boolean
    fun getDexInfo(packageName: String): DexInfo?
    fun getPackageInfo(packageName: String): PackageInfo?
    fun getProcesses(packageName: String): Array<String>
    fun preload(packageName: String)
    fun start(packageName: String)
    fun stop(packageName: String)
    fun requireProcessIndex(processName: String): Int
    fun getProgramProcessNameByPid(pid: Int): String

    fun wrapActivityIntent(intent: Intent): Intent
    fun queryIntentActivities(intent: Intent, flags: Int): MutableList<ResolveInfo>
    fun queryIntentServices(intent: Intent, flags: Int): MutableList<ResolveInfo>
    fun queryIntentContentProviders(intent: Intent, flags: Int): MutableList<ResolveInfo>
    fun resolveActivity(intent: Intent, flags: Int): ResolveInfo?
    fun resolveService(intent: Intent, flags: Int): ResolveInfo?
    fun resolveContentProvider(authority: String, flags: Int): ProviderInfo?
    fun getComponentEnabledSetting(componentName: ComponentName): Int
    fun setComponentEnabledSetting(componentName: ComponentName, newState: Int, flags: Int)
}