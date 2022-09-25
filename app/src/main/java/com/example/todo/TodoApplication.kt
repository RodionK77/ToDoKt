package com.example.todo

import android.app.Application
import com.example.todo.Data.TodoRepository

class TodoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        TodoRepository.initialize(this)
    }
}