package cn.jessie.runtime.app

import android.content.pm.PackageInfo
import android.content.res.Resources
import cn.jessie.Jessie
import cn.jessie.runtime.program.AbstractProgram
import cn.jessie.runtime.program.AndroidPackageComponents
import cn.jessie.runtime.program.DexInfo

internal object MyProgram : AbstractProgram() {
    private val delegate by lazy {
        val currentProcessName = Jessie.currentProgramProcessName
        require(currentProcessName.isNotEmpty())
        val colonIndex = currentProcessName.indexOf(":")
        val currentPackageName = if (colonIndex == -1) {
            currentProcessName
        } else {
            currentProcessName.substring(0, colonIndex)
        }
//        Logdog.warn("""
//            currentPackageName = $currentPackageName
//            currentProcessName = $currentProcessName
//        """.trimIndent())
        Jessie.getProgram(currentPackageName)!!
    }

    override val packageName: String get() = delegate.packageName
    override val classLoader: ClassLoader get() = delegate.classLoader
    override val resources: Resources get() = delegate.resources
    override val packageInfo: PackageInfo get() = delegate.packageInfo
    override val dexInfo: DexInfo get() = delegate.dexInfo
    override val packageComponents: AndroidPackageComponents get() = delegate.packageComponents

    override fun preload() = delegate.preload()
    override fun start() = delegate.start()
    override fun stop() = delegate.stop()
}