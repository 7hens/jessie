package cn.jessie.sample.plugin

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Bundle

class PluginApp : Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        PluginLog.debug("Application.attachBaseContext")
    }

    @SuppressLint("WrongConstant")
    override fun onCreate() {
        super.onCreate()
        manageActivityLifecycleCallbacks()
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        PluginLog.debug(activityManager.runningAppProcesses)
        PluginLog.debug("PluginApp.onCreate")
    }

    private fun manageActivityLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            private fun log(activity: Activity, lifecycle: String) {
                PluginLog.debug("$lifecycle(${activity.javaClass.name})")
            }

            override fun onActivityPaused(activity: Activity) {
                log(activity, "onActivityPaused")
            }

            override fun onActivityResumed(activity: Activity) {
                log(activity, "onActivityResumed")
            }

            override fun onActivityStarted(activity: Activity) {
                log(activity, "onActivityStarted")
            }

            override fun onActivityDestroyed(activity: Activity) {
                log(activity, "onActivityDestroyed")
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {
                log(activity, "onActivitySaveInstanceState")
            }

            override fun onActivityStopped(activity: Activity) {
                log(activity, "onActivityStopped")
            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                log(activity, "onActivityCreated")
            }
        })
    }
}