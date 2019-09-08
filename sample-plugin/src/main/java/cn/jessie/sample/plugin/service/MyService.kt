package cn.jessie.sample.plugin.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import cn.jessie.sample.plugin.PluginLog

abstract class MyService : Service() {
    override fun onCreate() {
        super.onCreate()
        print("Service.onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        print("Service.onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        print("Service.onDestroy")
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        print("Service.onBind")
        return null
    }

    override fun onUnbind(intent: Intent?): Boolean {
        print("Service.onUnbind")
        return true
    }

    private fun print(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        PluginLog.debug(text)
    }

    class Local : MyService()

    class Remote : MyService()
}