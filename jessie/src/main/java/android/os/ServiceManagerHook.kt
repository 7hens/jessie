package android.os

import cn.jessie.etc.Logdog

object ServiceManagerHook {
    @JvmStatic
    fun getService(name: String): IBinder? {
        Logdog.error("ServiceManagerHook.getService($name)")
        return ServiceManager.getService(name)
    }

    @JvmStatic
    fun getServiceOrThrow(name: String): IBinder? {
        return ServiceManager.getServiceOrThrow(name)
    }

    @JvmStatic
    fun addService(name: String, service: IBinder) {
        ServiceManager.addService(name, service)
    }

    @JvmStatic
    fun addService(name: String, service: IBinder, allowIsolated: Boolean) {
        ServiceManager.addService(name, service, allowIsolated)
    }

    @JvmStatic
    fun checkService(name: String): IBinder? {
        return ServiceManager.checkService(name)
    }

    @JvmStatic
    fun listServices(): Array<String>? {
        return ServiceManager.listServices()
    }

    @JvmStatic
    fun initServiceCache(cache: Map<String, IBinder>) {
        ServiceManager.initServiceCache(cache)
    }
}
