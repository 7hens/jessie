package cn.jessie.main

import cn.jessie.etc.JCLogger
import cn.jessie.etc.NumberPool
import java.util.concurrent.ConcurrentHashMap

/**
 * 插进进程分发器。
 */
internal object ProcessDispatcher {
    private const val MAX_SIZE = JessieStubComponents.PROCESS_COUNT
    private val runningProcessMap = ConcurrentHashMap<String, Int>()

    /**
     * 获取插件进程对应的索引。
     *
     * 如果对应的插件进程没有在运行，则返回 -1。
     *
     * @param processName 插件进程名，如：com.sample.app:h5
     * @return 宿主中对应进程坑位的索引，取值 -1~99，-1 表示该进程没有运行
     */
    fun getProcessIndex(processName: String): Int {
        Processes.checkDaemon()
        return runningProcessMap[processName] ?: -1
    }

    fun requireProcessIndex(processName: String): Int {
        val index = getProcessIndex(processName)
        if (index >= 0) return index
        val newIndex = nextProcessIndex()
        runningProcessMap[processName] = newIndex
        return newIndex
    }

    fun getProcessName(index: Int): String {
        return "${MainAppContext.packageName}:jc$index"
    }

    fun queryProgramRunningProcesses(packageName: String): List<String> {
        Processes.checkDaemon()
        return runningProcessMap.keys.filter { it == packageName || it.startsWith("$packageName:") }
    }

    fun queryProgramRunningProcessIndices(packageName: String): List<Int> {
        Processes.checkDaemon()
        return runningProcessMap
                .filter { it.key == packageName || it.key.startsWith("$packageName:") }
                .map { it.value }
    }

    fun getProgramProcessNameByPid(pid: Int): String {
        Processes.checkDaemon()
        return try {
            val process = Processes.all.first { it.pid == pid }
            val index = Processes.getJessieProcessIndex(process.processName)
            JCLogger.warn(process.processName + ", index = $index\n$runningProcessMap")
            runningProcessMap.entries.first { it.value == index }.key
        } catch (e: Throwable) {
            JCLogger.error(e)
            ""
        }
    }

    private val processPool = NumberPool(MAX_SIZE)

    private fun nextProcessIndex(): Int {
        var index = processPool.get()
        if (index == -1) {
            // FIXME 当前进程正在被使用，需要强杀
            index = processPool.force()
        }
        return index
    }
}