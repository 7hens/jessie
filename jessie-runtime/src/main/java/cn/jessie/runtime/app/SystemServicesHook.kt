package cn.jessie.runtime.app

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.ActivityManagerNative
import android.app.IActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.Process
import android.os.ServiceManager
import cn.jessie.Jessie
import cn.jessie.runtime.etc.Init
import cn.jessie.runtime.etc.JCLogger
import cn.jessie.runtime.etc.Reflections
import cn.jessie.runtime.main.JessieServices

@Suppress("UNCHECKED_CAST")
@SuppressLint("WrongConstant")
internal object SystemServicesHook {
    private val init = Init.create()

    fun initialize() {
        if (init.getElseInitialize()) return
        hookActivityManager()
    }

    private fun hookActivityManager() {
        try {
            val singleton = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Reflections.get(ActivityManager::class.java, "IActivityManagerSingleton")!!
            } else {
                Reflections.get(ActivityManagerNative::class.java, "gDefault")!!
            }
            val iActivityManager = Reflections.get(singleton, "mInstance")!!
            Reflections.set(singleton, "mInstance", proxyActivityManager(iActivityManager))
        } catch (e: Throwable) {
            JCLogger.error(e)
        }
    }

    private fun proxyActivityManager(originalActivityManager: Any): Any {
        return Reflections.proxy<IActivityManager> { _, method, args ->
            val methodName = method.name
            when {
                methodName == "getRunningAppProcesses" -> {
                    val result = method.invoke(originalActivityManager)
                    val list = result as List<ActivityManager.RunningAppProcessInfo>
                    val currentProcess = list.first { it.pid == Process.myPid() }
                    currentProcess.processName = Jessie.currentProgramProcessName
                    list
                }
                methodName == "startActivity" -> {
                    requireNotNull(args)
                    method.invoke(originalActivityManager, *Array(args.size) { index ->
                        val argument = args[index]
                        if (argument is Intent) {
                            JessieServices.programManager.wrapActivityIntent(argument)
                        } else {
                            argument
                        }
                    })
                }
                args.isNullOrEmpty() -> method.invoke(originalActivityManager)
                else -> method.invoke(originalActivityManager, *args)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun hookServiceManager() {
        try {
            val cServiceManager = ServiceManager::class.java
            for (declaredField in cServiceManager.declaredFields) {
                if (declaredField.name == "sCache") {
                    declaredField.isAccessible = true
                    val cache = declaredField.get(null) as HashMap<String, Any>
                    val binder = ServiceManager.getService(Context.ACTIVITY_SERVICE)
                    cache[Context.ACTIVITY_SERVICE] = Reflections.proxy<IBinder> { _, method, args ->
                        JCLogger.error(method.name)
                        if (args == null || args.isEmpty()) {
                            method.invoke(binder)
                        } else {
                            method.invoke(binder, *args)
                        }
                    }
                }
            }
        } catch (e: Throwable) {
            JCLogger.error(e)
        }
    }
}