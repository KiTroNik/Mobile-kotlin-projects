package com.example.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_show.*
import kotlinx.android.synthetic.main.activity_update.*

class ShowActivity : AppCompatActivity() {
    private lateinit var task: TaskModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show)
        getIntentData()
    }


    private fun getIntentData() {
        task = TaskModel(
            id=intent.getStringExtra("id").toString().toInt(),
            title=intent.getStringExtra("title").toString(),
            description = intent.getStringExtra("description").toString(),
            created_date = intent.getStringExtra("start_date").toString(),
            end_date = intent.getStringExtra("end_date").toString(),
            status = intent.getStringExtra("status").toString(),
            notification = intent.getStringExtra("notification").toString(),
            category = intent.getStringExtra("category").toString(),
            attachment = intent.getStringExtra("attachment").toString(),
        )
        title_input3.setText(task.title)
        description_input3.setText(task.description)
        time_picker3.setText(task.end_date)
        autoCompleteNotificationOption3.setText(task.notification)
        category_input3.setText(task.category)
        if (task.status == "finished") {
            checkBox3.toggle()
        }
    }
}
