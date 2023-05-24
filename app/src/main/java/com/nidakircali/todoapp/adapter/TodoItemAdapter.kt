package com.nidakircali.todoapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nidakircali.todoapp.databinding.CardTodoItemBinding
import com.nidakircali.todoapp.model.TodoModel

class TodoItemAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var _list = ArrayList<TodoModel>()
    val list get() = _list.toList()
    private lateinit var context: Context


    fun setList(newList: ArrayList<TodoModel>) {
        _list.clear()
        _list.addAll(newList)

        notifyDataSetChanged()
    }

    class CardTodoItemViewHolder(val binding: CardTodoItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return CardTodoItemViewHolder(
            CardTodoItemBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        bindCardTodoItemViewHolder(holder as CardTodoItemViewHolder, position)
    }

    private fun bindCardTodoItemViewHolder(
        holder: CardTodoItemViewHolder,
        position: Int
    ) {
        holder.binding.apply {
            val item = list[position]

            checkbox.isChecked = item.isCompleted
            tvTitle.text = item.title

            cardTodo.setOnClickListener {
                onClickListenerCustom?.let {
                    it(item)
                }
            }

            checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
                onCheckedChangeListenerCustom?.let { listener ->
                    listener(item.id, isChecked)
                }
            }
        }
    }


    private var onClickListenerCustom: ((item: TodoModel) -> Unit)? = null
    fun setOnClickListenerCustom(f: ((item: TodoModel) -> Unit)) {
        onClickListenerCustom = f
    }

    private var onCheckedChangeListenerCustom: ((itemId: Long, isChecked: Boolean) -> Unit)? = null
    fun setOnCheckedChangeListenerCustom(f: ((itemId: Long, isChecked: Boolean) -> Unit)) {
        onCheckedChangeListenerCustom = f
    }
}