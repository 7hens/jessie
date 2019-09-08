package cn.jessie.etc

import android.os.Build

@Suppress("unused", "MemberVisibilityCanBePrivate", "SpellCheckingInspection", "DEPRECATION")
internal object BuildCompat {

    const val ARM = "arm"
    const val ARM64 = "arm64"

    val ALL_SUPPORTED_ABIS = arrayOf(Build.CPU_ABI, Build.CPU_ABI2)

    val SUPPORTED_ABIS: Array<String> by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Build.SUPPORTED_ABIS != null) {
                Build.SUPPORTED_ABIS
            } else {
                ALL_SUPPORTED_ABIS
            }
        } else {
            ALL_SUPPORTED_ABIS
        }
    }

    val SUPPORTED_32_BIT_ABIS: Array<String> by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Build.SUPPORTED_32_BIT_ABIS != null) {
                Build.SUPPORTED_32_BIT_ABIS
            } else {
                ALL_SUPPORTED_ABIS
            }
        } else {
            ALL_SUPPORTED_ABIS
        }
    }

    val SUPPORTED_64_BIT_ABIS: Array<String> by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Build.SUPPORTED_64_BIT_ABIS != null) {
                Build.SUPPORTED_64_BIT_ABIS
            } else {
                ALL_SUPPORTED_ABIS
            }
        } else {
            ALL_SUPPORTED_ABIS
        }
    }
}
