package com.example.proyectologin005d.data.repository

import com.example.proyectologin005d.data.dao.ProductoDao
import com.example.proyectologin005d.data.model.Producto
import kotlinx.coroutines.flow.Flow

//Actua como una intermedia entre BBDD(Dao) y la UI
// Encapsula las operaciones de acceso de datos
// con los productos proporciona
//un nivel de abstraccion para que las clases consuman los datos (UI)

class ProductoRepository(private val productoDao: ProductoDao){
    suspend fun insertarProducto(producto: Producto){
        productoDao.insertarProducto(producto)
    }

    fun obtenerProductos(): Flow<List<Producto>>{
        return productoDao.obtenerProductos()
    }


}// fin class