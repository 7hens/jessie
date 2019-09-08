package cn.jessie.sample.plugin.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.jessie.sample.plugin.R
import kotlinx.android.synthetic.main.activity_static_fragment.*

class StaticFragmentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_static_fragment)
        vGoBack.setOnClickListener { finish() }
    }
}