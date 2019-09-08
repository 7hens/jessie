package cn.jessie.app

import android.content.Context
import android.content.Intent
import android.content.IntentFilter

internal object IntentMatcher {
    private const val TAG = "IntentMatcher"

    fun match(context: Context, intent: Intent, intentFilter: IntentFilter): Boolean {
        val action = intent.action
        val type = intent.resolveTypeIfNeeded(context.contentResolver)
        val scheme = intent.scheme
        val data = intent.data
        val categories = intent.categories
        val result = intentFilter.match(action, type, scheme, data, categories, TAG)
        return result >= 0
    }
}