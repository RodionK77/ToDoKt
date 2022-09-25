package com.example.todo.Data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todo.Domain.TodoItem

@Database(entities = [TodoItem::class], version = 1, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {

    abstract fun todoDao(): TodoDAO
}