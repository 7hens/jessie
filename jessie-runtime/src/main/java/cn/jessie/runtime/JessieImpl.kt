package cn.jessie.runtime

import android.annotation.TargetApi
import android.app.Activity
import android.content.*
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.util.LruCache
import cn.jessie.Jessie
import cn.jessie.program.Program
import cn.jessie.runtime.app.MyProgram
import cn.jessie.runtime.app.provider.ProgramResolver
import cn.jessie.runtime.app.service.ServiceExecutor
import cn.jessie.runtime.main.JessieServices
import cn.jessie.runtime.main.MainAppContext
import cn.jessie.runtime.main.RemoteProgram
import cn.jessie.runtime.program.AppProgram
import java.io.File

/**
 * @author 7hens
 */
object JessieImpl : Jessie {

    private val programManager get() = JessieServices.programManager

    private val programCache = LruCache<String, Program>(128)

    override val hostProgram: Program by lazy {
        AppProgram(MainAppContext.get())
    }

    override val currentProgram: Program = MyProgram

    override val programs: Map<String, Program>
        get() {
            val remoteProgramPackageNames = programManager.getProgramPackageNames()
            val result = hashMapOf<String, Program>()
            for (packageName in remoteProgramPackageNames) {
                result[packageName] = programCache.get(packageName) ?: run {
                    RemoteProgram(packageName).also { programCache.put(packageName, it) }
                }
            }
            return result
        }

    override fun getProgram(packageName: String): Program? {
        return if (containsProgram(packageName)) RemoteProgram(packageName) else null
    }

    override fun containsProgram(packageName: String): Boolean {
        return programManager.containsProgram(packageName)
    }

    override fun install(apk: File): Program {
        val packageName = programManager.install(apk.path)
        return RemoteProgram(packageName)
    }

    override fun uninstall(program: String): Boolean {
        return programManager.uninstall(program)
    }

    override val currentProgramProcessName: String by lazy {
        programManager.getProgramProcessNameByPid(Process.myPid())
    }

    internal fun wrapActivityIntent(intent: Intent): Intent {
        return programManager.wrapActivityIntent(intent)
    }

    override fun startActivity(context: Context, intent: Intent) {
        context.startActivity(wrapActivityIntent(intent))
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun startActivity(context: Context, intent: Intent, options: Bundle?) {
        context.startActivity(wrapActivityIntent(intent), options)
    }

    override fun startActivityForResult(context: Activity, intent: Intent, requestCode: Int) {
        context.startActivityForResult(wrapActivityIntent(intent), requestCode)
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun startActivityForResult(context: Activity, intent: Intent, requestCode: Int, options: Bundle?) {
        context.startActivityForResult(wrapActivityIntent(intent), requestCode, options)
    }

    override fun startService(context: Context, intent: Intent): ComponentName? {
        return ServiceExecutor.startService(intent)
    }

    override fun stopService(context: Context, intent: Intent): Boolean {
        return ServiceExecutor.stopService(intent)
    }

    override fun bindService(context: Context, intent: Intent, conn: ServiceConnection, flags: Int): Boolean {
        return ServiceExecutor.bindService(intent, conn, flags)
    }

    override fun unbindService(context: Context, conn: ServiceConnection) {
        ServiceExecutor.unbindService(conn)
    }

    override fun getContentResolver(context: Context): ContentResolver {
        return ProgramResolver.get()
    }
}