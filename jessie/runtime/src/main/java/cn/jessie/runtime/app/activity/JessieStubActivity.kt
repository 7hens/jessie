package cn.jessie.runtime.app.activity

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.AssetManager
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import cn.jessie.program.Program
import cn.jessie.runtime.app.MyProgram
import cn.jessie.runtime.app.ProgramContext
import cn.jessie.runtime.app.application.MyProgramApp
import cn.jessie.runtime.etc.Bitmaps
import cn.jessie.runtime.etc.Init
import cn.jessie.runtime.etc.JCLogger
import cn.jessie.runtime.etc.Reflections
import cn.jessie.runtime.main.JessieServices
import cn.jessie.runtime.main.MainAppContext
import cn.jessie.runtime.test.JessieTests
import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.lang.reflect.Modifier

abstract class JessieStubActivity : Activity() {
    internal lateinit var programActivity: Activity
    private lateinit var programActivityInfo: ActivityInfo
    private val program: Program = MyProgram
    private val programManager get() = JessieServices.programManager
    private val runOnReady = RunOnReady()

    private val reflections by lazy {
        try {
            ActivityReflections(program.classLoader, programActivity.javaClass)
        } catch (e: Throwable) {
            JCLogger.error(e)
            ActivityReflections(classLoader, Activity::class.java)
        }
    }

    private fun initProgramActivity() {
        val programIntent = intent.getParcelableExtra(EXTRA_INTENT) as? Intent ?: Intent()
        val packageName = intent.getStringExtra(EXTRA_PACKAGE) ?: ""
        val className = intent.getStringExtra(EXTRA_CLASS) ?: ""
        require(packageName.isNotEmpty()) { "packageName is empty" }
        require(className.isNotEmpty()) { "className is empty" }
        require(packageName == program.packageName) {
            "wrong packageName: expected ($packageName) but found (${program.packageName}), " +
                    "maybe this Activity isRunning on a wrong process"
        }
        programIntent.component = ComponentName(packageName, className)
        programIntent.setExtrasClassLoader(classLoader)
        JessieTests.printClass(classLoader.loadClass(className))
        programActivityInfo = program.packageComponents.activities.getValue(className).component
        programActivity = classLoader.loadClass(className).newInstance() as Activity
        (programActivity as? JessieBaseActivity)?.setStubActivity(this)
        for (field in Activity::class.java.declaredFields) {
            if (Modifier.isStatic(field.modifiers)) continue
            field.isAccessible = true
            field.set(programActivity, when (field.type) {
                Application::class.java -> MyProgramApp.get()
                else -> field.get(this)
            })
        }
        programActivity.intent = programIntent
        reflections.attachBaseContext.call(ProgramContext(this))
        programActivity.setTheme(getThemeResource())
        runOnReady.ready()
    }

    private fun getThemeResource(): Int {
        var themeRes = 0
        try {
            val activityInfo = program.packageInfo.activities.first { it.name == programActivity.javaClass.name }
            themeRes = activityInfo.theme
            if (themeRes == 0) {
                themeRes = program.packageInfo.applicationInfo.theme
            }
//            if (themeRes == 0) {
//                @Suppress("DEPRECATION")
//                themeRes = android.R.style.Theme_Holo_Light_NoActionBar
//            }
        } catch (e: Throwable) {
            JCLogger.error(e)
        }
        return themeRes
    }

    private val isInitialized: Boolean get() = ::programActivity.isInitialized

