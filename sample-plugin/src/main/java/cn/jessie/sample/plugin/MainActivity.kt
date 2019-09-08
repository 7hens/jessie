package cn.jessie.sample.plugin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import cn.jessie.sample.plugin.activity.ActivityFragment
import cn.jessie.sample.plugin.etc.EtcFragment
import cn.jessie.sample.plugin.provider.ProviderFragment
import cn.jessie.sample.plugin.service.ServiceFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val fragmentAdapter by lazy { MainFragmentAdapter(supportFragmentManager) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vPages.adapter = fragmentAdapter
        vTabs.setupWithViewPager(vPages)

//        PluginLog.debug("""
//            |host = ${Jessie.host.packageName}
//            |current = ${Jessie.current.packageName}
//            """.trimMargin())
    }

    private class MainFragmentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        private val pageTitles = arrayOf("Activity", "Service", "Provider", "Etc")

        private val fragClasses = arrayOf(
                ActivityFragment::class,
                ServiceFragment::class,
                ProviderFragment::class,
                EtcFragment::class)

        override fun getItem(position: Int): Fragment {
            return fragClasses[position].java.newInstance()
        }

        override fun getCount(): Int {
            return fragClasses.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return pageTitles[position]
        }
    }
}