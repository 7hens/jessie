package cn.jessie.main

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.pm.ComponentInfo
import android.os.Process
import cn.jessie.Jessie
import cn.jessie.app.activity.JessieStubActivity
import cn.jessie.app.provider.ProgramResolver
import cn.jessie.etc.Init
import cn.jessie.etc.Logdog

/**
 * Jessie 中的进程分 3 种：
 * 1. 主进程（Main）
 * 2. 守护进程（Daemon）
 * 3. 插件进程（Plugin）
 *
 * 主进程和插件进程类似，但又有不同。相同的是，都通过远程 Binder（[IJessieProgramManager]） 和守护进程通信。
 * 不同的是，插件进程会负责管理插件 Application 和四大组件的生命周期。
 *
 * **1) 主进程**
 *
 * 主进程的 [MainInitProvider] 会伴随着宿主的 Application 启动，
 * 继而 start [JessieDaemonService] 来启动守护者进程。到此为止，主进程的工作基本已经完了。
 * 对于插件的安装和卸载等操作基本同插件进程无异，这些会在插件进程中提到。
 *
 * **2) 守护进程**
 *
 * 守护进程是通过主进程的 [MainInitProvider] 来 start 守护进程的  [JessieDaemonService]  从而实现唤醒的，
 * 这个操作会在主进程 Application 初始化的时候执行。也就是说，守护进程会跟着主进程一起启动。
 *
 * 守护进程主要负责插件的安装、卸载、解析等较为繁重的操作。
 * 并且它还要负责插件进程的分配、ContentProvider 的分发、以及 Intent 的查询等操作。
 * 守护进程和其他进程的操作比较频繁，跨进程通信的类目前只有一个——[IJessieProgramManager]。
 *
 * 守护进程和其他进程的通信是通过 [JessieDaemonProvider] 来进行的。
 * [JessieDaemonProvider] 会提供一个远程 Binder ([IJessieProgramManager]) 供其他进程使用。
 *
 * **3) 插件进程**
 *
 * 插件进程是由守护进程分配的，只在特定需要（启动四大组件）的时候才会启动。
 *
 * Jessie 提供了 20 个进程坑位供插件使用。在启动插件的组件之前，需要向守护进程申请，
 * 守护进程中的 [ProcessDispatcher] 会为其找到一个合适的进程坑位。
 * - 插件的 Application：会伴随着当前插件进程的启动而初始化，同时会启动相同进程的 Providers。
 * - 插件的 Activity：通过 [JessieStubActivity] 为其提供桩位，通过代理来实现。
 * - 插件的 Service：通过插件的 Provider 来实现 bindService 的 Binder 传递。
 * - 插件的 Provider：通过宿主里面的 [ProgramResolver] 来分发 URI。
 * - 插件的 Receiver：静态的 Receiver 需要和 Application 一同启动（尚未实现，建议 Receiver 只支持插件的主进程）。
 */
@SuppressLint("WrongConstant")
object Processes {
    /**
     * 获取当前进程
     *
     * > 参考：[Android系统中获取进程（和顶端包名）](https://www.jianshu.com/p/91f60bd0d1f9)
     */
    lateinit var current: ActivityManager.RunningAppProcessInfo

    lateinit var main: ActivityManager.RunningAppProcessInfo

    private val daemonProcessName by lazy { MainAppContext.packageName + ":jcdm" }

    private val init = Init.create()

    val all: List<ActivityManager.RunningAppProcessInfo>
        get() {
            val context = MainAppContext.get()
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            return activityManager.runningAppProcesses
        }

    fun initialize(context: Context) {
        if (init.getElseInitialize()) return
        val myPid = Process.myPid()
        val packageName = context.packageName
        for (process in all) {
            if (process.pid == myPid) current = process
            if (process.processName == packageName) main = process
        }
    }

    fun getJessieProcessIndex(processName: String): Int {
        try {
            val signIndex = processName.lastIndexOf(":jc")
            if (signIndex == -1) return -1
            return processName.substring(signIndex + 3).toInt()
        } catch (e: Throwable) {
            Logdog.error(e)
            return -1
        }
    }

    fun getCurrentJessieProcessIndex(): Int {
        return getJessieProcessIndex(current.processName)
    }

    fun checkDaemon() {
        if (current.processName == daemonProcessName) return
        throw RuntimeException("running on wrong process: " +
                "expected ($daemonProcessName) but found (${current.processName})")
    }

    private fun checkProgram(expectedProcessName: String) {
        val actualProcessName = Jessie.currentProgramProcessName
        if (actualProcessName != expectedProcessName) {
            throw RuntimeException("running on wrong process: " +
                    "expected ($expectedProcessName) but found ($actualProcessName)")
        }
    }

    fun checkProgram(componentInfo: ComponentInfo) {
        checkProgram(processName(componentInfo))
    }

    fun processName(componentInfo: ComponentInfo): String {
        return componentInfo.processName ?: componentInfo.packageName
    }
}