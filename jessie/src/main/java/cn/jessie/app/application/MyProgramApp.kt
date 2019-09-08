package cn.jessie.app.application

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import cn.jessie.Jessie
import cn.jessie.app.MyProgram
import cn.jessie.app.ProgramContext
import cn.jessie.app.provider.MyProgramProviders
import cn.jessie.etc.Init
import cn.jessie.etc.Logdog
import cn.jessie.etc.Reflections
import cn.jessie.main.Processes
import java.lang.reflect.Modifier

internal object MyProgramApp {
    private val init = Init.create()
    private lateinit var app: Application

    fun get() = app

    @SuppressLint("WrongConstant")
    fun initialize(mainApp: Context) {
        if (init.getElseInitialize()) return
        try {
            val classLoader = MyProgram.classLoader
            val packageInfo = MyProgram.packageInfo
            val applicationInfo = packageInfo.applicationInfo
            val currentProcessName = Jessie.currentProgramProcessName
            val applicationName = applicationInfo.className ?: Application::class.java.name
            val cApplication = classLoader.loadClass(applicationName)
            val cContext = classLoader.loadClass(Context::class.java.name)

            // 1) 新建 Application 的实例
            app = cApplication.newInstance() as Application
            for (field in Application::class.java.declaredFields) {
                if (Modifier.isStatic(field.modifiers)) continue
                field.isAccessible = true
                field.set(app, field.get(mainApp))
            }

            // 2) 调用 Application.attachBaseContext
            val mAttachBaseContext = Reflections.method(cApplication, "attachBaseContext", cContext)
            mAttachBaseContext.isAccessible = true
            mAttachBaseContext.invoke(app, ProgramContext(mainApp))

            // 3) 启动同进程的 ContentProvider
            MyProgram.packageInfo.providers.asSequence()
                    .filter { currentProcessName == Processes.processName(it) }
                    .forEach { providerInfo ->
                        try {
                            MyProgramProviders.create(providerInfo)
                        } catch (e: Throwable) {
                            Logdog.error(e)
                        }
                    }

            // 4) 执行 Application.onCreate
            app.onCreate()
        } catch (e: Throwable) {
            Logdog.error(e)
        }
    }

}