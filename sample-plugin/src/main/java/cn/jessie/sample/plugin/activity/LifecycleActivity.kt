package cn.jessie.sample.plugin.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.jessie.sample.plugin.PluginLog

abstract class LifecycleActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PluginLog.debug(javaClass.simpleName + ".onCreate()")
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        PluginLog.debug(javaClass.simpleName + ".onPostCreate()")
    }

    override fun onStart() {
        super.onStart()
        PluginLog.debug(javaClass.simpleName + ".onStart()")
    }

    override fun onResume() {
        super.onResume()
        PluginLog.debug(javaClass.simpleName + ".onResume()")
    }

    override fun onPostResume() {
        super.onPostResume()
        PluginLog.debug(javaClass.simpleName + ".onPostResume()")
    }

    override fun onPause() {
        super.onPause()
        PluginLog.debug(javaClass.simpleName + ".onPause()")
    }

    override fun onStop() {
        super.onStop()
        PluginLog.debug(javaClass.simpleName + ".onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        PluginLog.debug(javaClass.simpleName + ".onDestroy()")
    }

    override fun onRestart() {
        super.onRestart()
        PluginLog.debug(javaClass.simpleName + ".onRestart()")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        PluginLog.debug(javaClass.simpleName + ".onSaveInstanceState()")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        PluginLog.debug(javaClass.simpleName + ".onRestoreInstanceState()")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        PluginLog.debug(javaClass.simpleName + ".onNewIntent()")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        PluginLog.debug(javaClass.simpleName + ".onBackPressed()")
    }
}