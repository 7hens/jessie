package cn.jessie.stub

import android.app.Activity
import android.os.Bundle

class JessieLauncherActivity: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        finish()
    }
}