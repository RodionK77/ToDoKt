package com.example.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.todo.Domain.TodoItem
import com.example.todo.Presentation.View.MainListFragment
import com.example.todo.Presentation.View.TodoPageFragment

class MainActivity : AppCompatActivity(), MainListFragment.Callbacks, TodoPageFragment.Callbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if(currentFragment == null){
            val fragment = MainListFragment()
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit()
        }
    }

    override fun onTodoSelected(todo: TodoItem) {
        val bundle = Bundle()
        bundle.putSerializable("todoItem", todo)
        val fragment = TodoPageFragment()
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit()
    }

    override fun onExitSelected() {
        supportFragmentManager.popBackStack()
    }

}