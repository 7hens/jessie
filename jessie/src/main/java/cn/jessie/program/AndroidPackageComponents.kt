package cn.jessie.program

import android.content.IntentFilter
import android.content.pm.*

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
    }

    data class Item<T : ComponentInfo>(
            val component: T,
            val intentFilters: List<IntentFilter>
    )
}