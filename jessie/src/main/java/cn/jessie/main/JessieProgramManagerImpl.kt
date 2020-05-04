package cn.jessie.main

import android.annotation.TargetApi
import android.content.ComponentName
import android.content.Intent
import android.content.pm.*
import android.os.Build
import cn.jessie.app.IntentMatcher
import cn.jessie.app.activity.ActivityStubs
import cn.jessie.etc.JCLogger
import cn.jessie.program.AndroidPackageComponents
import cn.jessie.program.DexInfo
import cn.jessie.program.PluginManager
import cn.jessie.program.Program
import java.io.File

internal class JessieProgramManagerImpl private constructor() : IJessieProgramManager {

    private val pluginManager by lazy { PluginManager(MainAppContext.dir(PLUGIN_DIR)) }

    val programs: Map<String, Program> get() = pluginManager.all()

    override fun getProgramPackageNames(): Array<String> {
        return pluginManager.all().keys.toTypedArray()
    }

    override fun containsProgram(packageName: String): Boolean {
        return pluginManager.all().containsKey(packageName)
    }

    fun getProgram(packageName: String): Program? {
        return programs[packageName]
    }

    fun installAll() {
        pluginManager.installAll()
    }

    override fun install(dexPath: String): String {
        return pluginManager.install(File(dexPath)).packageName
    }

    override fun uninstall(program: String): Boolean {
        return pluginManager.uninstall(program)
    }

    override fun getDexInfo(packageName: String): DexInfo? {
        return forProgram(packageName, null, Program::dexInfo::get)
    }

    override fun getPackageInfo(packageName: String): PackageInfo? {
        return forProgram(packageName, null, Program::packageInfo)
    }

    override fun getProcesses(packageName: String): Array<String> {
        // FIXME 应该返回进程名
        return pluginManager.all().keys.toTypedArray()
    }

    override fun preload(packageName: String) {
        return forProgram(packageName, Unit, Program::preload)
    }

    override fun start(packageName: String) {
        return forProgram(packageName, Unit, Program::start)
    }

    override fun stop(packageName: String) {
        return forProgram(packageName, Unit, Program::stop)
    }

    private inline fun <R> forProgram(packageName: String, defaultValue: R, fn: Program.() -> R): R {
        val program = pluginManager.all()[packageName] ?: return defaultValue
        return fn(program)
    }

    override fun requireProcessIndex(processName: String): Int {
        return ProcessDispatcher.requireProcessIndex(processName)
    }

    override fun getProgramProcessNameByPid(pid: Int): String {
        return ProcessDispatcher.getProgramProcessNameByPid(pid)
    }

    override fun wrapActivityIntent(intent: Intent): Intent {
        return ActivityStubs.wrapIntent(intent)
    }

    private val packageManager get() = MainAppContext.get().packageManager

    override fun queryIntentActivities(intent: Intent, flags: Int): MutableList<ResolveInfo> {
        return mutableListOf<ResolveInfo>().apply {
            queryIntentComponents(intent, flags) { packageComponents.activities }
                    .map { component ->
                        ResolveInfo().apply {
                            activityInfo = component
                            icon = component.icon
                            labelRes = component.labelRes
                            nonLocalizedLabel = component.nonLocalizedLabel
                            resolvePackageName = component.packageName
                        }
                    }
                    .let { addAll(it) }
            addAll(packageManager.queryIntentActivities(intent, flags) ?: emptyList())
        }
    }

