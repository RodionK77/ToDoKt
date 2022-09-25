package com.example.todo.Domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class TodoItem(
    var text: String = "",
    var flag: Boolean = false,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var visits: Int = 0
) : Serializable
