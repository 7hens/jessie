package android.app

import android.content.Intent
import android.content.res.Configuration
import android.os.IBinder
import java.io.FileDescriptor
import java.io.PrintWriter

abstract class ServiceWrapper : Service() {
    abstract val base: Service

    override fun onConfigurationChanged(newConfig: Configuration) {
        base.onConfigurationChanged(newConfig)
    }

    override fun onRebind(intent: Intent?) {
        base.onRebind(intent)
    }

    override fun dump(fd: FileDescriptor?, writer: PrintWriter?, args: Array<out String>?) {
        base.dump(fd, writer, args)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return base.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        base.onCreate()
    }

    override fun onLowMemory() {
        base.onLowMemory()
    }

    override fun onStart(intent: Intent?, startId: Int) {
        base.onStart(intent, startId)
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        base.onTaskRemoved(rootIntent)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return base.onBind(intent)
    }

    override fun onTrimMemory(level: Int) {
        base.onTrimMemory(level)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return base.onUnbind(intent)
    }

    override fun onDestroy() {
        base.onDestroy()
    }
}