package com.example.todo

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.preference.PreferenceManager
import com.example.todo.databinding.ActivityAddBinding
import kotlinx.android.synthetic.main.activity_add.*
import java.util.*

class AddActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

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

    private lateinit var binding: ActivityAddBinding
    private lateinit var sqLiteHelper: SQLiteHelper
    lateinit var shared: SharedPreferences

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createNotificationChannel()

        shared = PreferenceManager.getDefaultSharedPreferences(this)
        sqLiteHelper = SQLiteHelper(this)
        val notificationOptions = resources.getStringArray(R.array.notification_options)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, notificationOptions)
        autoCompleteNotificationOption.setAdapter(arrayAdapter)
        pickDate()

        btn_save.setOnClickListener {
            addTask()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val name = "Notif Channel"
        val desc = "A Description of the Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun addTask() {
        val title = title_input.text.toString()
        val description = description_input.text.toString()
        val endDate = time_picker.text.toString()
        val notification = autoCompleteNotificationOption.text.toString()
        val category = category_input.text.toString()

        if (title.isEmpty() || description.isEmpty() ||
            endDate.isEmpty() || notification.isEmpty() || category.isEmpty()
        ) {
            Toast.makeText(this, "Please enter required fields", Toast.LENGTH_SHORT).show()
        } else {
            val std = TaskModel(
                title = title, description = description,
                end_date = endDate, notification = notification, category = category
            )
            val status = sqLiteHelper.insertTask(std)
            if (status > -1) {
                Toast.makeText(this, "Task added :)", Toast.LENGTH_SHORT).show()
                if (binding.autoCompleteNotificationOption.text.toString() == "Enable") {
                    scheduleNotification()
                }
                clearEditText()
            } else {
                Toast.makeText(this, "Something went wrong :(", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun scheduleNotification() {
        val intent = Intent(applicationContext, Notification::class.java)
        val title = "Task Reminder!!"
        val message = binding.titleInput.text.toString()
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance()
        calendar.set(savedYear, savedMonth - 1, savedDay, savedHour, savedMinute)

        val settMinutes = shared.getString("minutes", "")
        if (settMinutes != "") {
            try {
                val minutesToSubstract = settMinutes?.toInt()
                if (minutesToSubstract != null) {
                    if (minutesToSubstract < 0 || minutesToSubstract > 30) {
                        AlertDialog.Builder(this)
                            .setTitle("Error")
                            .setMessage(
                                "Minutes settings must be in range of 0 and 30"
                            )
                            .setPositiveButton("Okay") { _, _ -> }
                            .show()
                        return
                    } else {
                        calendar.add(Calendar.MINUTE, -minutesToSubstract);
                    }
                }
            } catch (e: Exception) {
                AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage(
                        "Minutes settings must be integer"
                    )
                    .setPositiveButton("Okay") { _, _ -> }
                    .show()
                return
            }
        }

        val currentCal = Calendar.getInstance()

        if (currentCal > calendar) {
            AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(
                    "Date is in the past"
                )
                .setPositiveButton("Okay") { _, _ -> }
                .show()
            return
        }

        val timeMil = calendar.timeInMillis
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            timeMil,
            pendingIntent
        )
        showAlert(timeMil, title, message)
    }

    private fun showAlert(time: Long, title: String, message: String) {
        val date = Date(time)
        val dateFormat = DateFormat.getLongDateFormat(applicationContext)
        val timeFormat = DateFormat.getTimeFormat(applicationContext)

        AlertDialog.Builder(this)
            .setTitle("Notification Scheduled")
            .setMessage(
                "Title: " + title + "\nMessage: " + message + "\nAt: " + dateFormat.format(date) + " " + timeFormat.format(
                    time
                )
            )
            .setPositiveButton("Okay") { _, _ -> }
            .show()
    }

    private fun clearEditText() {
        title_input.setText("")
        description_input.setText("")
        time_picker.setText("")
        category_input.setText("")
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
        time_picker.setOnClickListener {
            getDateTimeCalendar()
            DatePickerDialog(this, this, year, month, day).show()
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

        time_picker.setText("$savedYear-$savedMonth-$savedDay $savedHour:$savedMinute:00.000")
    }
}
