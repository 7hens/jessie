package android.content

import android.annotation.SuppressLint
import android.content.pm.ProviderInfo
import android.content.res.AssetFileDescriptor
import android.content.res.Configuration
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import cn.jessie.JessieRuntime
import java.io.FileDescriptor
import java.io.PrintWriter
import java.util.ArrayList

@SuppressLint("MissingPermission", "NewApi")
abstract class ContentProviderWrapper : ContentProvider() {
    abstract val base: ContentProvider

    override fun attachInfo(context: Context?, info: ProviderInfo?) {
        JessieRuntime.initialize(context!!)
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

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?, cancellationSignal: CancellationSignal?): Cursor? {
        return base.query(uri, projection, selection, selectionArgs, sortOrder, cancellationSignal)
    }

    override fun query(uri: Uri, projection: Array<out String>?, queryArgs: Bundle?, cancellationSignal: CancellationSignal?): Cursor? {
        return base.query(uri, projection, queryArgs, cancellationSignal)
    }

    override fun openTypedAssetFile(uri: Uri, mimeTypeFilter: String, opts: Bundle?): AssetFileDescriptor? {
        return base.openTypedAssetFile(uri, mimeTypeFilter, opts)
    }

    override fun openTypedAssetFile(uri: Uri, mimeTypeFilter: String, opts: Bundle?, signal: CancellationSignal?): AssetFileDescriptor? {
        return base.openTypedAssetFile(uri, mimeTypeFilter, opts, signal)
    }

    override fun call(authority: String, method: String, arg: String?, extras: Bundle?): Bundle? {
        return base.call(authority, method, arg, extras)
    }

    override fun call(method: String, arg: String?, extras: Bundle?): Bundle? {
        return base.call(method, arg, extras)
    }

    override fun uncanonicalize(url: Uri): Uri? {
        return base.uncanonicalize(url)
    }

    override fun <T : Any?> openPipeHelper(uri: Uri, mimeType: String, opts: Bundle?, args: T?, func: PipeDataWriter<T>): ParcelFileDescriptor {
        return base.openPipeHelper(uri, mimeType, opts, args, func)
    }

    override fun bulkInsert(uri: Uri, values: Array<out ContentValues>): Int {
        return base.bulkInsert(uri, values)
    }

    override fun onLowMemory() {
        base.onLowMemory()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        base.onConfigurationChanged(newConfig)
    }

    override fun refresh(uri: Uri?, args: Bundle?, cancellationSignal: CancellationSignal?): Boolean {
        return base.refresh(uri, args, cancellationSignal)
    }

    override fun openFile(uri: Uri, mode: String): ParcelFileDescriptor? {
        return base.openFile(uri, mode)
    }

    override fun openFile(uri: Uri, mode: String, signal: CancellationSignal?): ParcelFileDescriptor? {
        return base.openFile(uri, mode, signal)
    }

    override fun onTrimMemory(level: Int) {
        base.onTrimMemory(level)
    }

    override fun getStreamTypes(uri: Uri, mimeTypeFilter: String): Array<String>? {
        return base.getStreamTypes(uri, mimeTypeFilter)
    }

    override fun applyBatch(authority: String, operations: ArrayList<ContentProviderOperation>): Array<ContentProviderResult> {
        return base.applyBatch(authority, operations)
    }

    override fun applyBatch(operations: ArrayList<ContentProviderOperation>): Array<ContentProviderResult> {
        return base.applyBatch(operations)
    }

    override fun openAssetFile(uri: Uri, mode: String): AssetFileDescriptor? {
        return base.openAssetFile(uri, mode)
    }

    override fun openAssetFile(uri: Uri, mode: String, signal: CancellationSignal?): AssetFileDescriptor? {
        return base.openAssetFile(uri, mode, signal)
    }

    override fun canonicalize(url: Uri): Uri? {
        return base.canonicalize(url)
    }

    override fun shutdown() {
        base.shutdown()
    }

    override fun isTemporary(): Boolean {
        return base.isTemporary()
    }

    override fun dump(fd: FileDescriptor?, writer: PrintWriter?, args: Array<out String>?) {
        base.dump(fd, writer, args)
    }
}