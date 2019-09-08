package cn.jessie.program

import cn.thens.okparcelable.OkParcelable
import dalvik.system.DexClassLoader

class DexInfo(
        val dexPath: String,
        val optimizedDirectory: String,
        val libraryDirectory: String) : OkParcelable {

    open class ClassLoader(parent: java.lang.ClassLoader?, dexInfo: DexInfo)
        : DexClassLoader(
            dexInfo.dexPath,
            dexInfo.optimizedDirectory,
            dexInfo.libraryDirectory,
            parent)
}