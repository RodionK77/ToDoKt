package com.example.todo.Data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.todo.Domain.TodoItem

@Dao
interface TodoDAO {
    @Query("SELECT * FROM todoItem")
    fun getTodos(): LiveData<List<TodoItem>>
    @Query("SELECT * FROM todoItem WHERE id=(:id)")
    fun getTodo(id: Long): LiveData<TodoItem?>

    @Update
    fun updateTodo(todoItem: TodoItem)

    @Insert
    fun addTodo(todoItem: TodoItem)

    @Delete
    fun deleteTodo(todoItem: TodoItem)
}