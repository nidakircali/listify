package com.nidakircali.todoapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.nidakircali.todoapp.R
import com.nidakircali.todoapp.dao.TodoDAO
import com.nidakircali.todoapp.databinding.ActivityTodoDetailBinding
import com.nidakircali.todoapp.model.TodoModel

class TodoDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTodoDetailBinding

    private var item: TodoModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodoDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        getItem()
        handleClickEvents()

    }

    private fun getItem() {
        val todoItem = intent.getSerializableExtra("TODO_ITEM") as? TodoModel
        if (todoItem != null) {
            processItem(todoItem)
        }

    }

    private fun processItem(todoItem: TodoModel) {
        binding.apply {
            item = todoItem
            etTitle.setText(todoItem.title)
            etDetail.setText(todoItem.detail)
        }
    }

    private fun handleClickEvents() {
        binding.apply {
            cardDelete.setOnClickListener {
                if (item != null) {
                    deleteTest(id = item!!.id)
                } else {

                }
            }

            cardUpdate.setOnClickListener {
                item?.let { item ->
                    val dao = TodoDAO(this@TodoDetailActivity)
                    val title = etTitle.text.toString().trim()
                    val detail = etDetail.text.toString().trim()
                    val updatedItem = TodoModel(item.id,title,detail, item.isCompleted)
                    val updatedRows = dao.updateTodoItem(updatedItem)
                    Log.e("TAG", "updatedRows : $updatedRows")

                    Log.e("TAG", "GÜNCELLENDİ!!")
                    Toast.makeText(this@TodoDetailActivity, "Updated", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@TodoDetailActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }
        }
    }

    private fun deleteTest(id: Long) {
        val dao = TodoDAO(this)
        val deletedRows = dao.deleteTodoItem(id)
        Log.e("TAG", "deletedRows : $deletedRows")

        Log.e("TAG", "SİLİNDİ!!")
        Toast.makeText(this@TodoDetailActivity, "Deleted", Toast.LENGTH_SHORT).show()
        val intent = Intent(this@TodoDetailActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}