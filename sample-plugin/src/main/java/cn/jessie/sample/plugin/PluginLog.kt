package cn.jessie.sample.plugin

/**
 * @author 7hens
 */

import android.util.Log
import android.util.LruCache
import java.io.PrintWriter
import java.io.StringWriter
import java.util.*

interface LogIgnored

@Suppress("SpellCheckingInspection", "unused")
interface PluginLog : LogIgnored {
    fun isLoggable(priority: Int, tag: String): Boolean
    fun tag(tag: String): PluginLog
    fun log(priority: Int, tag: String, fn: () -> String): PluginLog
    fun debug(any: Any?): PluginLog
    fun warn(any: Any?): PluginLog
    fun error(any: Any?): PluginLog
    fun wtf(sure: Boolean, any: Any?): PluginLog
    fun time(name: String): PluginLog
    fun count(name: String): PluginLog
    fun memory(): PluginLog
    fun trace(): PluginLog

    companion object : PluginLog by Logger() {
        private var logcatTagPrefix = 0
        private const val LRU_CACHE_MAX_SIZE = 256
        private val timers = LruCache<String, Long>(LRU_CACHE_MAX_SIZE)
        private val counters = LruCache<String, Long>(LRU_CACHE_MAX_SIZE)

        override fun isLoggable(priority: Int, tag: String): Boolean {
            return true
        }
    }

    private open class Logger : PluginLog {
        protected open val tag = "@JC_PLUGIN"

        override fun tag(tag: String): PluginLog {
            val self = this
            return object : Logger() {
                override val tag: String = tag

                override fun isLoggable(priority: Int, tag: String): Boolean {
                    return self.isLoggable(priority, tag)
                }

                override fun log(priority: Int, tag: String, fn: () -> String): PluginLog {
                    return self.log(priority, tag, fn)
                }
            }
        }

        override fun isLoggable(priority: Int, tag: String) = true

        @Synchronized
        override fun log(priority: Int, tag: String, fn: () -> String): PluginLog {
            if (!isLoggable(priority, tag)) return this
            val style = getStyle(priority)
            println(priority, style.top)
            run {
                val methodCount = 1
                val methodOffset = 0
                val trace = Thread.currentThread().stackTrace
                val stackOffset = getStackOffset(trace) + methodOffset
                val headerLineCount = Math.min(methodCount, trace.size - 1 - stackOffset)
                var offset = ""
                for (i in 0 until headerLineCount) {
                    val stackIndex = i + stackOffset
                    println(priority, style.middle + offset + trace[stackIndex].prettyInfo())
                    offset += "  "
                }

                if (headerLineCount > 0) {
                    println(priority, style.divider)
                }
            }
            run {
                val message = fn()
                val chunkSize = 4 * 1024
                val bytes = message.toByteArray()
                val length = bytes.size
                val pageSize = (length - 1) / chunkSize + 1
                for (i in 0 until pageSize) {
                    val offset = i * chunkSize
                    val count = Math.min(length - offset, chunkSize)
                    val chunk = String(bytes, offset, count, Charsets.UTF_8)
                    val lines = chunk.split('\n').dropLastWhile { it.isEmpty() }.toTypedArray()
                    for (line in lines) {
                        println(priority, style.middle + line)
                    }
                }
            }
            println(priority, style.bottom)
            return this
        }

        private fun println(priority: Int, text: String) {
            logcatTagPrefix = (logcatTagPrefix + 1) % 10
            Log.println(priority, "$logcatTagPrefix.$tag", text)
        }

        override fun debug(any: Any?): PluginLog {
            return log(Log.DEBUG, tag) { stringify(any) }
        }

        override fun warn(any: Any?): PluginLog {
            return log(Log.WARN, tag) { stringify(any) }
        }

        override fun error(any: Any?): PluginLog {
            return log(Log.ERROR, tag) { stringify(any) }
        }

        override fun wtf(sure: Boolean, any: Any?): PluginLog {
            if (sure) return this
            return log(Log.ASSERT, tag) { stringify(any) }
        }

        override fun time(name: String): PluginLog {
            return log(Log.DEBUG, tag) {
                val currentTime = System.currentTimeMillis()
                val lastTime = timers[name] ?: currentTime
                timers.put(name, currentTime)
                "time($name): ${currentTime - lastTime}ms"
            }
        }

        override fun count(name: String): PluginLog {
            return log(Log.DEBUG, tag) {
                val lastCount = counters[name] ?: 0L
                counters.put(name, lastCount + 1)
                "count($name): ${lastCount + 1}"
            }
        }

