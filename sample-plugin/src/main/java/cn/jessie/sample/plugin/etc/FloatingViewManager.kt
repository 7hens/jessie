package cn.jessie.sample.plugin.etc

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.ImageView
import cn.jessie.sample.plugin.PluginLog
import cn.jessie.sample.plugin.R
import java.lang.ref.WeakReference


/**
 * 信息流面板管理器。
 * 用来控制信息流的显示。
 */
@Suppress("MemberVisibilityCanBePrivate")
class FloatingViewManager private constructor(context: Context) {
    private var lastX = 0
    private var lastY = 0

    private val vFloatingView = ImageView(context).apply {
        setImageResource(R.drawable.ic_launcher_round)
    }

    private var isVisible = false

    private inline val context get() = vFloatingView.context.applicationContext

    private inline val windowManager
        @SuppressLint("WrongConstant")
        get () = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    init {
        vFloatingView.setOnClickListener { dismiss() }
        vFloatingView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastX = event.rawX.toInt()
                    lastY = event.rawY.toInt()
                }
                MotionEvent.ACTION_MOVE -> {
                    val nowX = event.rawX.toInt()
                    val nowY = event.rawY.toInt()
                    val deltaX = nowX - lastX
                    val deltaY = nowY - lastY
                    lastX = nowX
                    lastY = nowY
                    layoutParams.x = layoutParams.x + deltaX
                    layoutParams.y = layoutParams.y + deltaY
                    windowManager.updateViewLayout(vFloatingView, layoutParams)
                }
            }
            false
        }
    }

    /**
     * 显示。
     */
    @Synchronized
    fun show() {
        try {
            if (isVisible) return
            isVisible = true
            if (vFloatingView.parent != null) {
                windowManager.removeViewImmediate(vFloatingView)
            }
            windowManager.addView(vFloatingView, layoutParams)
        } catch (e: Exception) {
            PluginLog.error(e)
        }
    }

    /**
     * 隐藏。
     */
    @Synchronized
    fun dismiss() {
        if (!isVisible) return
        isVisible = false
        windowManager.removeViewImmediate(vFloatingView)
    }

    private val layoutParams by lazy {
        WindowManager.LayoutParams().apply {
            width = 200
            height = 200
            format = PixelFormat.RGBA_8888
            flags = WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH or
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
            gravity = Gravity.START or Gravity.TOP
            val displayMetrics = context.resources.displayMetrics
            x =  displayMetrics.widthPixels - width
            y = displayMetrics.heightPixels - height
            type = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> WindowManager.LayoutParams.TYPE_TOAST
                else -> WindowManager.LayoutParams.TYPE_PHONE
            }
        }
    }

    companion object {
        private var instanceRef = WeakReference<FloatingViewManager>(null)

        @JvmStatic
        fun get(context: Context): FloatingViewManager {
            var instance = instanceRef.get()
            if (instance != null) return instance
            instance = FloatingViewManager(context.applicationContext)
            instanceRef = WeakReference(instance)
            return instance
        }
    }
}