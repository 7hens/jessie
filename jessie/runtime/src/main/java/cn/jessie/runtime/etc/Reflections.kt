package cn.jessie.runtime.etc

import java.lang.reflect.*

/**
 * @author 7hens
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
internal object Reflections {

    fun get(cls: Class<*>, name: String): Any? {
        return get(cls, null, name)
    }

    fun get(obj: Any, name: String): Any? {
        return get(obj.javaClass, obj, name)
    }

    private fun get(cls: Class<*>, obj: Any?, name: String): Any? {
        return field(cls, name).let {
            it.isAccessible = true
            it.get(obj)
        }
    }

    fun set(cls: Class<*>, name: String, value: Any?) {
        set(cls, null, name, value)
    }

    fun set(obj: Any, name: String, value: Any?) {
        set(obj.javaClass, obj, name, value)
    }

    private fun set(cls: Class<*>, obj: Any?, name: String, value: Any?) {
        field(cls, name).let {
            it.isAccessible = true
            it.set(obj, value)
        }
    }

    fun invoke(cls: Class<*>, name: String, vararg args: Any?): Any? {
        return invoke(cls, null, name, *args)
    }

    fun invoke(obj: Any, name: String, vararg args: Any?): Any? {
        return invoke(obj.javaClass, obj, name, *args)
    }

    private fun invoke(cls: Class<*>, obj: Any?, name: String, vararg args: Any?): Any? {
        val method = method(cls, name, *typesOf(args))
        method.isAccessible = true
        return method.invoke(obj, *args)
    }

    fun <T> instantiate(cls: Class<T>, vararg args: Any?): T {
        return constructor(cls, *typesOf(args)).let {
            it.isAccessible = true
            it.newInstance(*args)
        }
    }

    @Throws(NoSuchFieldException::class)
    fun field(cls: Class<*>, name: String): Field {
        try {
            return cls.getField(name)
        } catch (e: Exception) {
            var type: Class<*>? = cls
            while (type != null) {
                try {
                    return type.getDeclaredField(name)
                } catch (e: NoSuchFieldException) {
                }
                type = type.superclass
            }
        }
        throw NoSuchFieldException(name)
    }

    fun fields(cls: Class<*>): Map<String, Field> {
        val map = mutableMapOf<String, Field>()
        var type: Class<*>? = cls
        while (type != null) {
            type.declaredFields.forEach { declaredField ->
                if (!Modifier.isStatic(declaredField.modifiers)) {
                    val name = declaredField.name
                    if (!map.containsKey(name)) {
                        map[name] = declaredField
                    }
                }
            }
            type = type.superclass
        }
        return map
    }

    fun method(cls: Class<*>, name: String, vararg paramTypes: Class<*>): Method {
        try {
            return cls.getMethod(name, *paramTypes)
        } catch (e: Exception) {
            var type: Class<*>? = cls
            while (type != null) {
                try {
                    return type.getDeclaredMethod(name, *paramTypes)
                } catch (e: NoSuchMethodException) {
                    for (method in type.declaredMethods) {
                        if (method.name == name && matches(method.parameterTypes, paramTypes)) {
                            return method
                        }
                    }
                }
                type = type.superclass
            }
        }
        val paramTypeNames = paramTypes.joinToString(", ") { it.name }
        throw NoSuchMethodException("$name($paramTypeNames)")
    }

    @Suppress("UNCHECKED_CAST")
    @Throws(NoSuchMethodException::class)
    fun <T> constructor(cls: Class<T>, vararg paramTypes: Class<*>): Constructor<T> {
        try {
            return cls.getDeclaredConstructor(*paramTypes)
        } catch (e: NoSuchMethodException) {
            for (constructor in cls.declaredConstructors) {
                if (matches(constructor.parameterTypes, paramTypes)) {
                    return constructor as Constructor<T>
                }
            }
            throw NoSuchMethodException()
        }
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> proxy(noinline handler: (proxy: Any, method: Method, args: Array<*>?) -> Any?): T {
        val cls = T::class.java
        return Proxy.newProxyInstance(cls.classLoader, arrayOf(cls), handler) as T
    }

    private fun matches(declaredTypes: Array<Class<*>>, actualTypes: Array<out Class<*>>): Boolean {
        if (declaredTypes.size != actualTypes.size) return false
        for (i in actualTypes.indices) {
            if (actualTypes[i] == NULL::class.java)
                continue
            if (matches(declaredTypes[i], actualTypes[i]))
                continue
            return false
        }
        return true
    }

    private fun matches(declaredType: Class<*>, actualType: Class<*>): Boolean {
        return objectTypeOf(declaredType).isAssignableFrom(objectTypeOf(actualType))
    }

    private fun typeOf(value: Any?): Class<*> {
        return value?.javaClass ?: NULL::class.java
    }

    private fun typesOf(values: Array<out Any?>): Array<Class<*>> {
        return Array(values.size) { i -> typeOf(values[i]) }
    }

    private fun objectTypeOf(type: Class<*>): Class<*> {
        if (!type.isPrimitive) return type
        return when (type) {
            Boolean::class.javaPrimitiveType -> Boolean::class.javaObjectType
            Byte::class.javaPrimitiveType -> Byte::class.javaObjectType
            Char::class.javaPrimitiveType -> Char::class.javaObjectType
            Short::class.javaPrimitiveType -> Short::class.javaObjectType
            Int::class.javaPrimitiveType -> Int::class.javaObjectType
            Long::class.javaPrimitiveType -> Long::class.javaObjectType
            Float::class.javaPrimitiveType -> Float::class.javaObjectType
            Double::class.javaPrimitiveType -> Double::class.javaObjectType
            Void::class.javaPrimitiveType -> Void::class.javaObjectType
            else -> throw RuntimeException("unknown primitive type: " + type.canonicalName)
        }
    }

    private class NULL
}