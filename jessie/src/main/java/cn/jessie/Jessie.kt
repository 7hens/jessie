package cn.jessie

import android.app.Activity
import android.content.*
import android.os.Bundle
import android.os.Process
import cn.jessie.app.MyProgram
import cn.jessie.app.provider.ProgramResolver
import cn.jessie.app.service.ServiceExecutor
import cn.jessie.main.JessieServices
import cn.jessie.main.MainAppContext
import cn.jessie.main.RemoteProgram
import cn.jessie.program.AppProgram
import cn.jessie.program.Program
import java.io.File

/**
 * @author 7hens
 */
object Jessie {

    private val programManager get() = JessieServices.programManager

    private val programsInternal = hashMapOf<String, Program>()

    val hostProgram: Program by lazy {
        AppProgram(MainAppContext.get())
    }

    val currentProgram: Program = MyProgram

    val programs: Map<String, Program>
        get() {
            // FIXME 删除后需要刷新
            val remoteProgramPackageNames = programManager.getProgramPackageNames()
            if (programsInternal.size != remoteProgramPackageNames.size) {
                for (programPackageName in remoteProgramPackageNames) {
                    if (programsInternal.containsKey(programPackageName)) continue
                    programsInternal[programPackageName] = RemoteProgram(programPackageName)
                }
            }
            return programsInternal
        }

    fun getProgram(packageName: String): Program? {
        return programs[packageName]
    }

    fun containsProgram(packageName: String): Boolean {
        return programManager.containsProgram(packageName)
    }

    fun install(apk: File): Program {
        val packageName = programManager.install(apk.path)
        return RemoteProgram(packageName)
    }

    fun uninstall(program: String): Boolean {
        return programManager.uninstall(program)
    }

    val currentProgramProcessName: String by lazy {
        programManager.getProgramProcessNameByPid(Process.myPid())
    }

    internal fun wrapActivityIntent(intent: Intent): Intent {
        return programManager.wrapActivityIntent(intent)
    }

    fun startActivity(context: Context, intent: Intent, options: Bundle? = null) {
        context.startActivity(wrapActivityIntent(intent), options)
    }

    fun startActivityForResult(context: Activity, intent: Intent, requestCode: Int, options: Bundle? = null) {
        context.startActivityForResult(wrapActivityIntent(intent), requestCode, options)
    }

    fun startService(context: Context, intent: Intent): ComponentName? {
        return ServiceExecutor.startService(intent)
    }

    fun stopService(context: Context, intent: Intent): Boolean {
        return ServiceExecutor.stopService(intent)
    }

    fun bindService(context: Context, intent: Intent, conn: ServiceConnection, flags: Int): Boolean {
        return ServiceExecutor.bindService(intent, conn, flags)
    }

    fun unbindService(context: Context, conn: ServiceConnection) {
        ServiceExecutor.unbindService(conn)
    }

    fun getContentResolver(context: Context): ContentResolver {
        return ProgramResolver.get()
    }
}