package cn.jessie.app.activity

import android.content.ComponentName
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.util.SparseArray
import cn.jessie.etc.JCLogger
import cn.jessie.etc.NumberPool
import cn.jessie.main.JessieProgramManagerImpl
import cn.jessie.main.JessieStubComponents
import cn.jessie.main.ProcessDispatcher
import cn.jessie.main.Processes

internal object ActivityStubs {
    private val shouldMatchesLaunchMode = "true".toBoolean()

    private val launchModeCounts = JessieStubComponents.ACTIVITY_LAUNCH_MODE_COUNTS
    private val stubPools = SparseArray<NumberPool>()
    private val runningStubs = hashMapOf<String, Int>()

    @Suppress("DEPRECATION")
    fun wrapIntent(intent: Intent): Intent {
        Processes.checkDaemon()
        val activities = JessieProgramManagerImpl.get().queryIntentActivities(intent, 0)
        if (activities.isEmpty()) return Intent()
        val activityInfo = activities.first().activityInfo
        val packageName = activityInfo.packageName
        val activityName = activityInfo.name
        if (!JessieProgramManagerImpl.get().containsProgram(packageName)) return intent
        val component = ComponentName(packageName, activityName)
        intent.component = component
        val processName = Processes.processName(activityInfo)
        val processIndex = ProcessDispatcher.getProcessIndex(processName)
        val isApplicationNotRunning = processIndex == -1
        val stubActivityName = nameOf(activityInfo)
        JCLogger.debug("component = $component\n" +
                "stubActivityName = $stubActivityName\n" +
                "isApplicationRunning = ${!isApplicationNotRunning}")
        val wrapIntent = JessieStubActivity.wrapIntent(stubActivityName, intent, component)
        if (isApplicationNotRunning) {
            wrapIntent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
                    .addFlags(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT
                    } else {
                        Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET
                    })
        }
        if (!shouldMatchesLaunchMode) {
            when (activityInfo.launchMode) {
                ActivityInfo.LAUNCH_SINGLE_TASK -> {
                    wrapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }
                ActivityInfo.LAUNCH_SINGLE_TOP -> {
                    wrapIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                }
            }
        }
        return wrapIntent
    }

    private fun nameOf(activityInfo: ActivityInfo): String {
        Processes.checkDaemon()
        val activityKey = keyOf(activityInfo)
        val stubId = runningStubs[activityKey]
        // FIXME 需要统计 activity 的使用次数，如果 activity 已经使用完毕，应及时释放坑位
        if (stubId != null) {
            return ID.activityName(stubId)
        }
        val processName = Processes.processName(activityInfo)
        val processIndex = ProcessDispatcher.requireProcessIndex(processName)
        val launchMode = getWrappedLaunchMode(activityInfo.launchMode)
        val key = ID.of(processIndex, launchMode, 0)
        val pool = stubPools.get(key, NumberPool(launchModeCounts[launchMode]))
        stubPools.put(key, pool)
        var number = pool.get()
        if (number == -1) {
            number = pool.force()
            forceToStopActivity(activityClassNameOf(processIndex, launchMode, number))
            runningStubs.remove(activityKey)
        }
        JCLogger.debug("process=$processIndex, launchMode=$launchMode, number=$number")
        runningStubs[activityKey] = ID.of(processIndex, launchMode, number)
        return activityClassNameOf(processIndex, launchMode, number)
    }

    private fun getWrappedLaunchMode(launchMode: Int): Int {
        if (shouldMatchesLaunchMode) return launchMode
        return if (launchMode == ActivityInfo.LAUNCH_SINGLE_INSTANCE) {
            ActivityInfo.LAUNCH_SINGLE_INSTANCE
        } else {
            ActivityInfo.LAUNCH_SINGLE_TOP
        }
    }

    private fun keyOf(activityInfo: ActivityInfo): String {
        return activityInfo.packageName + "/" + activityInfo.name
    }

    fun activityClassNameOf(processIndex: Int, launchMode: Int, number: Int): String {
        return JessieStubComponents.getActivityClassName(processIndex, launchMode, number)
    }

    /**
     * 强制停止 Activity
     */
    private fun forceToStopActivity(activityName: String) {
        // TODO 强制停止 Activity
    }

    /**
     * ActivityStubId
     */
    object ID {
        fun of(processIndex: Int, launchMode: Int, number: Int): Int {
            return (processIndex shl 10) + (launchMode shl 8) + number
        }

        fun processIndex(id: Int): Int {
            return (id shr 10) % (1 shl 8)
        }

        fun launchMode(id: Int): Int {
            return (id shr 8) % (1 shl 2)
        }

        fun number(id: Int): Int {
            return id % (1 shl 8)
        }

        fun activityName(id: Int): String {
            return activityClassNameOf(processIndex(id), launchMode(id), number(id))
        }
    }
}