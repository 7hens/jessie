package android.app;

import android.content.ComponentName;
import android.os.IBinder;

oneway interface IServiceConnection {
    void connected(in ComponentName name, IBinder service, boolean dead);
}
