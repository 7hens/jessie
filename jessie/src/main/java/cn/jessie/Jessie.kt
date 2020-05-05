package cn.jessie

import android.annotation.TargetApi
import android.app.Activity
import android.content.*
import android.os.Build
import android.os.Bundle
import cn.jessie.program.Program
import java.io.File

interface Jessie {
    val hostProgram: Program
    val currentProgram: Program
    val programs: Map<String, Program>
    val currentProgramProcessName: String
    fun getProgram(packageName: String): Program?
    fun containsProgram(packageName: String): Boolean
    fun install(apk: File): Program
    fun uninstall(program: String): Boolean
    fun startActivity(context: Context, intent: Intent)
    fun startActivityForResult(context: Activity, intent: Intent, requestCode: Int)

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    fun startActivity(context: Context, intent: Intent, options: Bundle?)

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    fun startActivityForResult(context: Activity, intent: Intent, requestCode: Int, options: Bundle?)

    fun startService(context: Context, intent: Intent): ComponentName?
    fun stopService(context: Context, intent: Intent): Boolean
    fun bindService(context: Context, intent: Intent, conn: ServiceConnection, flags: Int): Boolean
    fun unbindService(context: Context, conn: ServiceConnection)
    fun getContentResolver(context: Context): ContentResolver

    companion object : Jessie by JessieRuntime.jessie
}
