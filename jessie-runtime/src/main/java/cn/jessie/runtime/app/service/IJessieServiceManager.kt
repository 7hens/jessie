package cn.jessie.runtime.app.service

import android.content.ComponentName
import android.content.Intent
import cn.thens.okbinder.OkBinder

@OkBinder.Interface
interface IJessieServiceManager {
    fun startService(component: ComponentName, intent: Intent): ComponentName
    fun stopService(component: ComponentName, intent: Intent): Boolean
    fun bindService(component: ComponentName, intent: Intent, conn: IJessieServiceConnection, flags: Int): Boolean
    fun unbindService(component: ComponentName, conn: IJessieServiceConnection): Boolean
}