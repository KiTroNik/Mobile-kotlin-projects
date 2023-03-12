package com.example.todo

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(context: Context, list: ArrayList<TaskModel>): RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    private var stdList: ArrayList<TaskModel> = list
    private var con = context
    private var onClickDeleteItem: ((TaskModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_row, parent, false)
        return TaskViewHolder(view)
    }

    fun addItems(items: ArrayList<TaskModel>) {
        this.stdList = items
        notifyDataSetChanged()
    }

    fun setOnClickDeleteItem(callback:(TaskModel)->Unit) {
        this.onClickDeleteItem = callback
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val std = stdList[position]
        holder.bindView(std)
        holder.editBtn.setOnClickListener {
            val intent = Intent(con, UpdateActivity::class.java)
            intent.putExtra("id", std.id.toString())
            intent.putExtra("title", std.title)
            intent.putExtra("description", std.description)
            intent.putExtra("start_date", std.created_date)
            intent.putExtra("end_date", std.end_date)
            intent.putExtra("notification", std.notification)
            intent.putExtra("category", std.category)
            intent.putExtra("status", std.status)
            intent.putExtra("attachment", std.attachment)
            con.startActivity(intent)
        }

        holder.deleteBtn.setOnClickListener {
            onClickDeleteItem?.invoke(std)
        }

        holder.viewBtn.setOnClickListener {
            val intent = Intent(con, ShowActivity::class.java)
            intent.putExtra("id", std.id.toString())
            intent.putExtra("title", std.title)
            intent.putExtra("description", std.description)
            intent.putExtra("start_date", std.created_date)
            intent.putExtra("end_date", std.end_date)
            intent.putExtra("notification", std.notification)
            intent.putExtra("category", std.category)
            intent.putExtra("status", std.status)
            intent.putExtra("attachment", std.attachment)
            con.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return stdList.size
    }

    class TaskViewHolder(var view: View): RecyclerView.ViewHolder(view) {
        private var title = view.findViewById<TextView>(R.id.text_task_title)
        private var date = view.findViewById<TextView>(R.id.text_task_date)
        private var isFinished = view.findViewById<TextView>(R.id.text_task_done)
        var editBtn: Button = view.findViewById<Button>(R.id.btn_edit_task)
        var deleteBtn: Button = view.findViewById<Button>(R.id.btn_delete)
        var viewBtn: Button = view.findViewById(R.id.btn_vie_task)

        fun bindView(std: TaskModel) {
            title.text = std.title.toString()
            date.text = std.end_date.toString()
            if (std.status == "finished") {
                isFinished.text = "DONE :)"
            } else {
                isFinished.text = ""
            }
        }
    }
}
