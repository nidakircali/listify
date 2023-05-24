package com.nidakircali.todoapp.dao

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.nidakircali.todoapp.model.TodoModel

class TodoDAO(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "todolist.db"
        private const val TABLE_NAME = "todolist"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_DETAIL = "detail"
        private const val COLUMN_IS_COMPLETED = "isCompleted"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery =
            "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_TITLE TEXT, $COLUMN_DETAIL TEXT, $COLUMN_IS_COMPLETED INTEGER);"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS todolist")
        onCreate(db)
    }


    // INSERT
    fun addTodoItem(title: String, detail: String, isCompleted: Boolean): Long {
        val values = ContentValues().apply {
            put("title", title)
            put("detail", detail)
            put("isCompleted", if (isCompleted) 1 else 0)
        }

        val db = this.writableDatabase
        val insertedId = db.insert("todolist", null, values)
        db.close()
        return insertedId
    }

    // LIST
    @SuppressLint("Range")
    fun getAllTodoItems(isAll: Boolean): List<TodoModel> {
        val todoItems = mutableListOf<TodoModel>()
        val selectQuery = if (isAll) {
            "SELECT * FROM todolist"
        } else {
            "SELECT * FROM todolist WHERE isCompleted = 0"
        }

        val db = this.readableDatabase
        val cursor: Cursor? = db.rawQuery(selectQuery, null)

        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    val id = it.getLong(it.getColumnIndex("id"))
                    val title = it.getString(it.getColumnIndex("title"))
                    val detail = it.getString(it.getColumnIndex("detail"))
                    val isCompleted = it.getInt(it.getColumnIndex("isCompleted")) == 1

                    val todoItem = TodoModel(id, title, detail, isCompleted)
                    todoItems.add(todoItem)

                } while (it.moveToNext())
            }
        }

        cursor?.close()
        db.close()

        return todoItems
    }


    // DELETE
    fun deleteTodoItem(id: Long): Int {
        val db = this.writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(id.toString())
        val deletedRows = db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
        return deletedRows
    }

    // UPDATE
    fun updateTodoItem(todoModel: TodoModel) : Int{
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("title", todoModel.title)
            put("detail", todoModel.detail)
            put("isCompleted", if (todoModel.isCompleted) 1 else 0)
        }
        val whereClause = "id = ?"
        val whereArgs = arrayOf(todoModel.id.toString())
        val updatedRows = db.update("todolist", values, whereClause, whereArgs)
        db.close()
        return updatedRows
    }

    fun updateTodoItemStatus(id: Long, isCompleted: Boolean): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("isCompleted", isCompleted)
        val whereClause = "id = ?"
        val whereArgs = arrayOf(id.toString())
        val updatedRows = db.update("todolist", values, whereClause, whereArgs)
        db.close()
        return updatedRows
    }


}