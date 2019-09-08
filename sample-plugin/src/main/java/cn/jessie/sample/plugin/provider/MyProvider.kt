package cn.jessie.sample.plugin.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.widget.Toast
import cn.jessie.sample.plugin.PluginLog


class MyProvider : ContentProvider() {
    override fun onCreate(): Boolean {
        showToast("Provider.onCreate")
        PluginLog.debug("Provider.onCreate")
        return true
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        showToast("Provider.insert")
        return null
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        showToast("Provider.query")
        return null
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        showToast("Provider.update")
        return -1
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        showToast("Provider.delete")
        return -1
    }

    override fun getType(uri: Uri): String? {
        showToast("Provider.onCreate")
        return null
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val AUTHORITY = "jessie.sample.plugin.provider"
    }
}