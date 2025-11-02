package com.example.proyectologin005d.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_items")
data class OrderItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val orderId: Long,
    val codigo: String,
    val nombre: String,
    val precio: Int,
    val cantidad: Int,
    val subtotal: Int
)
