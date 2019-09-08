package cn.jessie.sample.plugin.provider

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cn.jessie.sample.plugin.R
import kotlinx.android.synthetic.main.fragment_main_provider.*

class ProviderFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main_provider, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vInsert.setOnClickListener { contentResolver.insert(uri, ContentValues()) }
        vUpdate.setOnClickListener { contentResolver.update(uri, ContentValues(), null, null) }
        vDelete.setOnClickListener { contentResolver.delete(uri, null, null) }
        vQuery.setOnClickListener { contentResolver.query(uri, null, null, null, null)?.close() }
    }

    private val uri by lazy { Uri.parse("content://${MyProvider.AUTHORITY}") }

    private val contentResolver by lazy { requireContext().contentResolver }
}