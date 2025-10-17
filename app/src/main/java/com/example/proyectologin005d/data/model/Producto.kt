package com.example.proyectologin005d.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class Producto(
    @PrimaryKey val id:Int =0,
    val nombre:String,
    val precio:String,
    val cantidad:String,
    val direccion:String,
    val conPapas:Boolean,
    val agrandarBebida: Boolean
)