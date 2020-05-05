package cn.jessie.program

import android.content.ComponentName
import android.content.Intent
import cn.jessie.app.provider.JessieStubProvider
import cn.jessie.etc.JCLogger
import cn.jessie.main.JessieProgramManagerImpl
import cn.jessie.main.MainAppContext
import cn.jessie.main.ProcessDispatcher

internal class PluginProgram(
        override val packageName: String,
        override val dexInfo: DexInfo) : DexProgram() {

    override fun start() {
        val launcherActivity = packageComponents.launcherActivities
                .map { it.component }
                .filter { it.processName == packageInfo.applicationInfo.processName && it.isEnabled }
                .map { it.name }
                .getOrElse(0) { "" }
        if (launcherActivity.isNullOrEmpty()) {
            JCLogger.warn("There is not a launcher activity in program $packageName")
            return
        }
        var intent = Intent().setComponent(ComponentName(packageName, launcherActivity))
        intent = JessieProgramManagerImpl.get().wrapActivityIntent(intent)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
        MainAppContext.get().startActivity(intent)
    }

    override fun stop() {
        val contentResolver = MainAppContext.get().contentResolver
        ProcessDispatcher.queryProgramRunningProcessIndices(packageName).forEach { index ->
            try {
                // TODO 需要回收进程和 Activity
                val uri = JessieStubProvider.uri(index, JessieStubProvider.ACTION_EXIT)
                contentResolver.query(uri, null, null, null, null)?.close()
            } catch (e: Throwable) {
                JCLogger.error(e)
            }
        }
    }
}