package com.example.proyectologin005d.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.proyectologin005d.data.model.Order
import com.example.proyectologin005d.data.model.OrderItem
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {

    @Query("SELECT * FROM orders WHERE userEmail = :email ORDER BY fechaMillis DESC")
    fun observeOrdersByUser(email: String): Flow<List<Order>>

    @Query("SELECT * FROM order_items WHERE orderId = :orderId ORDER BY id ASC")
    suspend fun getItemsByOrder(orderId: Long): List<OrderItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<OrderItem>)
}
