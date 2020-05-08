package android.os;

import java.util.Map;

public final class ServiceManager {

    public static IBinder getService(String name) {
        return ServiceManagerHook.getService(name);
    }

    public static IBinder getServiceOrThrow(String name) {
        return ServiceManagerHook.getServiceOrThrow(name);
    }

    public static void addService(String name, IBinder service) {
        ServiceManagerHook.addService(name, service);
    }

    public static void addService(String name, IBinder service, boolean allowIsolated) {
        ServiceManagerHook.addService(name, service, allowIsolated);
    }

    public static IBinder checkService(String name) {
        return ServiceManagerHook.checkService(name);
    }

    public static String[] listServices() {
        return ServiceManagerHook.listServices();
    }

    public static void initServiceCache(Map<String, IBinder> cache) {
        ServiceManagerHook.initServiceCache(cache);
    }
}
