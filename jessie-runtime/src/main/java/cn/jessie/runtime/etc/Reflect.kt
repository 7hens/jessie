package cn.jessie.runtime.etc

import android.os.Build
import java.lang.reflect.*

/**
 * @author 7hens
 */
@Suppress("MemberVisibilityCanBePrivate", "unused", "UNCHECKED_CAST")
internal interface Reflect<T : Any> {
    fun type(): Class<T>
    fun value(): T?
    operator fun get(name: String): Reflect<*>
    operator fun set(name: String, value: Any?): Reflect<T>
    operator fun invoke(name: String, vararg args: Any?): Reflect<*>
    fun instantiate(vararg args: Any?): Reflect<T>
    fun field(name: String): Field
    fun fields(): Map<String, Field>
    fun method(name: String, vararg paramTypes: Class<*>): Method
    fun constructor(vararg paramTypes: Class<*>): Constructor<T>
    fun proxy(handler: (proxy: Any, method: Method, args: Array<*>?) -> Any?): T
    fun <U : Any> cast(type: Class<U>): Reflect<U>
    fun on(value: T): Reflect<T>

    companion object {
        fun <T : Any> on(cls: Class<T>): Reflect<T> {
            return ReflectImpl(cls, null)
        }

        fun on(className: String): Reflect<*> {
            return on(Class.forName(className))
        }

        fun <T : Any> on(obj: T): Reflect<T> {
            return ReflectImpl(obj.javaClass, obj)
        }

        fun root() {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) return
            try {
                val cClass = Class::class.java
                val cString = String::class.java
                val cClassArray = arrayOf(Object::class.java).javaClass

                val forName = cClass.getDeclaredMethod("forName", cString)
                val getDeclaredMethod = cClass.getDeclaredMethod("getDeclaredMethod", cString, cClassArray)

                val cVMRuntime = forName.invoke(null, "dalvik.system.VMRuntime") as Class<*>
                val getRuntime = getDeclaredMethod.invoke(cVMRuntime, "getRuntime", null) as Method
                val setHiddenApiExemptions = getDeclaredMethod.invoke(cVMRuntime,
                        "setHiddenApiExemptions", arrayOf<Class<*>>(Array<String>::class.java)) as Method
                setHiddenApiExemptions.invoke(getRuntime.invoke(null), arrayOf("L"))
            } catch (e: Throwable) {
                JCLogger.error(e)
            }
        }

        private val nil: Reflect<*> by lazy {
            on(Reflect::class.java).proxy { _, method, args ->
                if (method.name == "value" && args.isNullOrEmpty()) {
                    null
                } else {
                    throw NullPointerException()
                }
            }
        }
    }

    private class ReflectImpl<T : Any>(val cls: Class<T>, val obj: T?) : Reflect<T> {
        override fun type(): Class<T> {
            return cls
        }

        override fun value(): T? {
            return obj
        }

        override operator fun get(name: String): Reflect<*> {
            val field = field(name)
            field.isAccessible = true
            val result = field.get(obj) ?: return nil
            return on(result)
        }

        override operator fun set(name: String, value: Any?): Reflect<T> {
            val field = field(name)
            field.isAccessible = true
            field.set(obj, value)
            return this
        }

        override operator fun invoke(name: String, vararg args: Any?): Reflect<*> {
            val method = method(name, *typesOf(args))
            method.isAccessible = true
            val result = method.invoke(obj, *args) ?: return nil
            return on(result)
        }

        override fun instantiate(vararg args: Any?): Reflect<T> {
            val constructor = constructor(*typesOf(args))
            constructor.isAccessible = true
            val result = constructor.newInstance(*args)
            return ReflectImpl(cls, result)
        }

        @Throws(NoSuchFieldException::class)
        override fun field(name: String): Field {
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

        override fun fields(): Map<String, Field> {
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

        override fun method(name: String, vararg paramTypes: Class<*>): Method {
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

        @Throws(NoSuchMethodException::class)
        override fun constructor(vararg paramTypes: Class<*>): Constructor<T> {
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

        override fun proxy(handler: (proxy: Any, method: Method, args: Array<*>?) -> Any?): T {
            return Proxy.newProxyInstance(cls.classLoader, arrayOf(cls), handler) as T
        }

        override fun <U : Any> cast(type: Class<U>): Reflect<U> {
            if (obj == null) throw NullPointerException()
            return ReflectImpl(type, obj as U)
        }

        override fun on(value: T): Reflect<T> {
            return ReflectImpl(cls, value)
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
            return type.kotlin.javaObjectType
        }

        private class NULL
    }
}