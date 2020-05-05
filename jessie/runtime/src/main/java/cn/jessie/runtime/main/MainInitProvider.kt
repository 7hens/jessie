package cn.jessie.runtime.main

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import cn.jessie.runtime.test.JessieTests

class MainInitProvider : ContentProvider() {
    override fun onCreate(): Boolean {
        val context = this.context!!
        MainAppContext.initialize(context)
        JessieServices.initialize(context)
//        JessieTests.printClass("android.app.Application")
//        JessieTests.printClass("android.app.Activity")
//        JessieTests.printClass("android.webkit.WebView")
        return true
    }

    override fun getType(uri: Uri): String? = null

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? = null

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int = 0

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = 0
}