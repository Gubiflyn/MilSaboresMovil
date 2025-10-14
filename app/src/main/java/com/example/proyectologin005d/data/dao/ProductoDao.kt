package com.example.proyectologin005d.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.proyectologin005d.data.model.Producto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao{

    @Insert
    suspend fun insertarProducto(producto: Producto)

    @Query("SELECT * FROM productos")
    fun obtenerProductos(): Flow<List<Producto>>

}