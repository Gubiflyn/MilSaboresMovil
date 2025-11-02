package com.example.proyectologin005d.data.repository

import android.util.Log
import com.example.proyectologin005d.data.dao.OrderDao
import com.example.proyectologin005d.data.model.Order
import com.example.proyectologin005d.data.model.OrderItem
import kotlinx.coroutines.flow.Flow

private const val TAG = "OrderRepo"

class OrderRepository(private val dao: OrderDao) {

    fun observeOrders(email: String): Flow<List<Order>> =
        dao.observeOrdersByUser(email)

    suspend fun getItems(orderId: Long): List<OrderItem> {
        val rel = dao.getOrderWithItems(orderId)
        if (rel != null) {
            Log.d(TAG, "getItems via relation: orderId=$orderId items=${rel.items.size}")
            return rel.items
        }
        val direct = dao.getItemsByOrder(orderId)
        Log.d(TAG, "getItems via direct table query: orderId=$orderId items=${direct.size}")
        return direct
    }

    suspend fun save(order: Order, items: List<OrderItem>): Long {
        val id = dao.insertOrderWithItems(order, items)
        Log.d(TAG, "save: inserted orderId=$id")
        return id
    }

    // ---- Limpieza ----
    suspend fun clearAllForUser(email: String) = dao.clearUserHistory(email)
    suspend fun clearInvalidForUser(email: String) = dao.clearInvalidByUser(email)
}