        override fun memory(): PluginLog {
            return log(Log.DEBUG, tag) {
                val runtime = Runtime.getRuntime()
                """
                    total memory: ${runtime.totalMemory()}
                    free memory: ${runtime.freeMemory()}
                    max memory: ${runtime.maxMemory()}
                """.trimIndent()
            }
        }

        override fun trace(): PluginLog {
            return log(Log.DEBUG, tag) {
                val trace = Thread.currentThread().stackTrace
                val buffer = StringBuilder()
                var offset = ""
                for (e in trace) {
                    val className = e.className.substring(e.className.lastIndexOf(".") + 1)
                    if (offset.isNotEmpty()) buffer.append("\n")
                    buffer.append(offset).append(className).append(".").append(e.methodName)
                            .append(" (").append(e.fileName).append(":").append(e.lineNumber).append(")")
                    offset += "  "
                }
                buffer.toString()
            }
        }

        private fun StackTraceElement.prettyInfo(): String {
            val className = className.substring(className.lastIndexOf(".") + 1)
            val threadName = Thread.currentThread().name
            return "$className.$methodName ($fileName:$lineNumber) on Thread: $threadName"
        }

        private fun getStackOffset(trace: Array<StackTraceElement>): Int {
            var isInOffsetClass = false
            for (i in 0 until trace.size) {
                val traceElement = trace[i]
                val cls = Class.forName(traceElement.className)
                if (LogIgnored::class.java.isAssignableFrom(cls)) {
                    isInOffsetClass = true
                } else if (isInOffsetClass) {
                    return i
                }
            }
            return 0
        }

        private fun getStyle(priority: Int): PrettyStyle {
            return if (priority > Log.DEBUG) PrettyStyle.DOUBLE else PrettyStyle.SINGLE
        }

        private fun stringify(any: Any?): String {
            return when (any) {
                null -> "null"
                is String -> any
                !javaClass.isArray -> any.toString()
                is BooleanArray -> Arrays.toString(any)
                is ByteArray -> Arrays.toString(any)
                is CharArray -> Arrays.toString(any)
                is ShortArray -> Arrays.toString(any)
                is IntArray -> Arrays.toString(any)
                is LongArray -> Arrays.toString(any)
                is FloatArray -> Arrays.toString(any)
                is DoubleArray -> Arrays.toString(any)
                is Array<*> -> Arrays.deepToString(any)
                is Throwable -> getStackTraceString(any)
                else -> any.toString()
            }
        }

        private fun getStackTraceString(throwable: Throwable): String {
            val stringWriter = StringWriter()
            val printWriter = PrintWriter(stringWriter)
            throwable.printStackTrace(printWriter)
            printWriter.flush()
            return stringWriter.toString()
        }

    }

    private data class PrettyStyle(val top: String, val divider: String, val middle: String, val bottom: String) {
        companion object {
            private const val EMPTY = ""
            private const val SINGLE_PART = "────────────────────────────"
            private const val DASHED_PART = "┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄"
            private const val DOUBLE_PART = "════════════════════════════"
            private const val SINGLE_LINE = SINGLE_PART + SINGLE_PART + SINGLE_PART + SINGLE_PART
            private const val DASHED_LINE = DASHED_PART + DASHED_PART + DASHED_PART + DASHED_PART
            private const val DOUBLE_LINE = DOUBLE_PART + DOUBLE_PART + DOUBLE_PART + DOUBLE_PART

            private const val SINGLE_TOP = "┌$SINGLE_LINE"
            private const val SINGLE_DIV = "├$DASHED_LINE"
            private const val SINGLE_MID = "│ "
            private const val SINGLE_BOT = "└$SINGLE_LINE"

            private const val DOUBLE_TOP = "╔$DOUBLE_LINE"
            private const val DOUBLE_DIV = "╟$DASHED_LINE"
            private const val DOUBLE_MID = "║ "
            private const val DOUBLE_BOT = "╚$DOUBLE_LINE"

            val SINGLE = PrettyStyle(SINGLE_TOP, SINGLE_DIV, SINGLE_MID, SINGLE_BOT)
            val DOUBLE = PrettyStyle(DOUBLE_TOP, DOUBLE_DIV, DOUBLE_MID, DOUBLE_BOT)
            val NONE = PrettyStyle(EMPTY, EMPTY, EMPTY, EMPTY)
        }
    }
}