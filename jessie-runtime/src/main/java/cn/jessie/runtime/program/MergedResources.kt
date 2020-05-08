package cn.jessie.runtime.program

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.res.*
import android.graphics.Movie
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import java.io.InputStream

/**
 * @author 7hens
 */
@Suppress("DEPRECATION")
@SuppressLint("Recycle")
class MergedResources(private val primary: Resources, private val secondary: Resources)
    : Resources(primary.assets, primary.displayMetrics, primary.configuration) {

    override fun getTextArray(id: Int): Array<CharSequence> {
        return try {
            primary.getTextArray(id)
        } catch (e: Throwable) {
            secondary.getTextArray(id)
        }
    }

    override fun obtainTypedArray(id: Int): TypedArray {
        return try {
            primary.obtainTypedArray(id)
        } catch (e: Throwable) {
            secondary.obtainTypedArray(id)
        }
    }

    override fun getAnimation(id: Int): XmlResourceParser {
        return try {
            primary.getAnimation(id)
        } catch (e: Throwable) {
            secondary.getAnimation(id)
        }
    }

    override fun getText(id: Int): CharSequence {
        return try {
            primary.getText(id)
        } catch (e: Throwable) {
            secondary.getText(id)
        }
    }

    override fun getText(id: Int, def: CharSequence?): CharSequence {
        return try {
            primary.getText(id, def)
        } catch (e: Throwable) {
            secondary.getText(id, def)
        }
    }

    override fun getDisplayMetrics(): DisplayMetrics {
        return try {
            primary.getDisplayMetrics()
        } catch (e: Throwable) {
            secondary.getDisplayMetrics()
        }
    }

    override fun getDrawableForDensity(id: Int, density: Int): Drawable? {
        return try {
            primary.getDrawableForDensity(id, density)
        } catch (e: Throwable) {
            secondary.getDrawableForDensity(id, density)
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun getDrawableForDensity(id: Int, density: Int, theme: Theme?): Drawable? {
        return try {
            primary.getDrawableForDensity(id, density, theme)
        } catch (e: Throwable) {
            secondary.getDrawableForDensity(id, density, theme)
        }
    }

    override fun obtainAttributes(set: AttributeSet?, attrs: IntArray?): TypedArray {
        return try {
            primary.obtainAttributes(set, attrs)
        } catch (e: Throwable) {
            secondary.obtainAttributes(set, attrs)
        }
    }

    override fun getDimensionPixelSize(id: Int): Int {
        return try {
            primary.getDimensionPixelSize(id)
        } catch (e: Throwable) {
            secondary.getDimensionPixelSize(id)
        }
    }

    override fun getIntArray(id: Int): IntArray {
        return try {
            primary.getIntArray(id)
        } catch (e: Throwable) {
            secondary.getIntArray(id)
        }
    }

    override fun getValue(id: Int, outValue: TypedValue?, resolveRefs: Boolean) {
        try {
            primary.getValue(id, outValue, resolveRefs)
        } catch (e: Throwable) {
            secondary.getValue(id, outValue, resolveRefs)
        }
    }

    override fun getValue(name: String?, outValue: TypedValue?, resolveRefs: Boolean) {
        try {
            primary.getValue(name, outValue, resolveRefs)
        } catch (e: Throwable) {
            secondary.getValue(name, outValue, resolveRefs)
        }
    }

    override fun getQuantityString(id: Int, quantity: Int, vararg formatArgs: Any?): String {
        return try {
            primary.getQuantityString(id, quantity, *formatArgs)
        } catch (e: Throwable) {
            secondary.getQuantityString(id, quantity, *formatArgs)
        }
    }

    override fun getQuantityString(id: Int, quantity: Int): String {
        return try {
            primary.getQuantityString(id, quantity)
        } catch (e: Throwable) {
            secondary.getQuantityString(id, quantity)
        }
    }

    override fun getResourcePackageName(resid: Int): String {
        return try {
            primary.getResourcePackageName(resid)
        } catch (e: Throwable) {
            secondary.getResourcePackageName(resid)
        }
    }

    override fun getStringArray(id: Int): Array<String> {
        return try {
            primary.getStringArray(id)
        } catch (e: Throwable) {
            secondary.getStringArray(id)
        }
    }

    override fun openRawResourceFd(id: Int): AssetFileDescriptor {
        return try {
            primary.openRawResourceFd(id)
        } catch (e: Throwable) {
            secondary.openRawResourceFd(id)
        }
    }

    override fun getDimension(id: Int): Float {
        return try {
            primary.getDimension(id)
        } catch (e: Throwable) {
            secondary.getDimension(id)
        }
    }

    override fun getColorStateList(id: Int): ColorStateList {
        return try {
            primary.getColorStateList(id)
        } catch (e: Throwable) {
            secondary.getColorStateList(id)
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun getColorStateList(id: Int, theme: Theme?): ColorStateList {
        return try {
            primary.getColorStateList(id, theme)
        } catch (e: Throwable) {
            secondary.getColorStateList(id, theme)
        }
    }

    override fun getBoolean(id: Int): Boolean {
        return try {
            primary.getBoolean(id)
        } catch (e: Throwable) {
            secondary.getBoolean(id)
        }
    }

    override fun getIdentifier(name: String?, defType: String?, defPackage: String?): Int {
        return try {
            primary.getIdentifier(name, defType, defPackage)
        } catch (e: Throwable) {
            secondary.getIdentifier(name, defType, defPackage)
        }
    }

    override fun getQuantityText(id: Int, quantity: Int): CharSequence {
        return try {
            primary.getQuantityText(id, quantity)
        } catch (e: Throwable) {
            secondary.getQuantityText(id, quantity)
        }
    }

    override fun getColor(id: Int): Int {
        return try {
            primary.getColor(id)
        } catch (e: Throwable) {
            secondary.getColor(id)
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun getColor(id: Int, theme: Theme?): Int {
        return try {
            primary.getColor(id, theme)
        } catch (e: Throwable) {
            secondary.getColor(id, theme)
        }
    }

    override fun openRawResource(id: Int): InputStream {
        return try {
            primary.openRawResource(id)
        } catch (e: Throwable) {
            secondary.openRawResource(id)
        }
    }

    override fun openRawResource(id: Int, value: TypedValue?): InputStream {
        return try {
            primary.openRawResource(id, value)
        } catch (e: Throwable) {
            secondary.openRawResource(id, value)
        }
    }

    override fun getMovie(id: Int): Movie {
        return try {
            primary.getMovie(id)
        } catch (e: Throwable) {
            secondary.getMovie(id)
        }
    }

    override fun getInteger(id: Int): Int {
        return try {
            primary.getInteger(id)
        } catch (e: Throwable) {
            secondary.getInteger(id)
        }
    }

    override fun parseBundleExtras(parser: XmlResourceParser?, outBundle: Bundle?) {
        try {
            primary.parseBundleExtras(parser, outBundle)
        } catch (e: Throwable) {
            secondary.parseBundleExtras(parser, outBundle)
        }
    }

    override fun getDrawable(id: Int): Drawable {
        return try {
            primary.getDrawable(id)
        } catch (e: Throwable) {
            secondary.getDrawable(id)
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun getDrawable(id: Int, theme: Theme?): Drawable {
        return try {
            primary.getDrawable(id, theme)
        } catch (e: Throwable) {
            secondary.getDrawable(id, theme)
        }
    }

    override fun getResourceTypeName(resid: Int): String {
        return try {
            primary.getResourceTypeName(resid)
        } catch (e: Throwable) {
            secondary.getResourceTypeName(resid)
        }
    }

    override fun getLayout(id: Int): XmlResourceParser {
        return try {
            primary.getLayout(id)
        } catch (e: Throwable) {
            secondary.getLayout(id)
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    override fun getFont(id: Int): Typeface {
        return try {
            primary.getFont(id)
        } catch (e: Throwable) {
            secondary.getFont(id)
        }
    }

    override fun getXml(id: Int): XmlResourceParser {
        return try {
            primary.getXml(id)
        } catch (e: Throwable) {
            secondary.getXml(id)
        }
    }

    override fun getString(id: Int): String {
        return try {
            primary.getString(id)
        } catch (e: Throwable) {
            secondary.getString(id)
        }
    }

    override fun getString(id: Int, vararg formatArgs: Any?): String {
        return try {
            primary.getString(id, *formatArgs)
        } catch (e: Throwable) {
            secondary.getString(id, *formatArgs)
        }
    }

    override fun getResourceName(resid: Int): String {
        return try {
            primary.getResourceName(resid)
        } catch (e: Throwable) {
            secondary.getResourceName(resid)
        }
    }

    override fun parseBundleExtra(tagName: String?, attrs: AttributeSet?, outBundle: Bundle?) {
        try {
            primary.parseBundleExtra(tagName, attrs, outBundle)
        } catch (e: Throwable) {
            secondary.parseBundleExtra(tagName, attrs, outBundle)
        }
    }

    override fun getDimensionPixelOffset(id: Int): Int {
        return try {
            primary.getDimensionPixelOffset(id)
        } catch (e: Throwable) {
            secondary.getDimensionPixelOffset(id)
        }
    }

    override fun getValueForDensity(id: Int, density: Int, outValue: TypedValue?, resolveRefs: Boolean) {
        try {
            primary.getValueForDensity(id, density, outValue, resolveRefs)
        } catch (e: Throwable) {
            secondary.getValueForDensity(id, density, outValue, resolveRefs)
        }
    }

    override fun getResourceEntryName(resid: Int): String {
        return try {
            primary.getResourceEntryName(resid)
        } catch (e: Throwable) {
            secondary.getResourceEntryName(resid)
        }
    }

    override fun getFraction(id: Int, base: Int, pbase: Int): Float {
        return try {
            primary.getFraction(id, base, pbase)
        } catch (e: Throwable) {
            secondary.getFraction(id, base, pbase)
        }
    }
}