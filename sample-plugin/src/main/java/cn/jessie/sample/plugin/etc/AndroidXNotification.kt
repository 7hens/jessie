package cn.jessie.sample.plugin.etc

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import cn.jessie.sample.plugin.R
import cn.jessie.sample.plugin.activity.StaticFragmentActivity

class AndroidXNotification(private val context: Context) {
    fun show() {
        val intent = Intent(context, StaticFragmentActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        val notification = getNotificationBuilder()
                .setContentTitle("contentTitle")
                .setContentText("contentText")
                .setSubText("subText")
                .setTicker("ticker")
                .setSmallIcon(R.drawable.ic_info)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher_round))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()
        getNotificationManager().notify(NOTIFICATION_ID, notification)
    }

    fun dismiss() {
        getNotificationManager().cancel(NOTIFICATION_ID)
    }

    private fun getNotificationBuilder(): NotificationCompat.Builder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("channelId", "Primary Channel", NotificationManager.IMPORTANCE_DEFAULT)
            channel.lightColor = Color.GREEN
            channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            getNotificationManager().createNotificationChannel(channel)
            NotificationCompat.Builder(context, "channelId")
        } else {
            NotificationCompat.Builder(context)
        }
    }

    private fun getNotificationManager(): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    companion object {
        private const val NOTIFICATION_ID = 1
    }
}