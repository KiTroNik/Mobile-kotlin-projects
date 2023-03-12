package com.example.todo

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private var adapter: TaskAdapter? = null
    private lateinit var sqLiteHelper: SQLiteHelper
    private lateinit var stdList: ArrayList<TaskModel>
    private lateinit var tempList: ArrayList<TaskModel>
    private lateinit var minutes: Number
    private lateinit var category: String
    lateinit var shared : SharedPreferences

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        shared = PreferenceManager.getDefaultSharedPreferences(this)
        sqLiteHelper = SQLiteHelper(this)
        recyclerView = findViewById(R.id.recyclerView)
        stdList = sqLiteHelper.getAllTasks()
        sorList()
        applySettings()
        tempList = ArrayList()
        tempList.addAll(stdList)
        adapter = TaskAdapter(this, stdList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        add_button.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }

        adapter?.setOnClickDeleteItem {
            deleteTask(it.id)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun applySettings() {
        val category = shared.getString("category", "")
        val finished = shared.getBoolean("finished_visibility", true)

        if (category != "") {
            stdList.removeIf { it.category != category }
        }

        if (!finished) {
            stdList.removeIf { it.status == "finished" }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun sorList() {
        stdList = ArrayList(stdList.sortedWith(compareBy { it.end_date }))
        var finishedList: ArrayList<TaskModel> = ArrayList()

        for (i in stdList.indices) {
            if (stdList[i].status == "finished") {
                finishedList += stdList[i]
            }
        }

        stdList.removeIf { it.status == "finished" }
        stdList += finishedList
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        val item = menu?.findItem(R.id.search_action)
        val searchView = item?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                tempList.clear()
                val searchText = newText!!.lowercase(Locale.getDefault())
                if (searchText.isNotEmpty()) {
                    stdList.forEach {
                        if (it.title.lowercase(Locale.getDefault()).contains(searchText)) {
                            tempList.add(it)
                        }
                    }
                    adapter?.addItems(tempList)
                } else {
                    tempList.clear()
                    tempList.addAll(stdList)
                    adapter?.addItems(tempList)
                }
                return false
            }
        })
        menuInflater.inflate(R.menu.settings_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun getTasks() {
        val list = sqLiteHelper.getAllTasks()
        adapter?.addItems(list)
    }

    private fun deleteTask(id: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to delete this task?")
        builder.setCancelable(true)
        builder.setPositiveButton("Yes") { dialog, _ ->
            sqLiteHelper.deleteTask(id)
            getTasks()
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        val alert = builder.create()
        alert.show()
    }
}
