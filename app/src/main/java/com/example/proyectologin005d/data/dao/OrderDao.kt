package com.example.proyectologin005d.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.proyectologin005d.data.model.Order
import com.example.proyectologin005d.data.model.OrderItem
import kotlinx.coroutines.flow.Flow

data class OrderWithItems(
    @androidx.room.Embedded val order: Order,
    @androidx.room.Relation(
        parentColumn = "id",
        entityColumn = "orderId"
    )
    val items: List<OrderItem>
)

@Dao
interface OrderDao {

    // --- Lectura ---
    @Query("SELECT * FROM orders WHERE userEmail = :email ORDER BY fechaMillis DESC")
    fun observeOrdersByUser(email: String): Flow<List<Order>>

    @Transaction
    @Query("SELECT * FROM orders WHERE id = :orderId LIMIT 1")
    suspend fun getOrderWithItems(orderId: Long): OrderWithItems?

    // (fallback directo si tu tableName es 'order_items')
    @Query("SELECT * FROM order_items WHERE orderId = :orderId ORDER BY id ASC")
    suspend fun getItemsByOrder(orderId: Long): List<OrderItem>

    // --- Inserción ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<OrderItem>)

    @Transaction
    suspend fun insertOrderWithItems(order: Order, items: List<OrderItem>): Long {
        val id = insertOrder(order)
        if (items.isNotEmpty()) {
            insertItems(items.map { it.copy(orderId = id) })
        }
        return id
    }

    // --- Limpieza / borrado ---
    @Query("""
        DELETE FROM order_items 
        WHERE orderId IN (SELECT id FROM orders WHERE userEmail = :email)
    """)
    suspend fun deleteItemsByUser(email: String)

    @Query("DELETE FROM orders WHERE userEmail = :email")
    suspend fun deleteOrdersByUser(email: String)

    @Transaction
    suspend fun clearUserHistory(email: String) {
        deleteItemsByUser(email)
        deleteOrdersByUser(email)
    }

    @Query("""
        DELETE FROM order_items 
        WHERE orderId IN (
            SELECT id FROM orders 
            WHERE userEmail = :email AND total = 0
        )
    """)
    suspend fun deleteZeroItemsByUser(email: String)

    @Query("DELETE FROM orders WHERE userEmail = :email AND total = 0")
    suspend fun deleteZeroOrdersByUser(email: String)

    @Transaction
    suspend fun clearInvalidByUser(email: String) {
        deleteZeroItemsByUser(email)
        deleteZeroOrdersByUser(email)
    }
}
