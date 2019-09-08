package cn.jessie.sample.plugin.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import cn.jessie.sample.plugin.R
import kotlinx.android.synthetic.main.fragment_main_activity.*

class ActivityFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main_activity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vStartActivity.setOnClickListener {
            startActivityForResult(Intent(requireContext(), LaunchModeActivity.Standard::class.java), 0)
        }
        vStaticFragment.setOnClickListener {
            startActivity(Intent(requireContext(), StaticFragmentActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            val extra = data.getStringExtra("hello")
            Toast.makeText(requireContext(), "onActivityResult($extra)", Toast.LENGTH_SHORT).show()
        }
    }
}