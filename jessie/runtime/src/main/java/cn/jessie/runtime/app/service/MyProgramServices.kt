package cn.jessie.runtime.app.service

import android.app.Service
import android.content.ComponentName
import cn.jessie.runtime.app.MyProgram
import cn.jessie.runtime.app.ProgramContext
import cn.jessie.runtime.etc.Reflections
import cn.jessie.runtime.main.MainAppContext
import java.lang.reflect.Modifier

internal object MyProgramServices {
    private val cache = mutableMapOf<ComponentName, Service>()

    fun contains(component: ComponentName): Boolean {
        return cache.containsKey(component)
    }

    fun get(component: ComponentName): Service? {
        return cache[component]
    }

    /**
     * get or create Service
     */
    fun require(component: ComponentName): Service? {
        return get(component) ?: run {
            val service = MyProgram.classLoader.loadClass(component.className).newInstance() as Service
            cache[component] = service
            val programContext = ProgramContext(MainAppContext.get())
            Reflections.invoke(service, "attachBaseContext", programContext)
            service.onCreate()
            service
        }
    }

    fun require(stubService: Service, component: ComponentName): Service? {
        return get(component) ?: run {
            val service = MyProgram.classLoader.loadClass(component.className).newInstance() as Service
            cache[component] = service
            var debugText = ""
            for (field in Service::class.java.declaredFields) {
                if (Modifier.isStatic(field.modifiers)) continue
                field.isAccessible = true
                debugText += "\n${field.name}"
                field.set(service, field.get(stubService))
            }
//            Logdog.debug(debugText)
            Reflections.invoke(service, "attachBaseContext", ProgramContext(stubService))
            service.onCreate()
            service
        }
    }

    fun remove(component: ComponentName) {
        cache.remove(component)
    }

    fun each(fn: (Service) -> Unit) {
        cache.values.forEach { fn(it) }
    }
}