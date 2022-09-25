package com.example.todo.Presentation.View

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.Domain.TodoItem
import com.example.todo.R
import com.example.todo.Presentation.ViewModel.TodoViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainListFragment : Fragment() {

    private val adapter: TodoAdapter = TodoAdapter()
    private var todos: List<TodoItem>? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var addButton: FloatingActionButton
    private lateinit var add26Button: FloatingActionButton
    private lateinit var hideDoneButton: FloatingActionButton
    private var callbacks: Callbacks? = null

    private lateinit var emptyTextView: TextView

    interface Callbacks{
        fun onTodoSelected(todo: TodoItem)
    }

    val todoViewModel by lazy {
        ViewModelProvider(this)[TodoViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_list, container, false)

        recyclerView = view.findViewById(R.id.todo_recycler_view)
        addButton = view.findViewById(R.id.add_button)
        add26Button = view.findViewById(R.id.add26_button)
        hideDoneButton = view.findViewById(R.id.hide_done_button)
        emptyTextView = view.findViewById(R.id.empty_text_view)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

        val swipeToDeleteCallback = object : SwipeToDeleteCallback(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                todoViewModel.deleteTodo(adapter.getTodoAt(pos))
                Toast.makeText(context, "Удаление элемента произведено", Toast.LENGTH_SHORT).show()
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        add26Button.setOnClickListener {
            for (i in 0..26) {
                todoViewModel.addTodo(TodoItem(text = "number $i", visits = 1))
            }
        }

        addButton.setOnClickListener {
            val todo = TodoItem()
            callbacks?.onTodoSelected(todo)
        }

        hideDoneButton.setOnClickListener {
            if(adapter.items.isNotEmpty()){
                if(!todoViewModel.currentViewFlag) {
                    adapter.items = adapter.items.filter { !it.flag }
                    todoViewModel.currentViewFlag = true
                } else {
                    adapter.items = adapter.itemsCheck
                    todoViewModel.currentViewFlag = false
                }
                adapter.notifyDataSetChanged()
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        todoViewModel.todoListLiveData.observe(
            viewLifecycleOwner,
            Observer { todos ->
                todos.let {
                    Log.i("TodoListFragment", "Got todos ${todos.filter { it.visits!=0 }.size}")
                    if (todos.isEmpty()) {
                        emptyTextView.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    } else {
                        emptyTextView.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        this.todos = todos
                    }
                    updateUI(todos)
                    checkViewStatus()
                }
            }
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    private fun updateUI(todos: List<TodoItem>) {
        adapter.items = todos
        adapter.itemsCheck = todos
        adapter.notifyDataSetChanged()
    }

    private fun checkViewStatus(){
        if(todoViewModel.currentViewFlag) {
            adapter.items = adapter.items.filter { !it.flag }
        }
        adapter.notifyDataSetChanged()
    }

    private inner class TodoHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var item: TodoItem
        private val todoText: TextView = itemView.findViewById(R.id.todo_text_view)
        private val todoCheckBox: CheckBox = itemView.findViewById(R.id.todo_checkbox)

        init{
            itemView.setOnClickListener {
                callbacks?.onTodoSelected(this.item)
            }
        }

        fun bind(item: TodoItem) {
            this.item = item
            todoText.text = "${item.text} have id ${item.id} and visits ${item.visits}"
            todoCheckBox.isChecked = item.flag
            if(item.flag){
                todoText.paintFlags = todoText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else todoText.paintFlags = Paint.DEV_KERN_TEXT_FLAG
            todoCheckBox.setOnClickListener {
                item.flag = todoCheckBox.isChecked
                todoViewModel.updateTodo(item)
            }
        }

    }

    private inner class TodoAdapter : RecyclerView.Adapter<TodoHolder>() {
            var items = listOf<TodoItem>()
            var itemsCheck = listOf<TodoItem>()

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoHolder {
                val view = layoutInflater.inflate(R.layout.todo_item, parent, false)
                return TodoHolder(view)
            }

            override fun onBindViewHolder(holder: TodoHolder, position: Int) {
                val item = items[position]
                holder.bind(item)
            }

            override fun getItemCount() = items.size

            override fun getItemId(position: Int): Long {
                return position.toLong()
            }

            override fun getItemViewType(position: Int): Int {
                return position
            }

            fun getTodoAt(position: Int) : TodoItem{
                return items[position]
            }
        }
}