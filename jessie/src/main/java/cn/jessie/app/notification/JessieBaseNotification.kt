package cn.jessie.app.notification

import android.annotation.TargetApi
import android.app.Notification
import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.os.Build
import cn.jessie.app.MyProgram
import cn.jessie.etc.Bitmaps
import cn.jessie.etc.JCLogger
import cn.jessie.main.MainAppContext

@Suppress("unused")
open class JessieBaseNotification : Notification() {

    open class Builder : Notification.Builder {
        @Suppress("DEPRECATION")
        constructor(context: Context) : super(context)

        @TargetApi(Build.VERSION_CODES.O)
        constructor(context: Context, channelId: String) : super(context, channelId)

        override fun setSmallIcon(icon: Icon?): Notification.Builder {
            return super.setSmallIcon(wrapIcon(icon))
        }

        override fun setSmallIcon(icon: Int): Notification.Builder {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && icon != 0) {
                return super.setSmallIcon(bitmapIcon(icon))
            }
            return super.setSmallIcon(0)
        }

        override fun setSmallIcon(icon: Int, level: Int): Notification.Builder {
            super.setSmallIcon(0, level)
            return setSmallIcon(icon)
        }

        private fun wrapIcon(icon: Icon?): Icon? {
            if (icon == null) return null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                if (icon.type == Icon.TYPE_RESOURCE && icon.resId != 0) {
                    return bitmapIcon(icon.resId)
                }
            }
            return icon
        }
    }

    companion object {
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
    }
}