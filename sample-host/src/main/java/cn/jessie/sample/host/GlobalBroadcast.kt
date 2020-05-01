package cn.jessie.sample.host


import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import cn.jessie.etc.Init
import cn.jessie.etc.JCLogger
import io.reactivex.Observable
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Suppress("unused", "MemberVisibilityCanBePrivate")
class GlobalBroadcast private constructor(private val context: Context) {
    private val action = context.packageName + ".GLOBAL_BROADCAST"
    private val extraName = "uri"
    private val callbacks = ConcurrentHashMap<String, (String) -> Unit>()
    private val records = ConcurrentHashMap<String, String>()

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action != action) return
            resolveUri(intent.getStringExtra(extraName) ?: "")
        }

        fun resolveUri(data: String) {
            try {
                val queryIndex = data.indexOf("?")
                val command: String
                val query: String
                if (queryIndex >= 0) {
                    command = data.substring(0, queryIndex)
                    query = data.substring(queryIndex + 1)
                } else {
                    command = data
                    query = ""
                }
                callbacks[command]?.invoke(query)
            } catch (e: Throwable) {
                JCLogger.error(e)
            }
        }
    }

    init {
        context.registerReceiver(receiver, IntentFilter(action))
        receiveDefaultCommands()
    }

    fun receive(command: String): Observable<String> {
        if (init.isInitialized()) {
            val record = getCommandRecord()
            return Observable
                    .create<String> { emitter ->
                        callbacks[command] = { emitter.onNext(it) }
                        records[command] = record
                    }
                    .doFinally {
                        callbacks.remove(command)
                        records.remove(command)
                    }
        }
        return Observable.error(RuntimeException("Broadcast is uninitialized"))
    }

    fun send(uri: String) {
        val intent = Intent()
        intent.action = action
        intent.putExtra(extraName, uri)
        context.sendBroadcast(intent)
    }

    private fun getCommandRecord(): String {
        val stackTrace = Thread.currentThread().stackTrace
        var isCurrentClass = false
        for (traceElement in stackTrace) {
            val className = traceElement.className
            val fileName = traceElement.fileName
            val lineNumber = traceElement.lineNumber
            if (GlobalBroadcast::class.java.name == className) {
                isCurrentClass = true
            } else if (isCurrentClass) {
                return String.format(Locale.getDefault(), "(%s:%d)", fileName, lineNumber)
            }
        }
        return ""
    }

    private fun receiveDefaultCommands() {
        receive("")
                .doOnNext {
                    val buffer = StringBuilder()
                    for ((command, record) in records) {
                        buffer.append(command).append(" ").append(record).append("\n")
                    }
                    JCLogger.debug(buffer.toString())
                }
                .subscribe()
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var instance: GlobalBroadcast
        private val init = Init.create()

        fun initialize(context: Context) {
            if (init.getElseInitialize()) return
            instance = GlobalBroadcast(context.applicationContext)
        }

        fun get(): GlobalBroadcast {
            return instance
        }
    }
}