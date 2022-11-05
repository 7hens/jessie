package cn.jessie.etc

import java.lang.reflect.Modifier

internal object ReflectionUtils {
    fun <T> copyFields(cls: Class<T>, from: T, to: T) {
        for (field in cls.declaredFields) {
            if (Modifier.isStatic(field.modifiers)) continue
            field.isAccessible = true
            field.set(to, field.get(from))
        }
    }
}