    override fun queryIntentServices(intent: Intent, flags: Int): MutableList<ResolveInfo> {
        return mutableListOf<ResolveInfo>().apply {
            queryIntentComponents(intent, flags) { packageComponents.services }
                    .map { component ->
                        ResolveInfo().apply {
                            serviceInfo = component
                            icon = component.icon
                            labelRes = component.labelRes
                            nonLocalizedLabel = component.nonLocalizedLabel
                            resolvePackageName = component.packageName
                        }
                    }
                    .let { addAll(it) }
            addAll(packageManager.queryIntentServices(intent, flags) ?: emptyList())
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    override fun queryIntentContentProviders(intent: Intent, flags: Int): MutableList<ResolveInfo> {
        return mutableListOf<ResolveInfo>().apply {
            queryIntentComponents(intent, flags) { packageComponents.providers }
                    .map { component ->
                        ResolveInfo().apply {
                            providerInfo = component
                            icon = component.icon
                            labelRes = component.labelRes
                            nonLocalizedLabel = component.nonLocalizedLabel
                            resolvePackageName = component.packageName
                        }
                    }
                    .let { addAll(it) }
            addAll(packageManager.queryIntentContentProviders(intent, flags) ?: emptyList())
        }
    }

    override fun resolveActivity(intent: Intent, flags: Int): ResolveInfo? {
        return queryIntentActivities(intent, flags).firstOrNull()
    }

    override fun resolveService(intent: Intent, flags: Int): ResolveInfo? {
        return queryIntentServices(intent, flags).firstOrNull()
    }

    override fun resolveContentProvider(authority: String, flags: Int): ProviderInfo? {
        for (program in programs.values) {
            val provider = program.packageComponents.providers[authority]
            if (provider != null) {
                return provider.component
            }
        }
        return packageManager.resolveContentProvider(authority, flags)
    }

    override fun getComponentEnabledSetting(componentName: ComponentName): Int {
        if (componentName == null) return PackageManager.COMPONENT_ENABLED_STATE_DISABLED
        val packageName = componentName.packageName
        if (programs.containsKey(packageName)) {
            programs.getValue(packageName).packageComponents.run {
                for (map in listOf(activities, services, receivers)) {
                    val item = map[componentName.className]
                    if (item != null) {
                        return getComponentEnabledSettingState(item.component.isEnabled)
                    }
                }
                val item = providers.values.filter { it.component.name == componentName.className }
                if (item.isNotEmpty()) {
                    return getComponentEnabledSettingState(item.first().component.isEnabled)
                }
            }
        }
        return packageManager.getComponentEnabledSetting(componentName)
    }

    private fun getComponentEnabledSettingState(isEnabled: Boolean): Int {
        if (isEnabled) return PackageManager.COMPONENT_ENABLED_STATE_ENABLED
        return PackageManager.COMPONENT_ENABLED_STATE_DISABLED
    }

    override fun setComponentEnabledSetting(componentName: ComponentName, newState: Int, flags: Int) {
        val packageName = componentName.packageName
        val isEnabled = newState == PackageManager.COMPONENT_ENABLED_STATE_ENABLED
        if (programs.containsKey(packageName)) {
            programs.getValue(packageName).packageComponents.run {
                for (map in listOf(activities, services, receivers)) {
                    val item = map[componentName.className]
                    if (item != null) {
                        item.component.enabled = isEnabled
                        return
                    }
                }
                val item = providers.values.filter { it.component.name == componentName.className }
                if (item.isNotEmpty()) {
                    item.first().component.enabled = isEnabled
                    return
                }
            }
        }
        return packageManager.setComponentEnabledSetting(componentName, newState, flags)
    }

    private inline fun <T : ComponentInfo> queryIntentComponents(
            intent: Intent?,
            flags: Int,
            fn: Program.() -> Map<String, AndroidPackageComponents.Item<T>>): MutableList<T> {

        try {
            if (intent == null) return mutableListOf()
            val result = mutableListOf<T>()
            val componentName = intent.component
            if (componentName != null) {
                val program = getProgram(componentName.packageName)
                if (program != null) {
                    val componentInfo = fn(program)[componentName.className]?.component
                    if (componentInfo != null) result.add(componentInfo)
                }
                return result
            }

            for (program in programs.values) {
                for (component in fn(program).values) {
                    for (intentFilter in component.intentFilters) {
                        if (IntentMatcher.match(MainAppContext.get(), intent, intentFilter)) {
                            result.add(component.component)
                        }
                    }
                }
            }
//            Logdog.debug(result)
            return result
        } catch (e: Throwable) {
            JCLogger.error(e)
            return mutableListOf()
        }
    }

    companion object {
        private const val PLUGIN_DIR = "jc_plugins"

        private val instance by lazy { JessieProgramManagerImpl() }

        fun get() = instance
    }
}