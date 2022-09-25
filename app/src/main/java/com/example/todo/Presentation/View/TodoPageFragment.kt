package com.example.todo.Presentation.View

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.todo.Domain.TodoItem
import com.example.todo.Presentation.ViewModel.TodoViewModel
import com.example.todo.R

class TodoPageFragment : Fragment() {

    private lateinit var todo: TodoItem
    private lateinit var exitButton: ImageView
    private lateinit var saveButton: Button
    private var callbacks: Callbacks? = null
    private lateinit var spinner: AutoCompleteTextView
    private lateinit var todoEditText: TextView

    private val todoViewModel by lazy {
        ViewModelProvider(this)[TodoViewModel::class.java]
    }

    interface Callbacks{
        fun onExitSelected()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_todo_page, container, false)

        exitButton = view.findViewById(R.id.toolbar_exit_image_view)
        saveButton = view.findViewById(R.id.toolbar_save_button)
        todoEditText = view.findViewById(R.id.todo_edit_text)
        spinner = view.findViewById(R.id.spinner)
        spinner.setAdapter(ArrayAdapter(requireContext(),
            R.layout.list_item, resources.getTextArray(R.array.importance_labels) ))
        spinner.setText(spinner.adapter.getItem(0).toString(), false);

        todo = arguments?.getSerializable("todoItem") as TodoItem

        exitButton.setOnClickListener {
            callbacks?.onExitSelected()
        }

        todoEditText.text = todo.text

        saveButton.setOnClickListener {
            this.todo.text = todoEditText.text.toString()
            if(todo.visits == 0){
                todoViewModel.addTodo(this.todo)
            }
            this.todo.visits++
            todoViewModel.updateTodo(this.todo)
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }
}