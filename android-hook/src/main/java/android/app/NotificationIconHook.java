package android.app;

import android.annotation.TargetApi;
import android.graphics.drawable.Icon;
import android.os.Build;

public final class NotificationIconHook {
    @TargetApi(Build.VERSION_CODES.M)
    public static Notification.Builder setSmallIcon(Notification.Builder builder, Icon icon) {
        return builder.setSmallIcon(icon);
    }

    public static Notification.Builder setSmallIcon(Notification.Builder builder, int icon) {
        return builder.setSmallIcon(icon);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void setSmallIcon(Notification notification, Icon icon) {
        notification.icon = 0;
    }

    public static void setSmallIcon(Notification notification, int icon) {
        notification.icon = 0;
        notification.icon = icon;
    }
}
