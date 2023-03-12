package com.example.todo

import java.io.Serializable
import java.util.*


data class TaskModel(
    var id: Int = getAutoId(),
    var title: String = "",
    var description: String = "",
    var created_date: String = getNowDateAndTime(),
    var end_date: String = "",
    var status: String = "unfinished",
    var notification: String = "",
    var category: String = "",
    var attachment: String = ""
) {
    companion object {
        fun getAutoId(): Int {
            val random = Random()
            return random.nextInt(100)
        }

        fun getNowDateAndTime(): String {
            val cal = Calendar.getInstance(TimeZone.getTimeZone("Poland"))
            val day = String.format("%02d", cal.get(Calendar.DAY_OF_MONTH))
            val month = String.format("%02d", cal.get(Calendar.MONTH) + 1)
            val year = cal.get(Calendar.YEAR)
            val hour = String.format("%02d", cal.get(Calendar.HOUR_OF_DAY))
            val minute = String.format("%02d", cal.get(Calendar.MINUTE))
            return "$year-$month-$day $hour:$minute:00.000"
        }
    }
}