    override fun getTheme(): Resources.Theme {
        return if (isInitialized) programActivity.theme else super.getTheme()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            initProgramActivity()
            resetWindow()
            requestedOrientation = programActivityInfo.screenOrientation
            setLabelAndIcon()
        } catch (e: Exception) {
            JCLogger.error(e)
            finish()
        }
        super.onCreate(savedInstanceState)
        savedInstanceState?.classLoader = classLoader
        reflections.onCreate.call(savedInstanceState)
    }

    private fun setLabelAndIcon() {
        try {
            val applicationInfo = program.packageInfo.applicationInfo
            val resources = program.resources
            val label = run {
                var labelRes = programActivityInfo.labelRes
                if (labelRes == 0) labelRes = applicationInfo.labelRes
                if (labelRes == 0) program.packageName else resources.getString(labelRes)
            }
            title = label
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val drawable = run {
                    var iconRes = programActivityInfo.icon
                    if (iconRes == 0) iconRes = applicationInfo.icon
                    if (iconRes == 0) {
                        MainAppContext.get().resources.getDrawable(MainAppContext.get().applicationInfo.icon, null)
                    } else {
                        resources.getDrawable(iconRes, programActivity.theme)
                    }
                }
                @Suppress("DEPRECATION")
                setTaskDescription(ActivityManager.TaskDescription(label, Bitmaps.from(drawable)))
            }
        } catch (e: Throwable) {
            JCLogger.error(e)
        }
    }

    private fun resetWindow() {
        if (!isInitialized) return
        window.callback = programActivity
        window.setSoftInputMode(programActivityInfo.softInputMode)
        window.setUiOptions(programActivityInfo.uiOptions)
    }

    override fun onWindowAttributesChanged(params: WindowManager.LayoutParams?) {
        runOnReady.post { programActivity.onWindowAttributesChanged(params) }
        super.onWindowAttributesChanged(params)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (isInitialized) programActivity.onWindowFocusChanged(hasFocus)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        reflections.onPostCreate.call(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        reflections.onStart.call()
    }

    override fun onRestart() {
        super.onRestart()
        reflections.onRestart.call()
    }

    override fun onResume() {
        super.onResume()
        reflections.onResume.call()
    }

    override fun onPostResume() {
        super.onPostResume()
        reflections.onPostResume.call()
    }

    override fun onPause() {
        super.onPause()
        reflections.onPause.call()
    }

    override fun onStop() {
        super.onStop()
        reflections.onStop.call()
    }

    override fun onDestroy() {
        super.onDestroy()
        reflections.onDestroy.call()
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int, options: Bundle?) {
        if (intent == null) return
        try {
            super.startActivityForResult(programManager.wrapActivityIntent(intent), requestCode, options)
        } catch (e: Throwable) {
            JCLogger.error(e)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        reflections.onActivityResult.call(requestCode, resultCode, data)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        reflections.onNewIntent.call(intent)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        reflections.onSaveInstanceState.call(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        reflections.onRestoreInstanceState.call(savedInstanceState)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        reflections.onAttachedToWindow.call()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        reflections.onDetachedFromWindow.call()
    }

    override fun onCreateView(name: String?, context: Context?, attrs: AttributeSet?): View? {
        if (isInitialized) {
            return programActivity.onCreateView(name, context, attrs)
        }
        return super.onCreateView(name, context, attrs)
    }

    override fun onCreateView(parent: View?, name: String?, context: Context?, attrs: AttributeSet?): View? {
        if (isInitialized) {
            return programActivity.onCreateView(parent, name, context, attrs)
        }
        return super.onCreateView(parent, name, context, attrs)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return super.onKeyDown(keyCode, event) || reflections.onKeyDown.call(keyCode, event) as? Boolean ?: false
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        return super.onKeyUp(keyCode, event) || reflections.onKeyUp.call(keyCode, event) as? Boolean ?: false
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        if (event == null) return false
        super.dispatchKeyEvent(event)
        return programActivity.dispatchKeyEvent(event)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        reflections.onBackPressed.call()
    }

    override fun getResources(): Resources {
        return MyProgramApp.get().resources
    }

    override fun getAssets(): AssetManager {
        return resources.assets
    }

    override fun getClassLoader(): ClassLoader {
        return MyProgramApp.get().classLoader
    }

    override fun getLayoutInflater(): LayoutInflater {
        if (isInitialized) return programActivity.layoutInflater
        return super.getLayoutInflater()
    }

    private fun Method.call(vararg params: Any?): Any? {
        return try {
            if (!isInitialized) return null
            invoke(programActivity, *params)
        } catch (e: Throwable) {
            JCLogger.error(if (e is InvocationTargetException) e.cause else e)
                    .error(Throwable())
            null
        }
    }

    companion object {
        private const val EXTRA_INTENT = "INTENT"
        private const val EXTRA_PACKAGE = "PACKAGE"
        private const val EXTRA_CLASS = "CLASS"

        fun wrapIntent(stubActivity: String, intent: Intent, component: ComponentName): Intent {
            return Intent()
                    .setComponent(ComponentName(MainAppContext.packageName, stubActivity))
                    .putExtra(EXTRA_INTENT, intent)
                    .putExtra(EXTRA_PACKAGE, component.packageName)
                    .putExtra(EXTRA_CLASS, component.className)
        }
    }

    @Suppress("unused", "PrivatePropertyName")
    class ActivityReflections(private val classLoader: ClassLoader, private val cActivity: Class<*>) {
        private val cBundle by loadClass<Bundle>()
        private val cInt = Int::class.javaPrimitiveType!!
        private val cKeyEvent by loadClass<KeyEvent>()
        private val cIntent by loadClass<Intent>()
        private val cContext by loadClass<Context>()

        private inline fun <reified T> loadClass(): Lazy<Class<*>> {
            return lazy { classLoader.loadClass(T::class.java.name) }
        }

        val attachBaseContext by method("attachBaseContext", cContext)
        val onCreate by method("onCreate", cBundle)
        val onPostCreate by method("onPostCreate", cBundle)
        val onStart by method("onStart")
        val onRestart by method("onRestart")
        val onResume by method("onResume")
        val onPostResume by method("onPostResume")
        val onPause by method("onPause")
        val onStop by method("onStop")
        val onDestroy by method("onDestroy")
        val onActivityResult by method("onActivityResult", cInt, cInt, cIntent)
        val onNewIntent by method("onNewIntent", cIntent)
        val onSaveInstanceState by method("onSaveInstanceState", cBundle)
        val onRestoreInstanceState by method("onRestoreInstanceState", cBundle)
        val onAttachedToWindow by method("onAttachedToWindow")
        val onDetachedFromWindow by method("onDetachedFromWindow")
        val onKeyDown by method("onKeyDown", cInt, cKeyEvent)
        val onKeyUp by method("onKeyUp", cInt, cKeyEvent)
        val onBackPressed by method("onBackPressed")

        private fun method(name: String, vararg paramTypes: Class<*>): Lazy<Method> {
            return lazy {
                Reflections.method(cActivity, name, *paramTypes).apply {
                    isAccessible = true
                }
            }
        }

        private fun field(name: String): Lazy<Field> {
            return lazy {
                Reflections.field(cActivity, name).apply {
                    isAccessible = true
                }
            }
        }
    }

    private class RunOnReady {
        private val actions = arrayListOf<() -> Unit>()
        private val init = Init.create()

        fun ready() {
            if (init.getElseInitialize()) return
            actions.forEach { it() }
            actions.clear()
        }

        fun post(action: () -> Unit) {
            if (init.isInitialized()) action() else actions.add(action)
        }
    }
}