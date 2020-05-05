package android.app

import android.annotation.TargetApi
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.os.Build
import cn.jessie.runtime.app.MyProgram
import cn.jessie.runtime.app.application.MyProgramApp
import cn.jessie.runtime.etc.Bitmaps
import cn.jessie.runtime.etc.JCLogger
import cn.jessie.runtime.etc.Reflections
import cn.jessie.runtime.main.MainAppContext

object NotificationIconHook {
    private const val defaultIconRes = 0

    @JvmStatic
    fun setSmallIcon(builder: Notification.Builder, icon: Int): Notification.Builder {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return builder.setSmallIcon(bitmapIcon(icon))
        }
        return builder.setSmallIcon(defaultIconRes)
    }

    @JvmStatic
    @TargetApi(Build.VERSION_CODES.M)
    fun setSmallIcon(builder: Notification.Builder, icon: Icon?): Notification.Builder {
        return builder.setSmallIcon(wrapIcon(icon))
    }

    @JvmStatic
    fun setSmallIcon(notification: Notification, icon: Int) {
        notification.icon = defaultIconRes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setSmallIconInternal(notification, bitmapIcon(icon))
        }
    }

    @JvmStatic
    @TargetApi(Build.VERSION_CODES.M)
    fun setSmallIcon(notification: Notification, icon: Icon?) {
        setSmallIconInternal(notification, wrapIcon(icon))
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun setSmallIconInternal(notification: Notification, icon: Icon?) {
        try {
            Reflections.set(notification, "mSmallIcon", icon)
        } catch (e: Throwable) {
            JCLogger.error(e)
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun bitmapIcon(resId: Int): Icon? {
        return try {
            JCLogger.debug("resId=$resId\npackageName=${MyProgram.packageName}")
            Icon.createWithBitmap(Bitmaps.from(getDrawable(resId)))
        } catch (e: Throwable) {
            JCLogger.error(e)
            null
        }
    }

    private fun getDrawable(resId: Int): Drawable {
        if (resId == 0) {
            return MainAppContext.get().resources.getDrawable(MainAppContext.get().applicationInfo.icon)
        }
        return MyProgram.resources.getDrawable(resId)
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun wrapIcon(icon: Icon?): Icon? {
        if (icon == null) return null
        val drawable = icon.loadDrawable(MyProgramApp.get())
        return Icon.createWithBitmap(Bitmaps.from(drawable))
    }
}