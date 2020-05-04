package cn.jessie.app.application

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks
import cn.jessie.app.activity.JessieStubActivity
import cn.jessie.etc.Reflections
import java.util.concurrent.ConcurrentHashMap

/**
 * @author 7hens
 */
@Suppress("RedundantOverride", "unused")
abstract class JessieBaseApplication : Application() {

    private val activityLifecycleCallbacksMap = ConcurrentHashMap<ActivityLifecycleCallbacks, ActivityLifecycleCallbacks>()

    override fun registerActivityLifecycleCallbacks(callback: ActivityLifecycleCallbacks) {
        val callbackWrapper = wrapActivityLifecycleCallbacks(callback)
        activityLifecycleCallbacksMap[callback] = callbackWrapper
        super.registerActivityLifecycleCallbacks(callbackWrapper)
    }

    override fun unregisterActivityLifecycleCallbacks(callback: ActivityLifecycleCallbacks) {
        activityLifecycleCallbacksMap[callback]?.let {
            super.unregisterActivityLifecycleCallbacks(it)
        }
        activityLifecycleCallbacksMap.remove(callback)
    }

    private fun wrapActivityLifecycleCallbacks(callback: ActivityLifecycleCallbacks): ActivityLifecycleCallbacks {
        return Reflections.proxy { _, method, args ->
            requireNotNull(args)
            val activity = args[0] as Activity
            val callbackClassLoader = callback.javaClass.classLoader
            val programActivity: Activity? = when {
                activity.javaClass.classLoader == callbackClassLoader -> activity
                activity is JessieStubActivity -> {
                    activity.programActivity.takeIf { it.javaClass.classLoader == callbackClassLoader }
                }
                else -> null
            }
            val newArgs = Array(args.size) { index ->
                if (index == 0 && programActivity != null) programActivity else args[index]
            }
            method.invoke(callback, *newArgs)
        }
    }

    override fun registerComponentCallbacks(callback: ComponentCallbacks?) {
        // TODO
        super.registerComponentCallbacks(callback)
    }
}