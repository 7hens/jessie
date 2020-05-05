package cn.jessie.runtime.app.service

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import cn.jessie.runtime.app.provider.JessieStubProvider
import cn.jessie.runtime.etc.BinderCursor
import cn.jessie.runtime.etc.JCLogger
import cn.jessie.runtime.main.JessieServices
import cn.jessie.runtime.main.MainAppContext
import cn.jessie.runtime.main.Processes
import cn.thens.okbinder.OkBinder
import java.util.concurrent.ConcurrentHashMap

/**
 * 插件的 Service 启动器。
 *
 * 插件的 Service 都是交由同进程的 [JessieStubProvider] 来管理。
 *
 * @author 7hens
 */
@Suppress("MemberVisibilityCanBePrivate")
internal object ServiceExecutor {
    private val programManager get() = JessieServices.programManager

    fun startService(intent: Intent): ComponentName? {
        try {
            val serviceInfo = resolveIService(intent)
            if (serviceInfo == null || !programManager.containsProgram(serviceInfo.packageName)) {
                return MainAppContext.get().startService(intent)
            }
            val component = ComponentName(serviceInfo.packageName, serviceInfo.name)
            return getServiceManager(serviceInfo).startService(component, intent)
        } catch (e: Exception) {
            JCLogger.error(e)
            return null
        }
    }

    fun stopService(intent: Intent): Boolean {
        try {
            val serviceInfo = resolveIService(intent)
            if (serviceInfo == null || !programManager.containsProgram(serviceInfo.packageName)) {
                return MainAppContext.get().stopService(intent)
            }
            val component = ComponentName(serviceInfo.packageName, serviceInfo.name)
            return getServiceManager(serviceInfo).stopService(component, intent)
        } catch (e: Exception) {
            JCLogger.error(e)
            return false
        }
    }

    fun bindService(intent: Intent, conn: ServiceConnection, flags: Int): Boolean {
        try {
            val serviceInfo = resolveIService(intent)
            if (serviceInfo == null || !programManager.containsProgram(serviceInfo.packageName)) {
                return MainAppContext.get().bindService(intent, conn, flags)
            }
            val component = ComponentName(serviceInfo.packageName, serviceInfo.name)
            serviceConnection.add(component, conn)
            return getServiceManager(serviceInfo).bindService(component, intent, serviceConnection, flags)
        } catch (e: Exception) {
            JCLogger.error(e)
            return false
        }
    }

    fun unbindService(conn: ServiceConnection) {
        try {
            val component = serviceConnection.remove(conn) ?: return
            val serviceInfo = resolveIService(Intent().setComponent(component))
            if (serviceInfo == null || !programManager.containsProgram(serviceInfo.packageName)) {
                MainAppContext.get().unbindService(conn)
                return
            }
            conn.onServiceDisconnected(component)
            getServiceManager(serviceInfo).unbindService(component, serviceConnection)
        } catch (e: Exception) {
            JCLogger.error(e)
        }
    }

    private fun resolveIService(intent: Intent): ServiceInfo? {
        val services = programManager.queryIntentServices(intent, 0)
        if (services.isEmpty()) {
            JCLogger.error(intent).error(Throwable())
        }
        return services.firstOrNull()?.serviceInfo
    }

    private val serviceManagers = hashMapOf<String, IJessieServiceManager>()

    private fun getServiceManager(serviceInfo: ServiceInfo): IJessieServiceManager {
        val processName = Processes.processName(serviceInfo)
        return serviceManagers.getOrPut(processName) {
            val processIndex = programManager.requireProcessIndex(processName)
            val uri = JessieStubProvider.uri(processIndex, JessieStubProvider.ACTION_SERVICE)
            val binder = MainAppContext.get().contentResolver.query(uri, null, null, null, null)?.use { BinderCursor.getBinder(it) }
            OkBinder.proxy(IJessieServiceManager::class.java, binder!!)
        }
    }

    fun component(intent: Intent): ComponentName {
        return intent.component!!
    }

    private val serviceConnection by lazy {
        object : IJessieServiceConnection {
            private val connections: MutableMap<ComponentName, ServiceConnection> = ConcurrentHashMap()

            override fun onConnect(component: ComponentName, binder: IBinder?) {
                val conn = connections[component] ?: return
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    if (binder == null) {
                        conn.onNullBinding(component)
                    }
                }
//            Logdog.debug(component)
                conn.onServiceConnected(component, binder)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    binder?.linkToDeath({ conn.onBindingDied(component) }, 0)
                }
            }

            fun add(component: ComponentName, conn: ServiceConnection) {
                connections[component] = conn
            }

            fun remove(conn: ServiceConnection): ComponentName? {
                val component = get(conn) ?: return null
                connections.remove(component)
                return component
            }

            private fun get(conn: ServiceConnection): ComponentName? {
                for ((key, value) in connections.entries) {
                    if (value == conn) return key
                }
                return null
            }
        }
    }
}