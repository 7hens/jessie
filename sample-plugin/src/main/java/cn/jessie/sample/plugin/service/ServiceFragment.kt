package cn.jessie.sample.plugin.service

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cn.jessie.sample.plugin.PluginLog
import cn.jessie.sample.plugin.R
import kotlinx.android.synthetic.main.fragment_main_service.*

class ServiceFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main_service, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vStartLocalService.setOnClickListener { start(localIntent()) }
        vStopLocalService.setOnClickListener { stop(localIntent()) }
        vBindLocalService.setOnClickListener { bind(localIntent()) }
        vUnbindLocalService.setOnClickListener { unbind(localIntent()) }
        vStartRemoteService.setOnClickListener { start(removeIntent()) }
        vStopRemoteService.setOnClickListener { stop(removeIntent()) }
        vBindRemoteService.setOnClickListener { bind(removeIntent()) }
        vUnbindRemoteService.setOnClickListener { unbind(removeIntent()) }
    }

    private val connection by lazy {
        object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?) {
                PluginLog.debug("onServiceDisconnected")
            }

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                PluginLog.debug("onServiceConnected")
            }
        }
    }

    private fun start(intent: Intent) {
        requireContext().startService(intent)
    }

    private fun stop(intent: Intent) {
        requireContext().stopService(intent)
    }

    private fun bind(intent: Intent) {
        requireContext().bindService(intent, connection, 0)
    }

    private fun unbind(intent: Intent) {
        requireContext().unbindService(connection)
    }

    private fun localIntent(): Intent {
        return Intent(requireContext(), MyService.Local::class.java)
    }

    private fun removeIntent(): Intent {
        return Intent(requireContext(), MyService.Remote::class.java)
    }
}