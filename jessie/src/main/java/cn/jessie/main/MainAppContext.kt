package cn.jessie.main

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import cn.jessie.etc.Files
import cn.jessie.etc.Init
import cn.jessie.etc.Reflections
import java.io.File

/**
 * @author 7hens
 */
@SuppressLint("WrongConstant", "StaticFieldLeak")
@Deprecated("jessie-runtime")
internal object MainAppContext {
    const val JESSIE_HOST_APPLICATION_SERVICE = "jessie_host_application"
    private val init = Init.create()
    private lateinit var app: Context
    private var isPluginProgram: Boolean = false
    val packageName: String by lazy { app.packageName }

    val debug: Boolean by lazy {
        try {
            require(init.isInitialized())
            Reflections.get(Class.forName("$packageName.BuildConfig"), "DEBUG") as Boolean
        } catch (e: Throwable) {
            Log.e("@JC", MainAppContext::class.java.name + "#debug", e)
            false
        }
    }

    fun initialize(context: Context) {
        if (init.getElseInitialize()) return
        app = context.applicationContext as Application
//        Logdog.debug("JessieHostApplication = " + context.getSystemService(JESSIE_HOST_APPLICATION_SERVICE))
        val hostApp = context.getSystemService(JESSIE_HOST_APPLICATION_SERVICE) as? Context
        if (hostApp != null) {
            isPluginProgram = true
            app = hostApp
            return
        }
        Processes.initialize(app)
//        Logdog.debug("MainApp.onCreate: ${Processes.current.let { it.processName + "#" + it.pid }}")
    }

    fun get(): Context = app

    fun isHost(): Boolean {
        require(init.isInitialized())
        return !isPluginProgram
    }

    fun dir(name: String): File {
        return Files.dir(get().getDir(name, Context.MODE_PRIVATE))
    }
}