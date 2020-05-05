package cn.jessie.runtime.main

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * 守护者进程 Service，do nothing。
 *
 * 该 Service 只用来负责启动守护者进程（:jc 进程）。在守护者进程启动之后，会自动启动 [JessieDaemonProvider]。
 */
class JessieDaemonService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}