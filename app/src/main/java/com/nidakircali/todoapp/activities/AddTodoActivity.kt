package com.nidakircali.todoapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nidakircali.todoapp.R
import android.content.Intent
import com.nidakircali.todoapp.dao.TodoDAO
import com.nidakircali.todoapp.databinding.ActivityAddTodoBinding

class AddTodoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddTodoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTodoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        handleClicks()
    }

    private fun handleClicks() {
        binding.apply {
            cardAdd.setOnClickListener {
                addNote()

            }
        }
    }

    private fun addNote() {
        binding.apply {
            val title = binding.etTitle.text.toString().trim()
            val detail = binding.etDetail.text.toString().trim()

            val dao = TodoDAO(this@AddTodoActivity)
            dao.addTodoItem(
                title = title,
                detail = detail,
                isCompleted = false
            )
            val intent = Intent(this@AddTodoActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}