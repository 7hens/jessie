package android.app;

import android.content.Context;

public class Notification2 extends NotificationHook {

    public static class Builder extends NotificationHook.Builder {

        public Builder(Context context) {
            super(context);
        }

        public Builder(Context context, String channelId) {
            super(context, channelId);
        }
    }
}
