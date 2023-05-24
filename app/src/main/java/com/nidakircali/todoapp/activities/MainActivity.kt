package com.nidakircali.todoapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.nidakircali.todoapp.R
import com.nidakircali.todoapp.adapter.TodoItemAdapter
import com.nidakircali.todoapp.dao.TodoDAO
import com.nidakircali.todoapp.databinding.ActivityMainBinding
import com.nidakircali.todoapp.model.TodoModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        handleClickEvents()

//        addTest()
//        deleteTest()
        getTodos(true)
    }
    private fun handleClickEvents() {
        binding.apply {
            fab.setOnClickListener {
                val intent = Intent(this@MainActivity, AddTodoActivity::class.java)
                startActivity(intent)
            }

            tvShowAll.setOnClickListener {
                getTodos(isAll = true)
                tvShowJustIncomplete.visibility = View.VISIBLE
                tvShowAll.visibility = View.GONE
            }
            tvShowJustIncomplete.setOnClickListener {
                getTodos(isAll = false)
                tvShowAll.visibility = View.VISIBLE
                tvShowJustIncomplete.visibility = View.GONE
            }
        }
    }

    private fun getTodos(isAll: Boolean) {
        val dao = TodoDAO(this)
        val todoItems = dao.getAllTodoItems(isAll = isAll)
        todoItems.forEach { item ->
            Log.e("TAG", "item : $item")
        }
        processTodos(todoItems = todoItems)
    }

    private fun processTodos(todoItems: List<TodoModel>) {
        binding.apply {
            if (todoItems.isNotEmpty()) {
                rvTodos.visibility = View.VISIBLE
                val adapter = TodoItemAdapter()
                rvTodos.adapter = null
                rvTodos.adapter = adapter
                rvTodos.layoutManager =
                    LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
                adapter.setList(todoItems as ArrayList<TodoModel>)

                adapter.setOnClickListenerCustom {
                    val intent = Intent(this@MainActivity, TodoDetailActivity::class.java)
                    intent.putExtra("TODO_ITEM", it)
                    startActivity(intent)
                }

                adapter.setOnCheckedChangeListenerCustom { itemId, isChecked ->
                    val dao = TodoDAO(this@MainActivity)
                    dao.updateTodoItemStatus(itemId, isChecked)
                }

            } else {
                rvTodos.visibility = View.GONE

            }
        }
    }

}