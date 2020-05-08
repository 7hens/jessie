/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.app;

import android.content.Intent;
import android.os.IBinder;

/**
 * @deprecated will be removed soon. See individual methods for alternatives.
 */
@Deprecated
public abstract class ActivityManagerNative {
    /**
     * Cast a Binder object into an activity manager interface, generating
     * a proxy if needed.
     *
     * @deprecated use IActivityManager.Stub.asInterface instead.
     */
    public static IActivityManager asInterface(IBinder obj) {
        return IActivityManager.Stub.asInterface(obj);
    }

    /**
     * Retrieve the system's default/global activity manager.
     *
     * @deprecated use ActivityManager.getService instead.
     */
    public static IActivityManager getDefault() {
        throw new RuntimeException("Stub!");
    }

    /**
     * Convenience for checking whether the system is ready.  For internal use only.
     *
     * @deprecated use ActivityManagerInternal.isSystemReady instead.
     */
    public static boolean isSystemReady() {
        throw new RuntimeException("Stub!");
    }

    /**
     * @deprecated use ActivityManager.broadcastStickyIntent instead.
     */
    public static void broadcastStickyIntent(Intent intent, String permission, int userId) {
        throw new RuntimeException("Stub!");
    }

    /**
     * Convenience for sending a sticky broadcast.  For internal use only.
     * If you don't care about permission, use null.
     *
     * @deprecated use ActivityManager.broadcastStickyIntent instead.
     */
    public static void broadcastStickyIntent(Intent intent, String permission, int appOp, int userId) {
        throw new RuntimeException("Stub!");
    }

    /**
     * @deprecated use ActivityManager.noteWakeupAlarm instead.
     */
    public static void noteWakeupAlarm(PendingIntent ps, int sourceUid, String sourcePkg, String tag) {
        throw new RuntimeException("Stub!");
    }

    /**
     * @deprecated use ActivityManager.noteAlarmStart instead.
     */
    public static void noteAlarmStart(PendingIntent ps, int sourceUid, String tag) {
        throw new RuntimeException("Stub!");
    }

    /**
     * @deprecated use ActivityManager.noteAlarmFinish instead.
     */
    public static void noteAlarmFinish(PendingIntent ps, int sourceUid, String tag) {
        throw new RuntimeException("Stub!");
    }
}