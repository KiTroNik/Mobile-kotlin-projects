package com.example.todo

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import java.lang.Exception

class SQLiteHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "todo.db"
        private const val TABLE_NAME = "tasks"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_DESC = "description"
        private const val COLUMN_CREATED = "created_date"
        private const val COLUMN_END = "end_date"
        private const val COLUMN_STATUS = "status"
        private const val COLUMN_NOTIFICATION = "notification"
        private const val COLUMN_CATEGORY = "category"
        private const val COLUMN_ATTACHMENT = "attachment"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val query = ("CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DESC + " TEXT, " +
                COLUMN_CREATED + " TEXT, " +
                COLUMN_END + " TEXT, " +
                COLUMN_STATUS + " TEXT, " +
                COLUMN_NOTIFICATION + " TEXT, " +
                COLUMN_CATEGORY + " TEXT, " +
                COLUMN_ATTACHMENT + " TEXT);"
                )
        p0?.execSQL(query)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME;")
        onCreate(p0)
    }

    fun insertTask(task: TaskModel): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(COLUMN_TITLE, task.title)
        contentValues.put(COLUMN_DESC, task.description)
        contentValues.put(COLUMN_CREATED, task.created_date)
        contentValues.put(COLUMN_END, task.end_date)
        contentValues.put(COLUMN_STATUS, task.status)
        contentValues.put(COLUMN_NOTIFICATION, task.notification)
        contentValues.put(COLUMN_CATEGORY, task.category)
        contentValues.put(COLUMN_ATTACHMENT, task.attachment)

        val result = db.insert(TABLE_NAME, null, contentValues)
        db.close()
        return result
    }

    @SuppressLint("Range")
    fun getAllTasks(): ArrayList<TaskModel> {
        val taskList: ArrayList<TaskModel> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase

        val cursor: Cursor?

        try {
           cursor = db.rawQuery(selectQuery, null)
        } catch(e: Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var title: String
        var description: String
        var created_date: String
        var end_date: String
        var status: String
        var notification: String
        var category: String
        var attachment: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
                title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE))
                description = cursor.getString(cursor.getColumnIndex(COLUMN_DESC))
                created_date = cursor.getString(cursor.getColumnIndex(COLUMN_CREATED))
                end_date = cursor.getString(cursor.getColumnIndex(COLUMN_END))
                status = cursor.getString(cursor.getColumnIndex(COLUMN_STATUS))
                notification = cursor.getString(cursor.getColumnIndex(COLUMN_NOTIFICATION))
                category = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY))
                attachment = cursor.getString(cursor.getColumnIndex(COLUMN_ATTACHMENT))

                val std = TaskModel(id, title, description, created_date, end_date, status, notification, category, attachment)
                taskList.add(std)
            } while(cursor.moveToNext())
        }
        return taskList
    }

    fun updateTask(task: TaskModel): Int {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_ID, task.id)
        contentValues.put(COLUMN_TITLE, task.title)
        contentValues.put(COLUMN_DESC, task.description)
        contentValues.put(COLUMN_CREATED, task.created_date)
        contentValues.put(COLUMN_END, task.end_date)
        contentValues.put(COLUMN_STATUS, task.status)
        contentValues.put(COLUMN_NOTIFICATION, task.notification)
        contentValues.put(COLUMN_CATEGORY, task.category)
        contentValues.put(COLUMN_ATTACHMENT, task.attachment)
        val status = db.update(TABLE_NAME, contentValues, "id=" + task.id, null)
        db.close()
        return status
    }

    fun deleteTask(id: Int): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_ID, id)
        val result = db.delete(TABLE_NAME, "id=$id", null)
        db.close()
        return result
    }
}
