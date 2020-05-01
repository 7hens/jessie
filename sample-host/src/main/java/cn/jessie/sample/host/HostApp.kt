package cn.jessie.sample.host

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Bundle
import cn.jessie.Jessie
import cn.jessie.etc.JCLogger

class HostApp : Application() {
    override fun onCreate() {
        super.onCreate()
        GlobalBroadcast.initialize(this)
        GlobalBroadcast.get().apply {
            receive("stop").subscribe { programPackageName ->
                if (programPackageName.isNotEmpty()) {
                    val program = Jessie.getProgram(programPackageName)
                            ?: throw RuntimeException("program($programPackageName) not exists")
                    program.stop()
                } else {
                    Jessie.programs.values.forEach { it.stop() }
                }
            }
            receive("start").subscribe { programPackageName ->
                val program = Jessie.getProgram(programPackageName)
                        ?: throw RuntimeException("program($programPackageName) not exists")
                program.start()
            }
        }
    }

    @SuppressLint("WrongConstant")
    private fun isOnMainProcess(): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val myPid = android.os.Process.myPid()
        for (runningAppProcess in activityManager.runningAppProcesses) {
            if (runningAppProcess.pid == myPid) {
                if (runningAppProcess.processName == packageName) {
                    return true
                }
            }
        }
        return false
    }

    private fun manageActivityLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity) {
                JCLogger.debug("${activity.javaClass.name}#onActivityPaused")
            }

            override fun onActivityResumed(activity: Activity) {
                JCLogger.debug("${activity.javaClass.name}#onActivityResumed")
            }

            override fun onActivityStarted(activity: Activity) {
                JCLogger.debug("${activity.javaClass.name}#onActivityStarted")
            }

            override fun onActivityDestroyed(activity: Activity) {
                JCLogger.debug("${activity.javaClass.name}#onActivityDestroyed")
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {
                JCLogger.debug("${activity.javaClass.name}#onActivitySaveInstanceState")
            }

            override fun onActivityStopped(activity: Activity) {
                JCLogger.debug("${activity.javaClass.name}#onActivityStopped")
            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                JCLogger.debug("${activity.javaClass.name}#onActivityCreated")
            }
        })
    }
}