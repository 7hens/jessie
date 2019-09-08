package cn.jessie.sample.plugin.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

abstract class LaunchModeActivity : LifecycleActivity() {
    private val trace by lazy {
        (intent.getStringExtra(EXTRA_TRACE) ?: "") + " >> " + javaClass.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(createContentView(this))
    }

    @SuppressLint("SetTextI18n")
    private fun createContentView(context: Context): View {
        return LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)

            TextView(context).apply {
                text = trace
            }.let { addView(it) }

            Button(context).apply {
                text = "back"
                setOnClickListener {
                    setResult(RESULT_OK, Intent().putExtra("hello", "jessie"))
                    finish()
                }
            }.let { addView(it) }

            Button(context).apply {
                text = "Standard"
                isAllCaps = false
                setOnClickListener { start<Standard>() }
            }.let { addView(it) }

            Button(context).apply {
                text = "SingleTop"
                isAllCaps = false
                setOnClickListener { start<SingleTop>() }
            }.let { addView(it) }

            Button(context).apply {
                text = "SingleTask"
                isAllCaps = false
                setOnClickListener { start<SingleTask>() }
            }.let { addView(it) }

            Button(context).apply {
                text = "SingleInstance"
                isAllCaps = false
                setOnClickListener { start<SingleInstance>() }
            }.let { addView(it) }
        }
    }

    private inline fun <reified A : Activity> start() {
        startActivity(Intent(this, A::class.java)
                .putExtra(EXTRA_TRACE, trace))
    }

    companion object {
        private const val EXTRA_TRACE = "TRACE"
    }

    class Standard : LaunchModeActivity()
    class SingleTop : LaunchModeActivity()
    class SingleTask : LaunchModeActivity()
    class SingleInstance : LaunchModeActivity()
}