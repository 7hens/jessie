package cn.jessie.runtime.program

import android.content.IntentFilter
import android.content.pm.*
import android.content.res.AssetManager

class AndroidPackageComponents(
        val application: ApplicationInfo,
        val activities: Map<String, Item<ActivityInfo>>,
        val services: Map<String, Item<ServiceInfo>>,
        val receivers: Map<String, Item<ActivityInfo>>,
        val providers: Map<String, Item<ProviderInfo>>) {

    val launcherActivities by lazy {
        activities.values.filter { item ->
            item.intentFilters.find { it.hasAction(ACTION_MAIN) && it.hasCategory(CATEGORY_LAUNCHER) } != null
        }
    }

    companion object {
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
                            name to Item(component, intentFilters)
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

    data class Item<T : ComponentInfo>(
            val component: T,
            val intentFilters: List<IntentFilter>
    )
}