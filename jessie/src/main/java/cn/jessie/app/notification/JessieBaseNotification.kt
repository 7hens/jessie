package cn.jessie.app.notification

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationIconHook
import android.content.Context
import android.graphics.drawable.Icon
import android.os.Build

@Suppress("unused")
open class JessieBaseNotification : Notification() {

    open class Builder : Notification.Builder {
        @Suppress("DEPRECATION")
        constructor(context: Context) : super(context)

        @TargetApi(Build.VERSION_CODES.O)
        constructor(context: Context, channelId: String) : super(context, channelId)

        override fun setSmallIcon(icon: Icon?): Notification.Builder {
            return NotificationIconHook.setSmallIcon(this, icon)
        }

        override fun setSmallIcon(icon: Int): Notification.Builder {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && icon != 0) {
                return NotificationIconHook.setSmallIcon(this, icon)
            }
            return super.setSmallIcon(0)
        }

        override fun setSmallIcon(icon: Int, level: Int): Notification.Builder {
            super.setSmallIcon(0, level)
            return setSmallIcon(icon)
        }
    }
}