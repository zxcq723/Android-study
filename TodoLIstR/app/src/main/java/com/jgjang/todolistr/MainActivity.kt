package com.jgjang.todolistr

import android.app.Activity
import android.graphics.Paint
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.PointerIcon
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputBinding
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jgjang.todolistr.databinding.ActivityMainBinding
import com.jgjang.todolistr.databinding.ItemTodoBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val data = arrayListOf<Todo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        data.add(Todo("숙제"))
        data.add(Todo("청소"))
        data.add(Todo("빨래", true))

        binding.recyclerView.apply{
           layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = TodoAdapter(data,
                onClickDeleteIcon = {
                    deleteTodo(it)
                },
                onCLickItem = {
                    toggleTodo(it)
                }
            )
        }
        binding.addBotton.setOnClickListener {
            addTodo()
        }
    }

    private fun toggleTodo(todo: Todo) {
        todo.isDone = !todo.isDone
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun addTodo() {
        val todo = Todo(binding.editText.text.toString(), false)
        data.add(todo)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun deleteTodo(todo: Todo) {
        data.remove(todo)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }
}

data class Todo(
        val text: String,
        var isDone: Boolean = false
)

class TodoAdapter(private val mydataSet: List<Todo>,
                  val onClickDeleteIcon: (todo: Todo) -> Unit,
                    val onCLickItem: (todo: Todo) -> Unit

) :
        RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    class TodoViewHolder(val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_todo, parent, false)
        return TodoViewHolder(ItemTodoBinding.bind(view))

    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todo = mydataSet[position]
        holder.binding.todoText.text = mydataSet[position].text

        if (todo.isDone) {
            holder.binding.todoText.apply {
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                setTypeface(null, Typeface.ITALIC)
            }
        }else {
            holder.binding.todoText.apply {
                paintFlags = 0
                setTypeface(null, Typeface.NORMAL)
            }
        }


        holder.binding.deleteImageView.setOnClickListener {
            onClickDeleteIcon.invoke(todo)
        }

        holder.binding.root.setOnClickListener{
                onCLickItem.invoke(todo)
        }

    }

    override fun getItemCount(): Int = mydataSet.size

}