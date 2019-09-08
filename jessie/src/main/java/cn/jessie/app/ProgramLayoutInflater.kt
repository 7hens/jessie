package cn.jessie.app

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewStub
import cn.jessie.app.activity.JessieBaseActivity
import cn.jessie.app.activity.JessieStubActivity
import cn.jessie.etc.Logdog
import java.lang.reflect.Constructor

internal class ProgramLayoutInflater(original: LayoutInflater, newContext: Context) : LayoutInflater(original, newContext) {

    override fun cloneInContext(newContext: Context): LayoutInflater {
        val programContext = if (newContext is JessieStubActivity) newContext.programActivity else newContext
        val layoutInflater = ProgramLayoutInflater(this, programContext)
        layoutInflater.factory2 = Factory2(layoutInflater)
        return ProgramLayoutInflater(layoutInflater, programContext)
    }

    class Factory2(private val layoutInflater: LayoutInflater) : LayoutInflater.Factory2 {
        private val classPrefixes = arrayOf("android.widget.", "android.webkit.", "android.app.")
        private val constructors = hashMapOf<String, Constructor<out View>>()

        override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet?): View? {
            if (!name.contains(".")) {
                for (classPrefix in classPrefixes) {
                    val view = onCreateView(parent, classPrefix + name, context, attrs)
                    if (view != null) return view
                }
                return null
            }
            return createCustomView(name, context, attrs)
        }

        override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
            return onCreateView(null, name, context, attrs)
        }

        private fun createCustomView(name: String, context: Context, attrs: AttributeSet?): View? {
            val cacheKey = "${layoutInflater.context.packageName}-$name"
            var constructor = constructors[cacheKey]
            if (constructor != null && !verifyClassLoader(context, constructor)) {
                constructor = null
                constructors.remove(cacheKey)
            }
            try {
                if (constructor == null) {
                    val clazz = context.classLoader.loadClass(name).asSubclass(View::class.java)
                    constructor = clazz.getConstructor(Context::class.java, AttributeSet::class.java)
                    constructor.isAccessible = true
                    constructors[cacheKey] = constructor
                }

                val view = constructor!!.newInstance(context, attrs)
                if (view is ViewStub && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.layoutInflater = layoutInflater.cloneInContext(context)
                }
                return view
            } catch (e: Exception) {
                return null
            }
        }

        private fun verifyClassLoader(context: Context, constructor: Constructor<out View>): Boolean {
            val constructorClassLoader = constructor.declaringClass.classLoader
            if (constructorClassLoader === LayoutInflater::class.java.classLoader) {
                return true
            }
            var classLoader: ClassLoader? = context.classLoader
            while (classLoader != null) {
                if (constructorClassLoader === classLoader) return true
                classLoader = classLoader.parent
            }
            return false
        }
    }

}