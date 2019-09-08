package java.io

import android.annotation.SuppressLint
import cn.jessie.app.MyProgram
import cn.jessie.main.MainAppContext
import java.net.URI

open class FileHook : File {

    constructor(pathname: String) : super(wrapPath(pathname))

    constructor(parent: String, child: String) : super(wrapPath(parent + pathSeparator + child))

    constructor(parent: File, child: String) : super(parent.absolutePath, child)

    constructor(uri: URI) : super(uri)

    companion object {
        @SuppressLint("SdCardPath")
        private fun wrapPath(path: String): String {
            val host = MainAppContext.packageName
            val program = MyProgram.packageName
            return path.replace("/data/data/$program", "/data/data/$host/app_jc_plugins/$program/data")
        }
    }
}