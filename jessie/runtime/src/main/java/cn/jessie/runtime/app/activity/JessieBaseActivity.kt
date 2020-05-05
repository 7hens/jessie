package cn.jessie.runtime.app.activity

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import cn.jessie.runtime.app.application.MyProgramApp
import cn.jessie.runtime.main.JessieServices

/**
 * @author 7hens
 */
@SuppressLint("MissingSuperCall", "WrongConstant")
abstract class JessieBaseActivity : Activity() {
    private lateinit var stubActivity: Activity
    private val programManager get() = JessieServices.programManager

    fun setStubActivity(hostActivity: Activity) {
        this.stubActivity = hostActivity
    }

    private val isStubInitialized get() = ::stubActivity.isInitialized

    override fun onCreate(savedInstanceState: Bundle?) {
//        Logdog.warn("$javaClass\nisStubInitialized = $isStubInitialized")
        if (isStubInitialized) return
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        if (isStubInitialized) return
        super.onStart()
    }

    override fun onResume() {
        if (isStubInitialized) return
        super.onResume()
    }

    override fun onPause() {
        if (isStubInitialized) return
        super.onPause()
    }

    override fun onStop() {
        if (isStubInitialized) return
        super.onStop()
    }

    override fun onDestroy() {
        if (isStubInitialized) return
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        if (isStubInitialized) return
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        if (isStubInitialized) return
        super.onBackPressed()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (isStubInitialized) return false
        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (isStubInitialized) return false
        return super.onKeyUp(keyCode, event)
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        if (isStubInitialized) return false
        return super.dispatchKeyEvent(event)
    }

    override fun getPackageName(): String {
        return MyProgramApp.get().packageName
    }

    override fun getClassLoader(): ClassLoader {
        return MyProgramApp.get().classLoader
    }

    override fun getLayoutInflater(): LayoutInflater {
        if (isStubInitialized) {
            return layoutInflaterInternal
        }
        return super.getLayoutInflater()
    }

    private val layoutInflaterInternal: LayoutInflater by lazy {
        //        super.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val serviceName = Context.LAYOUT_INFLATER_SERVICE
        val inflater = MyProgramApp.get().getSystemService(serviceName) as LayoutInflater
        inflater.cloneInContext(this)
    }

    override fun getMenuInflater(): MenuInflater {
        if (isStubInitialized) {
            return stubActivity.menuInflater
        }
        return super.getMenuInflater()
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        if (intent == null) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivityForResult(intent, requestCode, null)
            return
        }
        if (isStubInitialized) {
            stubActivity.startActivityForResult(intent, requestCode)
        } else {
            val wrapIntent = programManager.wrapActivityIntent(intent)
            super.startActivityForResult(wrapIntent, requestCode)
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun startActivityForResult(intent: Intent?, requestCode: Int, options: Bundle?) {
        if (intent == null) return
//        Logdog.warn("isStubInitialized = $isStubInitialized\n$intent")
        if (isStubInitialized) {
            stubActivity.startActivityForResult(intent, requestCode, options)
        } else {
            val wrapIntent = programManager.wrapActivityIntent(intent)
            super.startActivityForResult(wrapIntent, requestCode, options)
        }
    }

    override fun getCurrentFocus(): View? {
        return if (isStubInitialized) {
            stubActivity.currentFocus
        } else {
            super.getCurrentFocus()
        }
    }

    override fun isFinishing(): Boolean {
        return if (isStubInitialized) {
            stubActivity.isFinishing
        } else {
            super.isFinishing()
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun isDestroyed(): Boolean {
        return if (isStubInitialized) {
            stubActivity.isDestroyed
        } else {
            super.isDestroyed()
        }
    }

    override fun isChangingConfigurations(): Boolean {
        return if (isStubInitialized) {
            stubActivity.isChangingConfigurations
        } else {
            super.isChangingConfigurations()
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun shouldShowRequestPermissionRationale(permission: String): Boolean {
        return if (isStubInitialized) {
            stubActivity.shouldShowRequestPermissionRationale(permission)
        } else {
            super.shouldShowRequestPermissionRationale(permission)
        }
    }
}