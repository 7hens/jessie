package cn.jessie.program

import android.content.IntentFilter
import android.content.res.AssetManager
import android.content.res.XmlResourceParser
import android.os.PatternMatcher
import cn.jessie.etc.Logdog
import java.util.*

internal object AndroidManifestParser {
    private const val MANIFEST_FILE = "AndroidManifest.xml"
    private const val NAMESPACE_ANDROID = "http://schemas.android.com/apk/res/android"
    private const val MANIFEST = "manifest"
    private const val ACTIVITY = "activity"
    private const val SERVICE = "service"
    private const val RECEIVER = "receiver"
    private const val INTENT_FILTER = "intent-filter"
    private const val ACTION = "action"
    private const val CATEGORY = "category"
    private const val DATA = "data"
    private const val NAME = "name"
    private const val PACKAGE = "package"
    private const val SCHEME = "scheme"
    private const val HOST = "host"
    private const val PORT = "port"
    private const val PATH = "path"
    private const val PATH_PREFIX = "pathPrefix"
    private const val PATH_PATTERN = "pathPattern"
    private const val MIME_TYPE = "mimeType"

    fun parse(assetManager: AssetManager): Map<String, List<IntentFilter>> {
        var parser: XmlResourceParser? = null
        val intentFilterMap = hashMapOf<String, List<IntentFilter>>()
        try {
            var packageName = ""
            var intentFilter = IntentFilter()
            var intentFilters = arrayListOf<IntentFilter>()

            parser = assetManager.openXmlResourceParser(MANIFEST_FILE)
            var tagType = parser.next()
            while (tagType != XmlResourceParser.END_DOCUMENT) {
                if (tagType == XmlResourceParser.START_TAG) {
                    when (parser.name) {
                        MANIFEST -> {
                            packageName = parser.getAttributeValue(null, PACKAGE)
                        }
                        ACTIVITY, SERVICE, RECEIVER -> {
                            val className = getFullClassName(packageName, parser.android(NAME))
                            intentFilters = ArrayList()
                            intentFilterMap[className] = intentFilters
                        }
                        INTENT_FILTER -> {
                            intentFilter = IntentFilter()
                            intentFilters.add(intentFilter)
                        }
                        ACTION -> {
                            intentFilter.addAction(parser.android(NAME))
                        }
                        CATEGORY -> {
                            intentFilter.addCategory(parser.android(NAME))
                        }
                        DATA -> {
                            val scheme = parser.android(SCHEME)
                            if (scheme.isNotEmpty()) {
                                intentFilter.addDataScheme(scheme)
                            }

                            val host = parser.android(HOST)
                            val port = parser.android(PORT)
                            if (scheme.isNotEmpty() && port.isNotEmpty()) {
                                intentFilter.addDataAuthority(host, port)
                            }

                            val path = parser.android(PATH)
                            if (path.isNotEmpty()) {
                                val pathPrefix = parser.android(PATH_PREFIX)
                                val pathPattern = parser.android(PATH_PATTERN)
                                intentFilter.addDataPath(path, patternMatcherType(pathPrefix, pathPattern))
                            }

                            val mimeType = parser.android(MIME_TYPE)
                            if (mimeType.isNotEmpty()) {
                                intentFilter.addDataType(mimeType)
                            }
                        }
                    }
                }
                tagType = parser.next()
            }
        } catch (e: Exception) {
            Logdog.error(e)
        } finally {
            parser?.close()
            return intentFilterMap
        }
    }

    private fun XmlResourceParser.android(name: String): String {
        return getAttributeValue(NAMESPACE_ANDROID, name) ?: ""
    }

    private fun getFullClassName(packageName: String, className: String): String {
        return when {
            className.startsWith(".") -> packageName + className
            className.contains(".") -> className
            else -> "$packageName.$className"
        }
    }

    private fun patternMatcherType(pathPrefix: String, pathPattern: String): Int {
        return when {
            pathPrefix.isEmpty() && pathPattern.isEmpty() -> PatternMatcher.PATTERN_LITERAL
            !pathPrefix.isEmpty() -> PatternMatcher.PATTERN_PREFIX
            else -> PatternMatcher.PATTERN_SIMPLE_GLOB
        }
    }
}
