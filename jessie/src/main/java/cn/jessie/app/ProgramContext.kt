package cn.jessie.app

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.PendingIntent
import android.app.Service
import android.content.*
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.os.UserHandle
import android.view.LayoutInflater
import cn.jessie.app.application.MyProgramApp
import cn.jessie.app.provider.ProgramResolver
import cn.jessie.app.service.ServiceExecutor
import cn.jessie.etc.Files
import cn.jessie.etc.Reflections
import cn.jessie.main.JessieServices
import cn.jessie.main.MainAppContext
import java.io.File
import kotlin.reflect.KClass

@SuppressLint("WrongConstant")
@Suppress("unused", "UNUSED_PARAMETER", "MemberVisibilityCanBePrivate")
internal class ProgramContext(base: Context) : ContextWrapper(base) {
    private val program = MyProgram
    private val programManager get() = JessieServices.programManager

    private val packageConflictedClasses = listOf(
            PendingIntent::class.java.name,
            Service::class.java.name)

    override fun getPackageName(): String {
        for (traceElement in Thread.currentThread().stackTrace) {
            if (traceElement.className in packageConflictedClasses) {
                return MainAppContext.packageName
            }
        }
        return program.packageName
    }

    override fun createPackageContext(packageName: String?, flags: Int): Context {
        if (packageName == program.packageName) {
            return super.createPackageContext(MainAppContext.packageName, flags)
        }
        return super.createPackageContext(packageName, flags)
    }

    private fun isCalledBy(vararg classes: KClass<*>): Boolean {
        val classNames = classes.map { it.java.name }
        for (traceElement in Thread.currentThread().stackTrace) {
            if (traceElement.className in classNames) {
                return true
            }
        }
        return false
    }

    override fun getClassLoader(): ClassLoader {
        return program.classLoader
    }

    override fun getApplicationContext(): Context {
        return MyProgramApp.get()
    }

    override fun getApplicationInfo(): ApplicationInfo {
        return program.packageInfo.applicationInfo
    }

    override fun getResources(): Resources {
        return program.resources
    }

    override fun getAssets(): AssetManager {
        return program.resources.assets
    }

    override fun getContentResolver(): ContentResolver {
        return ProgramResolver.get()
    }

    private val systemLayoutInflater by lazy {
        val layoutInflater = super.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        ProgramLayoutInflater(layoutInflater, this).cloneInContext(this)
//        layoutInflater.cloneInContext(this).apply {
//            try {
//                val constructorMap = Reflections.get(this, "sConstructorMap") as HashMap<*, *>
//                constructorMap.clear()
//            } catch (e: Throwable) {
//                Logdog.error(e)
//            }
//        }
    }

    override fun getSystemService(name: String): Any? {
        return when (name) {
            MainAppContext.JESSIE_HOST_APPLICATION_SERVICE -> MainAppContext.get()
            Context.LAYOUT_INFLATER_SERVICE -> systemLayoutInflater
            else -> super.getSystemService(name)
        }
    }

    override fun getPackageManager(): PackageManager {
        return ProgramPackageManager.get()
    }

    private val rootDir by lazy {
        File(program.packageInfo.applicationInfo.dataDir)
    }

    override fun getDataDir(): File {
        return rootDir
    }

    fun getSharedPrefsFile(name: String): File {
        return getSharedPreferencesPath(name)
    }

    fun getSharedPreferencesPath(name: String): File {
        val dir = Files.dir(File(rootDir, "shared_prefs"))
        return File(dir, "$name.xml")
    }

    override fun getSharedPreferences(name: String?, mode: Int): SharedPreferences {
        return super.getSharedPreferences(program.packageName + "-" + name, mode)
    }

    override fun getCacheDir(): File {
        return Files.dir(File(rootDir, "cache"))
    }

    override fun getFilesDir(): File {
        return Files.dir(File(rootDir, "files"))
    }

    override fun getDatabasePath(name: String?): File {
        // FIXME 注释里的代码有问题，返回 is a directory
        return super.getDatabasePath("$packageName-$name")
//        return File(Files.dir(File(rootDir, "databases")), "$name.db").apply { createNewFile() }
    }

    override fun getDir(name: String?, mode: Int): File {
        return Files.dir(File(rootDir, "app_$name"))
    }

    private val externalRootDir by lazy {
        // FIXME 需要检查 SD 卡是否存在
        Files.dir(MainAppContext.get().getExternalFilesDir(program.packageName)!!)
    }

    override fun getExternalCacheDir(): File? {
        return Files.dir(File(externalRootDir, "cache"))
    }

    override fun getExternalFilesDir(type: String?): File? {
        return Files.dir(File(externalRootDir, "files" + File.separator + type))
    }

    override fun getPackageCodePath(): String {
        return program.packageInfo.applicationInfo.sourceDir
    }

    override fun getPackageResourcePath(): String {
        return packageCodePath
    }

    override fun startActivity(intent: Intent?) {
        startActivity(intent, null)
    }

    override fun startActivity(intent: Intent?, options: Bundle?) {
        requireNotNull(intent)
//        Logdog.warn(intent)
        val wrapIntent = programManager.wrapActivityIntent(intent)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        super.startActivity(wrapIntent, options)
    }

    override fun startService(service: Intent): ComponentName? {
        return ServiceExecutor.startService(service)
    }

    override fun stopService(name: Intent?): Boolean {
        return ServiceExecutor.stopService(name!!)
    }

    override fun bindService(service: Intent?, conn: ServiceConnection, flags: Int): Boolean {
        return ServiceExecutor.bindService(service!!, conn, flags)
    }

    override fun unbindService(conn: ServiceConnection) {
        ServiceExecutor.unbindService(conn)
    }

    fun createPackageContext(packageName: String, flags: Int, user: UserHandle): Context {
        return MainAppContext.get()
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun getUser(): UserHandle {
        return Reflections.invoke(baseContext, "getUser") as UserHandle
    }
}