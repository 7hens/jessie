package android.os;

import java.util.Map;

@SuppressWarnings("ALL")
public final class ServiceManagerHook {

    public static IBinder getService(String name) {
        return null;
    }

    public static IBinder getServiceOrThrow(String name) {
        return null;
    }

    public static void addService(String name, IBinder service) {
    }

    public static void addService(String name, IBinder service, boolean allowIsolated) {
    }

    public static IBinder checkService(String name) {
        return null;
    }

    public static String[] listServices() {
        return null;
    }

    public static void initServiceCache(Map<String, IBinder> cache) {
    }
}
