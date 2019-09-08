package cn.jessie.sample.plugin.etc

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import cn.jessie.sample.plugin.R
import cn.jessie.sample.plugin.activity.StaticFragmentActivity
import kotlinx.android.synthetic.main.fragment_main_etc.*

class EtcFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main_etc, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vShowNotification.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            }
            val intent = Intent(requireContext(), StaticFragmentActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(requireContext(), 0, intent, 0)
            val notification = getNotificationBuilder()
                    .setContentTitle("contentTitle")
                    .setContentText("contentText")
                    .setSubText("subText")
                    .setTicker("ticker")
                    .setSmallIcon(R.drawable.ic_info)
                    .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_launcher_round))
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .build()
            getNotificationManager().notify(1, notification)
        }
        vHideNotification.setOnClickListener {
            getNotificationManager().cancel(1)
        }

        vShowDialog.setOnClickListener {
            AlertDialog.Builder(requireActivity())
                    .setIcon(R.drawable.ic_launcher_round)
                    .setTitle("Jessie")
                    .setMessage("Hello, Jessie")
                    .setNegativeButton("取消") { dialog, _ -> dialog.dismiss() }
                    .setPositiveButton("确定") { dialog, _ ->
                        dialog.dismiss()
                        Toast.makeText(requireActivity(), "OK", Toast.LENGTH_SHORT).show()
                    }
                    .show()
        }

        vShowToast.setOnClickListener {
            Toast.makeText(requireActivity(), "Hello, Jessie", Toast.LENGTH_SHORT).show()
        }

        vShowFloatingView.setOnClickListener {
            whenOverlayPermitted {
                FloatingViewManager.get(requireActivity()).show()
            }
//            RxPermissions(requireActivity())
//                    .request(android.Manifest.permission.SYSTEM_ALERT_WINDOW)
//                    .doOnNext { isGranted ->
//                        if (isGranted) {
//                            FloatingViewManager.get(requireActivity()).show()
//                        } else {
//                            Toast.makeText(requireActivity(), "请先授予悬浮窗权限", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                    .subscribe()
        }
    }

    private fun whenOverlayPermitted(fn: () -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(requireContext())) {
                fn.invoke()
            } else {
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                Toast.makeText(requireContext(), "请先授予悬浮窗权限", Toast.LENGTH_SHORT).show()
                startActivity(intent)
            }
        } else {
            fn.invoke()
        }
    }


    @SuppressLint("WrongConstant")
    private fun getNotificationManager(): NotificationManager {
        return requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private fun getNotificationBuilder(): NotificationCompat.Builder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("channelId", "Primary Channel", NotificationManager.IMPORTANCE_DEFAULT);
            channel.lightColor = Color.GREEN
            channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            getNotificationManager().createNotificationChannel(channel)
            NotificationCompat.Builder(requireContext(), "channelId")
        } else {
            NotificationCompat.Builder(requireContext())
        }
    }
}