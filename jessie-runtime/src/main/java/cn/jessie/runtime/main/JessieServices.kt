package cn.jessie.runtime.main

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.IBinder
import cn.jessie.runtime.etc.BinderCursor
import cn.jessie.runtime.etc.Init
import cn.jessie.runtime.etc.JCLogger
import cn.jessie.runtime.main.JessieServices.DaemonServiceConnection.bindService
import cn.thens.okbinder.OkBinder
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicReference

/**
 * Jessie 服务管理器
 *
 * @author 7hens
 */
internal object JessieServices {
    private const val PROGRAM = "program"

    private val init = Init.create()
    private val uri by lazy { Uri.parse("content://" + JessieDaemonProviderImpl.authority) }

    private fun query(context: Context, name: String): IBinder? {
        return context.contentResolver.query(uri, null, name, null, null)?.use { cursor ->
            BinderCursor.getBinder(cursor)
        }
    }

    fun initialize(context: Context) {
        if (init.getElseInitialize()) return
        JCLogger.debug("isHost = ${MainAppContext.isHost()}")
        if (!MainAppContext.isHost()) return
        @Suppress("RemoveRedundantQualifierName")
        DaemonServiceConnection.bindService(context)
    }

    val programManager: IJessieProgramManager get() = ProgramManagerProvider.get()

    private object ProgramManagerProvider {
        private val ref = AtomicReference<IJessieProgramManager>(null)

        fun get(): IJessieProgramManager {
            if (ref.get() == null) {
                val context = MainAppContext.get()
                val binder = query(context, PROGRAM)!!
                binder.linkToDeath(object : IBinder.DeathRecipient {
                    override fun binderDied() {
                        binder.unlinkToDeath(this, 0)
                    }
                }, 0)
                val programManager = OkBinder.proxy(IJessieProgramManager::class.java, binder)
                ref.compareAndSet(null, programManager)
            }
            return ref.get()
        }
    }

    /**
     * 用于保活守护进程。
     *
     * 当守护进程意外退出时，
     * [DaemonServiceConnection] 会通过调用 [bindService] 来启动 [JessieDaemonService]。
     */
    private object DaemonServiceConnection : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            bindService(MainAppContext.get())
        }

        @SuppressLint("WrongConstant")
        fun bindService(context: Context) {
            context.bindService(Intent(context, JessieDaemonService::class.java),
                    this, Context.BIND_AUTO_CREATE)
        }
    }

    object Daemon {
        private val init = Init.create()

        fun initialize() {
            if (init.getElseInitialize()) return
            JessieProgramManagerImpl.get().installAll()
        }

        private val services = ConcurrentHashMap<String, IBinder?>()

        fun query(name: String): IBinder? {
            return services.getOrPut(name) {
                when (name) {
                    PROGRAM -> OkBinder.create(JessieProgramManagerImpl.get())
                    else -> null
                }
            }
        }

    }
}