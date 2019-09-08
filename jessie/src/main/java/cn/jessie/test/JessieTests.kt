package cn.jessie.test

import android.view.LayoutInflater
import cn.jessie.etc.Logdog
import cn.jessie.etc.Reflections
import cn.jessie.main.MainAppContext
import cn.jessie.program.AndroidHook
import java.lang.reflect.Constructor
import java.util.*

@Suppress("UNCHECKED_CAST", "MemberVisibilityCanBePrivate")
internal object JessieTests {
    fun inflater(layoutInflater: LayoutInflater) {
        val constructorMap = Reflections.get(layoutInflater, "sConstructorMap") as HashMap<String, Constructor<*>>
        var debugText = ""
        constructorMap.values.forEach {
            debugText += it.declaringClass.name + ": " + it.declaringClass.classLoader + "\n"
        }
        Logdog.debug(debugText)
    }

    fun printClass(clazz: Class<*>) {
        var cls: Class<*>? = clazz
        var result = ""
        while (cls != null) {
            result += cls.canonicalName!! + " (${cls.classLoader})\n"
            cls = cls.superclass
        }
        Logdog.debug(result)
    }

    fun printClass(className: String) {
        val classLoader = AndroidHook.classLoader(MainAppContext.get().classLoader)
        printClass(classLoader.loadClass(className))
    }
}