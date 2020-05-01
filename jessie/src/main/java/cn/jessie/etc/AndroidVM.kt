package cn.jessie.etc

import android.annotation.SuppressLint
import android.os.Build

/**
 * @author 7hens
 */
@SuppressLint("PrivateApi")
internal object AndroidVM {
    /**
     * 是否运行 ART 虚拟机
     */
    val isArt: Boolean by lazy {
        System.getProperty("java.vm.version", "")?.startsWith("2") ?: false
    }

    @Suppress("LocalVariableName")
    val is64Bit: Boolean by lazy {
        try {
            // Android API 21之前不支持64位CPU
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                false
            } else {
                val _VMRuntime = Class.forName("dalvik.system.VMRuntime")
                val runtime = Reflections.invoke(_VMRuntime, "getRuntime")
                Reflections.invoke(runtime!!, "is64Bit") as Boolean
            }
        } catch (e: Throwable) {
            JCLogger.error(e)
            false
        }
    }
}
