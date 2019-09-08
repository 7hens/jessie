package cn.jessie.program

import android.content.pm.PackageInfo
import android.content.res.Resources

interface Program {
    val packageName: String

    val packageInfo: PackageInfo

    val packageComponents: AndroidPackageComponents

    val classLoader: ClassLoader

    val resources: Resources

    val dexInfo: DexInfo

    fun preload()

    fun start()

    fun stop()
}
