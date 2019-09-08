package android.app

import android.annotation.TargetApi
import android.os.Build
import cn.jessie.app.application.JessieBaseApplication

/**
 * @author 7hens
 */
open class ApplicationHook : JessieBaseApplication() {

    override fun registerActivityLifecycleCallbacks(callback: Application.ActivityLifecycleCallbacks) {
        super.registerActivityLifecycleCallbacks(callback)
    }

    override fun unregisterActivityLifecycleCallbacks(callback: Application.ActivityLifecycleCallbacks) {
        super.unregisterActivityLifecycleCallbacks(callback)
    }

    interface ActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    interface OnProvideAssistDataListener : Application.OnProvideAssistDataListener
}