package com.example.todo

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.activity_update.*
import java.util.*

class UpdateActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0

    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0
    var savedHour = 0
    var savedMinute = 0

    private lateinit var sqLiteHelper: SQLiteHelper
    private lateinit var task: TaskModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)
        sqLiteHelper = SQLiteHelper(this)
        val notificationOptions = resources.getStringArray(R.array.notification_options)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, notificationOptions)
        autoCompleteNotificationOption2.setAdapter(arrayAdapter)
        pickDate()
        getIntentData()

        btn_update.setOnClickListener {
            updateTask()
        }
    }

    private fun updateTask() {
        val title = title_input2.text.toString()
        val description = description_input2.text.toString()
        val endDate = time_picker2.text.toString()
        val notification = autoCompleteNotificationOption2.text.toString()
        val category = category_input2.text.toString()
        if (checkBoxCompleted.isChecked) {
            task.status = "finished"
        } else {
            task.status = "unfinished"
        }

        if (title.isEmpty() || description.isEmpty() ||
            endDate.isEmpty() || notification.isEmpty() || category.isEmpty()) {
            Toast.makeText(this, "Please enter required fields", Toast.LENGTH_SHORT).show()
        } else {
            task.title = title
            task.description = description
            task.end_date = endDate
            task.notification = notification
            task.category = category
            val status = sqLiteHelper.updateTask(task)
            if (status > -1) {
                Toast.makeText(this, "Task updated :)", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Something went wrong :(", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getDateTimeCalendar() {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("Poland"))
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR_OF_DAY)
        minute = cal.get(Calendar.MINUTE)
    }

    private fun pickDate() {
        time_picker2.setOnClickListener {
            getDateTimeCalendar()
            DatePickerDialog(this, this, year, month, day).show()
        }
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
        title_input2.setText(task.title)
        description_input2.setText(task.description)
        time_picker2.setText(task.end_date)
        autoCompleteNotificationOption2.setText(task.notification)
        category_input2.setText(task.category)
        if (task.status == "finished") {
            checkBoxCompleted.toggle()
        }
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        savedDay = p3
        savedMonth = p2 + 1
        savedYear = p1

        getDateTimeCalendar()

        TimePickerDialog(this, this, hour, minute, true).show()
    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        savedHour = p1
        savedMinute = p2

        val savedMonth = String.format("%02d", savedMonth)
        val savedDay = String.format("%02d", savedDay)
        val savedMinute = String.format("%02d", savedMinute)
        val savedHour = String.format("%02d", savedHour)

        time_picker2.setText("$savedYear-$savedMonth-$savedDay $savedHour:$savedMinute:00.000")
    }
}
