package cn.jessie.etc

import android.annotation.SuppressLint
import android.os.FileUtils

@Suppress("MemberVisibilityCanBePrivate", "unused")
@SuppressLint("PrivateApi")
internal object FilePermissions {
    const val UR = 0b100000000
    const val UW = 0b10000000
    const val UX = 0b1000000
    const val GR = 0b100000
    const val GW = 0b10000
    const val GX = 0b1000
    const val OR = 0b100
    const val OW = 0b10
    const val OX = 0b1

    const val U7 = UR or UW or UX
    const val G7 = GR or GW or GX
    const val O7 = OR or OW or OX

    fun set(filePath: String, mode: Int, uid: Int = -1, gid: Int = -1): Int {
        return try {
            FileUtils.setPermissions(filePath, mode, uid, gid)
        } catch (e: Throwable) {
            Logdog.error(e)
            -1
        }
    }
}