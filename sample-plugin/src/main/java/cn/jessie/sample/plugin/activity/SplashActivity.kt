package cn.jessie.sample.plugin.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import cn.jessie.sample.plugin.MainActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class SplashActivity : AppCompatActivity() {
    override fun onResume() {
        super.onResume()
        Observable.timer(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { startActivity(Intent(this, MainActivity::class.java)) }
                .subscribe()
    }
}