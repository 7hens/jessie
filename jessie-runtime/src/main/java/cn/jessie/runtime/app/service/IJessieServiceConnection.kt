package cn.jessie.runtime.app.service

import android.content.ComponentName
import android.os.IBinder
import cn.thens.okbinder.OkBinder

@OkBinder.Interface
interface IJessieServiceConnection {
    fun onConnect(component: ComponentName, binder: IBinder?)
}