package com.example.proyectologin005d.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pasteles")
data class Pastel(
    @PrimaryKey val codigo: String,
    val categoria: String,
    val nombre: String,
    val precio: Int,
    val imagen: String? = null,
    val descripcion: String? = null,
    val stock: Int = 0
)
