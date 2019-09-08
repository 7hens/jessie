package android.app;

import android.app.Notification;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

interface INotificationManager {
    void cancelAllNotifications(String pkg, int userId);
    void enqueueNotificationWithTag(String pkg, String opPkg, String tag, int id, in Notification notification, inout int[] idReceived, int userId);
    void cancelNotificationWithTag(String pkg, String tag, int id, int userId);
    void setNotificationsEnabledForPackage(String pkg, int uid, boolean enabled);
    boolean areNotificationsEnabledForPackage(String pkg, int uid);
    boolean areNotificationsEnabled(String pkg);
}