package android.app

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.res.Configuration
import android.os.IBinder
import java.io.FileDescriptor
import java.io.PrintWriter

abstract class ServiceWrapper : Service() {
    abstract val baseService: Service

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        val cContextWrapper = ContextWrapper::class.java
        val cContext = Context::class.java
        val attachBaseContext = cContextWrapper.getDeclaredMethod("attachBaseContext", cContext)
        attachBaseContext.isAccessible = true
        attachBaseContext.invoke(baseService, this)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        baseService.onConfigurationChanged(newConfig)
    }

    override fun onRebind(intent: Intent?) {
        baseService.onRebind(intent)
    }

    override fun dump(fd: FileDescriptor?, writer: PrintWriter?, args: Array<out String>?) {
        baseService.dump(fd, writer, args)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return baseService.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        baseService.onCreate()
    }

    override fun onLowMemory() {
        baseService.onLowMemory()
    }

    override fun onStart(intent: Intent?, startId: Int) {
        baseService.onStart(intent, startId)
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        baseService.onTaskRemoved(rootIntent)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return baseService.onBind(intent)
    }

    override fun onTrimMemory(level: Int) {
        baseService.onTrimMemory(level)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return baseService.onUnbind(intent)
    }

    override fun onDestroy() {
        baseService.onDestroy()
    }
}