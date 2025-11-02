package com.example.proyectologin005d.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val userEmail: String,
    val fechaMillis: Long,
    val total: Int,
    val itemsCount: Int
)
