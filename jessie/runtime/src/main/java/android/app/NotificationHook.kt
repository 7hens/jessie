package android.app

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import cn.jessie.runtime.app.notification.JessieBaseNotification

@Suppress("unused")
open class NotificationHook : JessieBaseNotification() {

    open class Builder : JessieBaseNotification.Builder {
        @Suppress("DEPRECATION")
        constructor(context: Context) : super(context)

        @TargetApi(Build.VERSION_CODES.O)
        constructor(context: Context, channelId: String) : super(context, channelId)
    }
}