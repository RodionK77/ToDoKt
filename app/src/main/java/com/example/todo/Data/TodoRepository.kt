package com.example.todo.Data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.todo.Domain.TodoItem
import java.util.concurrent.Executors

class TodoRepository private constructor(context: Context) {

    private val database: TodoDatabase = Room.databaseBuilder(
        context.applicationContext,
        TodoDatabase::class.java,
        "todo_database"
    ).build()

    private val todoDao = database.todoDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getTodos(): LiveData<List<TodoItem>> = todoDao.getTodos()
    fun getTodo(id: Long): LiveData<TodoItem?> = todoDao.getTodo(id)
    fun updateTodo(todo: TodoItem){
        executor.execute{
            todoDao.updateTodo(todo)
        }
    }
    fun addTodo(todo: TodoItem){
        executor.execute{
            todoDao.addTodo(todo)
        }
    }
    fun deleteTodo(todo: TodoItem){
        executor.execute{
            todoDao.deleteTodo(todo)
        }
    }


    companion object{
        private var INSTANCE: TodoRepository? = null

        fun initialize(context: Context){
            if(INSTANCE == null){
                INSTANCE = TodoRepository(context)
            }
        }

        fun get(): TodoRepository {
            return INSTANCE ?: throw IllegalStateException("TodoRepository must be initialized")
        }
    }
}