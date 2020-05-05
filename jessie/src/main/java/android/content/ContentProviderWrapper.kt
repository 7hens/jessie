package android.content

import android.content.pm.ProviderInfo
import android.database.Cursor
import android.net.Uri

abstract class ContentProviderWrapper : ContentProvider() {
    abstract val base: ContentProvider

    override fun attachInfo(context: Context?, info: ProviderInfo?) {
        super.attachInfo(context, info)
        base.attachInfo(context, info)
    }

    override fun onCreate(): Boolean {
        return base.onCreate()
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        return base.query(uri, projection, selection, selectionArgs, sortOrder)
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return base.insert(uri, values)
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        return base.update(uri, values, selection, selectionArgs)
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return base.delete(uri, selection, selectionArgs)
    }

    override fun getType(uri: Uri): String? {
        return base.getType(uri)
    }
}