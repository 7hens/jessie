package cn.jessie.runtime.program

import android.content.IntentFilter
import android.content.pm.ComponentInfo
import android.content.pm.PackageInfo
import android.content.res.AssetManager
import cn.jessie.program.AndroidPackageComponents

object AndroidPackageComponentsParser  {
    private const val ACTION_MAIN = "android.intent.action.MAIN"
    private const val CATEGORY_LAUNCHER = "android.intent.category.LAUNCHER"

    val launcherFilter by lazy {
        IntentFilter().apply {
            addAction(ACTION_MAIN)
            addCategory(CATEGORY_LAUNCHER)
        }
    }

    fun parse(packageInfo: PackageInfo, assetManager: AssetManager): AndroidPackageComponents {
        val intentFilterMap = AndroidManifestParser.parse(assetManager)
        fun <T : ComponentInfo> map(components: Array<T>, key: T.() -> String) = run {
            components
                    .map { component ->
                        val name = key(component)
                        val intentFilters = intentFilterMap[name] ?: emptyList()
                        name to AndroidPackageComponents.Item(component, intentFilters)
                    }
                    .toMap()
        }

        return AndroidPackageComponents(
                packageInfo.applicationInfo,
                map(packageInfo.activities) { name },
                map(packageInfo.services) { name },
                map(packageInfo.receivers) { name },
                map(packageInfo.providers) { authority }
        )
    }
}