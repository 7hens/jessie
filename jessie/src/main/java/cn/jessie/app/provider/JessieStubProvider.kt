package cn.jessie.app.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Process
import cn.jessie.app.SystemServicesHook
import cn.jessie.app.application.MyProgramApp
import cn.jessie.app.service.JessieStubService
import cn.jessie.etc.BinderCursor
import cn.jessie.etc.JCLogger
import cn.jessie.main.JessieStubComponents
import cn.jessie.main.MainAppContext
import cn.thens.okbinder.OkBinder

abstract class JessieStubProvider : ContentProvider() {
    override fun onCreate(): Boolean {
        MainAppContext.initialize(context!!)
        SystemServicesHook.initialize()
        MyProgramApp.initialize(MainAppContext.get())
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        JCLogger.debug(uri)
        when (getActionFromUri(uri)) {
            ACTION_EXIT -> {
                Process.killProcess(Process.myPid())
                return null
            }
            ACTION_SERVICE -> {
                return BinderCursor(OkBinder.create(JessieStubService.serviceManager))
            }
            ACTION_PROVIDER -> {
                val programUri = unboxUri(uri) ?: return null
                return provider(programUri).query(programUri, projection, selection, selectionArgs, sortOrder)
            }
            else -> return null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        JCLogger.debug(uri)
        val programUri = unboxUri(uri) ?: return null
        return provider(programUri).insert(programUri, values)
    }

    override fun bulkInsert(uri: Uri, values: Array<ContentValues>): Int {
        JCLogger.debug(uri)
        val programUri = unboxUri(uri) ?: return -1
        return provider(programUri).bulkInsert(programUri, values)
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        JCLogger.debug(uri)
        val programUri = unboxUri(uri) ?: return -1
        return provider(programUri).update(programUri, values, selection, selectionArgs)
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        JCLogger.debug(uri)
        val programUri = unboxUri(uri) ?: return -1
        return provider(programUri).delete(programUri, selection, selectionArgs)
    }

    override fun getType(uri: Uri): String? {
        JCLogger.debug(uri)
        val programUri = unboxUri(uri) ?: return null
        return provider(programUri).getType(programUri)
    }


    private fun provider(uri: Uri): ContentProvider {
        return MyProgramProviders.require(uri)
    }

    companion object {
        const val ACTION_EXIT = "exit"
        const val ACTION_SERVICE = "service"
        const val ACTION_PROVIDER = "provider"

        private const val SCHEMA = "content://"

        /**
         * URI 装箱
         *
         * - **from**   "`content://plugin.provider.authority/domain`"
         * - **to** "`content://${hostPackageName}.jc2/provider/plugin.package.name/plugin.provider.authority/domain`"
         */
        fun boxUri(uri: Uri, packageName: String, authority: String): Uri {
            val uriWithoutSchema = uri.toString().substring(SCHEMA.length)
            return Uri.parse("$SCHEMA$authority/$ACTION_PROVIDER/$packageName/$uriWithoutSchema")
        }

        /**
         * URI 拆箱
         *
         * - **from** "`content://${hostPackageName}.jc2/provider/plugin.package.name/plugin.provider.authority/domain`"
         * - **to**   "`content://plugin.provider.authority/domain`"
         */
        @Suppress("SpellCheckingInspection")
        private fun unboxUri(uri: Uri): Uri? {
            val authority = uri.authority ?: ""
            if (!matchesAuthority(authority)) return null
            val pathSegments = uri.pathSegments
            if (pathSegments.size < 3) return null
            val packageName = pathSegments[1]
            val uriString = uri.toString().replace("$authority/$ACTION_PROVIDER/$packageName/", "")
            return Uri.parse(uriString)
        }

        private val mainPackageName by lazy { MainAppContext.packageName }

        fun authority(index: Int): String {
            return JessieStubComponents.getProviderAuthority(index)
        }

        fun uri(processIndex: Int, action: String): Uri {
            return Uri.parse("content://${authority(processIndex)}/$action")
        }

        private fun getActionFromUri(uri: Uri): String {
            val pathSegments = uri.pathSegments
            if (pathSegments.isNullOrEmpty()) return ""
            return pathSegments[0]
        }

        private fun matchesAuthority(authority: String): Boolean {
            return authority.matches(Regex("^$mainPackageName.jc\\d{1,2}\$"))
        }
    }
}