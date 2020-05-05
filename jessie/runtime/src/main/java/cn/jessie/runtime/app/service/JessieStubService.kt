package cn.jessie.runtime.app.service

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.os.IBinder
import cn.jessie.runtime.etc.JCLogger
import cn.jessie.runtime.main.JessieStubComponents
import cn.jessie.runtime.main.MainAppContext
import cn.jessie.runtime.main.Processes

abstract class JessieStubService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val result = super.onStartCommand(intent, flags, startId)
        if (intent == null) return result
        val programIntent = intent.getParcelableExtra<Intent>(EXTRA_INTENT)
        val component = intent.getParcelableExtra<ComponentName>(EXTRA_COMPONENT)
        val action = intent.getIntExtra(EXTRA_ACTION, ACTION_START)
        JCLogger.debug("$programIntent #$action")
        when (action) {
            ACTION_START -> {
                val service = MyProgramServices.require(this, component)!!
                service.onStartCommand(programIntent, 0, 0)
                JCLogger.debug(service)
            }
            ACTION_STOP -> {
                MyProgramServices.get(component)?.onDestroy()
            }
            ACTION_BIND -> {
                val service = MyProgramServices.require(this, component)!!
                val binder = service.onBind(programIntent)
                val conn = connections[component]!!
                conn.onConnect(component, binder)
                binder?.linkToDeath({ connections.remove(component) }, 0)
//                Logdog.debug(binder)
            }
            ACTION_UNBIND -> {
                MyProgramServices.get(component)?.onUnbind(programIntent)
            }
        }
        return result
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    internal companion object {
        private const val EXTRA_INTENT = "INTENT"
        private const val EXTRA_COMPONENT = "COMPONENT"
        private const val EXTRA_ACTION = "ACTION"
        private const val EXTRA_CONNECTION = "CONNECTION"
        const val ACTION_START = 1
        const val ACTION_STOP = 2
        const val ACTION_BIND = 3
        const val ACTION_UNBIND = 4
        private val connections = HashMap<ComponentName, IJessieServiceConnection>()

        val serviceManager by lazy {
            object : IJessieServiceManager {
                override fun startService(component: ComponentName, intent: Intent): ComponentName {
                    MainAppContext.get().startService(wrapIntent(ACTION_START, intent, component))
                    return component
                }

                override fun stopService(component: ComponentName, intent: Intent): Boolean {
                    MainAppContext.get().startService(wrapIntent(ACTION_STOP, intent, component))
                    return true
                }

                override fun bindService(component: ComponentName, intent: Intent, conn: IJessieServiceConnection, flags: Int): Boolean {
                    MainAppContext.get().startService(wrapIntent(ACTION_BIND, intent, component))
                    connections[component] = conn
                    return true
                }

                override fun unbindService(component: ComponentName, conn: IJessieServiceConnection): Boolean {
                    val intent = Intent().setComponent(component)
                    MainAppContext.get().startService(wrapIntent(ACTION_UNBIND, intent, component))
                    connections.remove(component)
                    return true
                }

                private fun serviceClassNameOf(index: Int): String {
                    return JessieStubComponents.getServiceClassName(index, 0)
                }

                private fun wrapIntent(action: Int, intent: Intent, component: ComponentName): Intent {
                    val index = Processes.getCurrentJessieProcessIndex()
                    val stubService = serviceClassNameOf(index)
                    return Intent()
                            .setComponent(ComponentName(MainAppContext.packageName, stubService))
                            .putExtra(EXTRA_ACTION, action)
                            .putExtra(EXTRA_INTENT, intent)
                            .putExtra(EXTRA_COMPONENT, component)
                }
            }
        }

    }
}