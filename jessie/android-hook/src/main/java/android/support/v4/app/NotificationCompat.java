package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;

@SuppressWarnings({"unused", "deprecation", "NullableProblems"})
public class NotificationCompat extends androidx.core.app.NotificationCompat {

    public static class Builder extends androidx.core.app.NotificationCompat.Builder {
        public Builder(Context context, String channelId) {
            super(context, channelId);
        }

        public Builder(Context context) {
            super(context);
        }

        public Builder setWhen(long when) {
            super.setWhen(when);
            return this;
        }

        public Builder setShowWhen(boolean show) {
            super.setShowWhen(show);
            return this;
        }

        public Builder setUsesChronometer(boolean b) {
            super.setUsesChronometer(b);
            return this;
        }

        public Builder setSmallIcon(int icon) {
            super.setSmallIcon(icon);
            return this;
        }

        public Builder setSmallIcon(int icon, int level) {
            super.setSmallIcon(icon, level);
            return this;
        }

        public Builder setContentTitle(CharSequence title) {
            super.setContentTitle(title);
            return this;
        }

        public Builder setContentText(CharSequence text) {
            super.setContentText(text);
            return this;
        }

        public Builder setSubText(CharSequence text) {
            super.setSubText(text);
            return this;
        }

        public Builder setRemoteInputHistory(CharSequence[] text) {
            super.setRemoteInputHistory(text);
            return this;
        }

        public Builder setNumber(int number) {
            super.setNumber(number);
            return this;
        }

        public Builder setContentInfo(CharSequence info) {
            super.setContentInfo(info);
            return this;
        }

        public Builder setProgress(int max, int progress, boolean indeterminate) {
            super.setProgress(max, progress, indeterminate);
            return this;
        }

        public Builder setContent(RemoteViews views) {
            super.setContent(views);
            return this;
        }

        public Builder setContentIntent(PendingIntent intent) {
            super.setContentIntent(intent);
            return this;
        }

        public Builder setDeleteIntent(PendingIntent intent) {
            super.setDeleteIntent(intent);
            return this;
        }

        public Builder setFullScreenIntent(PendingIntent intent, boolean highPriority) {
            super.setFullScreenIntent(intent, highPriority);
            return this;
        }

        public Builder setTicker(CharSequence tickerText) {
            super.setTicker(tickerText);
            return this;
        }

        public Builder setTicker(CharSequence tickerText, RemoteViews views) {
            super.setTicker(tickerText, views);
            return this;
        }

        public Builder setLargeIcon(Bitmap icon) {
            super.setLargeIcon(icon);
            return this;
        }

        public Builder setSound(Uri sound) {
            super.setSound(sound);
            return this;
        }

        public Builder setSound(Uri sound, @StreamType int streamType) {
            super.setSound(sound, streamType);
            return this;
        }

        public Builder setVibrate(long[] pattern) {
            super.setVibrate(pattern);
            return this;
        }

        public Builder setLights(int argb, int onMs, int offMs) {
            super.setLights(argb, onMs, offMs);
            return this;
        }

        public Builder setOngoing(boolean ongoing) {
            super.setOngoing(ongoing);
            return this;
        }

        public Builder setColorized(boolean colorize) {
            super.setColorized(colorize);
            return this;
        }

        public Builder setOnlyAlertOnce(boolean onlyAlertOnce) {
            super.setOnlyAlertOnce(onlyAlertOnce);
            return this;
        }

        public Builder setAutoCancel(boolean autoCancel) {
            super.setAutoCancel(autoCancel);
            return this;
        }

        public Builder setLocalOnly(boolean b) {
            super.setLocalOnly(b);
            return this;
        }

        public Builder setCategory(String category) {
            super.setCategory(category);
            return this;
        }

        public Builder setDefaults(int defaults) {
            super.setDefaults(defaults);
            return this;
        }

        public Builder setPriority(int pri) {
            super.setPriority(pri);
            return this;
        }

        public Builder addPerson(String uri) {
            super.addPerson(uri);
            return this;
        }

        public Builder setGroup(String groupKey) {
            super.setGroup(groupKey);
            return this;
        }

        public Builder setGroupSummary(boolean isGroupSummary) {
            super.setGroupSummary(isGroupSummary);
            return this;
        }

        public Builder setSortKey(String sortKey) {
            super.setSortKey(sortKey);
            return this;
        }

        public Builder addExtras(Bundle extras) {
            super.addExtras(extras);
            return this;
        }

        public Builder setExtras(Bundle extras) {
            super.setExtras(extras);
            return this;
        }

        public Builder addAction(int icon, CharSequence title, PendingIntent intent) {
            super.addAction(icon, title, intent);
            return this;
        }

        public Builder addAction(Action action) {
            super.addAction(action);
            return this;
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public Builder addInvisibleAction(int icon, CharSequence title, PendingIntent intent) {
            super.addInvisibleAction(icon, title, intent);
            return this;
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public Builder addInvisibleAction(Action action) {
            super.addInvisibleAction(action);
            return this;
        }

        public Builder setStyle(Style style) {
            super.setStyle(style);
            return this;
        }

        public Builder setColor(int argb) {
            super.setColor(argb);
            return this;
        }

        public Builder setVisibility(int visibility) {
            super.setVisibility(visibility);
            return this;
        }

        public Builder setPublicVersion(Notification n) {
            super.setPublicVersion(n);
            return this;
        }

        public Builder setCustomContentView(RemoteViews contentView) {
            super.setCustomContentView(contentView);
            return this;
        }

        public Builder setCustomBigContentView(RemoteViews contentView) {
            super.setCustomBigContentView(contentView);
            return this;
        }

        public Builder setCustomHeadsUpContentView(RemoteViews contentView) {
            super.setCustomHeadsUpContentView(contentView);
            return this;
        }

        public Builder setChannelId(String channelId) {
            super.setChannelId(channelId);
            return this;
        }

        public Builder setTimeoutAfter(long durationMs) {
            super.setTimeoutAfter(durationMs);
            return this;
        }

        public Builder setShortcutId(String shortcutId) {
            super.setShortcutId(shortcutId);
            return this;
        }

        public Builder setBadgeIconType(int icon) {
            super.setBadgeIconType(icon);
            return this;
        }

        public Builder setGroupAlertBehavior(int groupAlertBehavior) {
            super.setGroupAlertBehavior(groupAlertBehavior);
            return this;
        }

        public Builder extend(Extender extender) {
            super.extend(extender);
            return this;
        }
    }
}
