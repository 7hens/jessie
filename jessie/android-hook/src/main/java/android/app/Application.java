package android.app;

import android.os.Bundle;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class Application extends ApplicationHook {

    public void registerActivityLifecycleCallbacks(ActivityLifecycleCallbacks callbacks) {
        super.registerActivityLifecycleCallbacks(callbacks);
    }

    public void unregisterActivityLifecycleCallbacks(ActivityLifecycleCallbacks callbacks) {
        super.unregisterActivityLifecycleCallbacks(callbacks);
    }

    public interface ActivityLifecycleCallbacks extends ApplicationHook.ActivityLifecycleCallbacks {
        void onActivityCreated(Activity activity, Bundle savedInstanceState);

        void onActivityStarted(Activity activity);

        void onActivityResumed(Activity activity);

        void onActivityPaused(Activity activity);

        void onActivityStopped(Activity activity);

        void onActivitySaveInstanceState(Activity activity, Bundle outState);

        void onActivityDestroyed(Activity activity);
    }

    public interface OnProvideAssistDataListener extends ApplicationHook.OnProvideAssistDataListener {
        void onProvideAssistData(Activity activity, Bundle data);
    }
}
