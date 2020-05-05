package cn.jessie.runtime.program

import android.content.Context
import android.content.res.Resources
import cn.jessie.etc.JCLogger
import cn.jessie.main.MainAppContext

abstract class DexProgram : AbstractProgram() {

    override val packageComponents by lazy {
        AndroidPackageComponents.parse(packageInfo, resources.assets)
    }

    override val packageInfo by lazy {
        AndroidPackageInfo.get(dexInfo)
    }

    override val classLoader: ClassLoader by lazy {
        MainAppContext.get().classLoader
                .let { AndroidHook.classLoader(it) }
                .let { PluginClassLoader(it, dexInfo) }
    }

    override val resources: Resources by lazy {
        val context = MainAppContext.get()
        val packageManager = context.packageManager
        val resources = packageManager.getResourcesForApplication(packageInfo.applicationInfo)
        MergedResources(resources, context.resources)
    }

    private fun getResource(context: Context, resources: Resources, packageName: String): Resources {
        val configRes = resources
        return object : Resources(resources.assets, configRes.displayMetrics, configRes.configuration) {
            override fun getIdentifier(name: String?, defType: String?, defPackage: String?): Int {
                JCLogger.debug("name = $name\ndefType = $defType\ndefPackage = $defPackage")
                var id = super.getIdentifier(name, defType, packageName)
                if (id == 0) {
                    id = super.getIdentifier(name, defType, defPackage)
                }
                return id
            }
        }
    }

    override fun preload() {
        try {
            classLoader
            packageComponents
        } catch (e: Throwable) {
            JCLogger.error(e).error(packageName)
        }
    }

    private class PluginClassLoader(parent: ClassLoader?, dexInfo: DexInfo)
        : DexInfo.ClassLoader(parent, dexInfo) {

        override fun loadClass(name: String, resolve: Boolean): Class<*> {
            if (name in AndroidHook.hookedClassNames) {
                return parent.loadClass(name)
            }
            return findLoadedClass(name) ?: run {
                try {
                    findClass(name)!!
                } catch (e: Throwable) {
                    parent.loadClass(name)
                }
            }
        }
    }
}