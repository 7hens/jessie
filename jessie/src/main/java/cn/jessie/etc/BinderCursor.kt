package cn.jessie.etc

import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.os.IBinder

/**
 * @author 7hens
 */
internal class BinderCursor(columnNames: Array<String>, binder: IBinder) : MatrixCursor(columnNames) {
    private var bundle = Bundle()

    constructor(binder: IBinder) : this(emptyArray(), binder)

    init {
        val value = BinderParcelable(binder)
        bundle.putParcelable(KEY_BINDER, value)
    }

    override fun getExtras(): Bundle {
        return bundle
    }

    companion object {
        private const val KEY_BINDER = "BINDER"

        fun getBinder(cursor: Cursor): IBinder? {
            return try {
                val extras = cursor.extras
                extras.classLoader = BinderCursor::class.java.classLoader
                val binderParcelable = extras.getParcelable(KEY_BINDER) as BinderParcelable
                binderParcelable.binder
            } catch (e: Throwable) {
                Logdog.error(e)
                null
            }
        }
    }
}
