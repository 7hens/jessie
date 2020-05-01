package cn.jessie.etc

import java.io.File
import java.io.FileInputStream

internal object Files {
    fun dir(file: File): File {
        if (!file.exists()) {
            file.mkdirs()
        }
        return file
    }

    fun delete(file: File): Boolean {
        return file.deleteRecursively()
    }

    fun read(file: String): String {
        return try {
            FileInputStream(file).bufferedReader().use { it.readText() }
        } catch (e: Throwable) {
            JCLogger.error(e)
            ""
        }
    }
}


