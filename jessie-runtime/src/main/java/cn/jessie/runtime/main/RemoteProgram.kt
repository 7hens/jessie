package cn.jessie.runtime.main

import android.content.pm.PackageInfo
import cn.jessie.program.DexInfo
import cn.jessie.runtime.etc.JCLogger
import cn.jessie.runtime.program.DexProgram

internal class RemoteProgram(override val packageName: String) : DexProgram() {

    private val programManager get() = JessieServices.programManager

    override val dexInfo: DexInfo by lazy {
        programManager.getDexInfo(packageName)!!
    }

    override val packageInfo: PackageInfo by lazy {
        programManager.getPackageInfo(packageName)!!
    }

    override fun preload() {
        programManager.preload(packageName)
    }

    override fun start() {
        programManager.start(packageName)
    }

    override fun stop() {
        try {
            programManager.stop(packageName)
        } catch (e: Throwable) {
            JCLogger.debug("packageName = $packageName")
            JCLogger.error(e)
        }
    }
}
