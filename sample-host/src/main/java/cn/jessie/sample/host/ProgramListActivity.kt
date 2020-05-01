package cn.jessie.sample.host

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.jessie.Jessie
import cn.jessie.etc.JCLogger
import cn.jessie.program.Program
import kotlinx.android.synthetic.main.activity_program_list.*
import kotlinx.android.synthetic.main.item_program.view.*
import java.io.File
import java.util.concurrent.Executors

class ProgramListActivity : Activity() {
    private val adapter by lazy { ProgramAdapter() }
    private val threadExecutor by lazy { Executors.newSingleThreadExecutor() }

    private val pluginDir by lazy {
        val sdcard = Environment.getExternalStorageDirectory()
        val file = File(sdcard, PLUGIN_DIR)
        if (!file.exists()) {
            file.mkdirs()
        }
        file
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_program_list)
        requestPermissions()
        vProgramList.adapter = adapter
        vProgramList.layoutManager = LinearLayoutManager(this)
        vRefresh.setOnRefreshListener { refreshPrograms() }
        vProgramList.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                if (parent.getChildAdapterPosition(view) == 0) {
                    outRect.top = vTips.height
                }
            }
        })
        refreshPrograms()
    }

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val storagePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE
            if (checkSelfPermission(storagePermission) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(storagePermission)) {
                    Toast.makeText(this, "请先打开存储权限", Toast.LENGTH_SHORT).show()
                }
                requestPermissions(arrayOf(storagePermission), 0)
            }
        }
    }

    private inline fun runOnUiThread(crossinline fn: () -> Unit) {
        val mainLooper = Looper.getMainLooper()
        if (mainLooper.thread == Thread.currentThread()) {
            fn()
        } else {
            Handler(mainLooper).post { fn() }
        }
    }

    private fun refreshPrograms() {
        vRefresh.post { vRefresh.isRefreshing = true }
        Toast.makeText(this, "加载可能会有点慢...请耐心等待", Toast.LENGTH_SHORT).show()
        threadExecutor.submit {
            try {
                (pluginDir.listFiles() ?: emptyArray())
                        .filter { it.extension == "apk" }
                        .forEach {
                            try {
                                Jessie.install(it)
                                it.delete()
                            } catch (e: Throwable) {
                                JCLogger.error(e)
                            }
                        }
            } catch (e: Throwable) {
                JCLogger.error(e)
            }
//            Logdog.debug("preloading programs ...")
            val programs = Jessie.programs.values
            programs.forEach { it.preload() }
            runOnUiThread {
//                Logdog.debug("** refresh success **")
                adapter.refill(programs)
                vRefresh.isRefreshing = false
            }
        }
    }

    companion object {
        private const val PLUGIN_DIR = "jc_plugins"
    }

    class ProgramAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun getItemCount(): Int = dataSet.size

        @SuppressLint("WrongConstant")
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.item_program, parent, false)
            return object : RecyclerView.ViewHolder(view) {}
        }

        @Suppress("DEPRECATION")
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            holder.itemView.apply {
                val program = dataSet[position]
                val applicationInfo = program.packageInfo.applicationInfo
                val resources = program.resources
                vProgramIcon.setImageDrawable(run {
                    val iconRes = applicationInfo.icon
                    if (iconRes == 0) null else resources.getDrawable(iconRes)
                })
                vProgramName.text = run {
                    val labelRes = applicationInfo.labelRes
                    if (labelRes == 0) "(null)" else resources.getText(labelRes)
                }
                vProgramPackage.text = program.packageName
                setOnClickListener {
                    try {
                        program.start()
                    } catch (e: Exception) {
                        JCLogger.error(e)
                    }
                }
                setOnLongClickListener {
                    PopupMenu(context, this).apply {
                        menu.add("卸载")
                        setOnMenuItemClickListener { item ->
                            when (item.title) {
                                "卸载" -> {
                                    Jessie.uninstall(program.packageName)
                                    dataSet.remove(program)
                                    notifyDataSetChanged()
                                    true
                                }
                                else -> false
                            }
                        }
                        show()
                    }
                    true
                }
            }
        }

        private val dataSet = mutableListOf<Program>()

        fun refill(newDataSet: Iterable<Program>) {
            dataSet.clear()
            dataSet.addAll(newDataSet)
            notifyDataSetChanged()
        }
    }
}