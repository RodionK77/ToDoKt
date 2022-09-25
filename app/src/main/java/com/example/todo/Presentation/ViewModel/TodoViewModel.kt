package com.example.todo.Presentation.ViewModel

import androidx.lifecycle.ViewModel
import com.example.todo.Domain.TodoItem
import com.example.todo.Data.TodoRepository

class TodoViewModel : ViewModel() {

    private val todoRepository = TodoRepository.get()
    val todoListLiveData = todoRepository.getTodos()
    var currentViewFlag = false

    fun addTodo(todo: TodoItem){
        todoRepository.addTodo(todo)
    }

    fun deleteTodo(todo: TodoItem){
        todoRepository.deleteTodo(todo)
    }

    fun updateTodo(todo: TodoItem){
        todoRepository.updateTodo(todo)
    }
}