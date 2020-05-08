package cn.jessie.runtime.main

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.pm.ProviderInfo
import android.database.Cursor
import android.net.Uri
import cn.jessie.runtime.etc.BinderCursor

/**
 * 守护者 Provider
 *
 * 用来和其他进程交互，并传递 Binder。
 */
class JessieDaemonProviderImpl : ContentProvider() {
    override fun attachInfo(context: Context?, info: ProviderInfo?) {
        MainAppContext.initialize(context!!)
        JessieServices.Daemon.initialize()
        super.attachInfo(context, info)
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        if (selection == null) return null
        val service = JessieServices.Daemon.query(selection) ?: return null
        return BinderCursor(service)
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        return -1
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return -1
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    companion object {
        val authority by lazy { MainAppContext.packageName + ".jc.daemon" }
    }
}