package com.example.proyectologin005d.data.repository

import com.example.proyectologin005d.data.dao.OrderDao
import com.example.proyectologin005d.data.model.Order
import com.example.proyectologin005d.data.model.OrderItem
import kotlinx.coroutines.flow.Flow

class OrderRepository(private val dao: OrderDao) {

    fun observeOrders(email: String): Flow<List<Order>> =
        dao.observeOrdersByUser(email)

    suspend fun getItems(orderId: Long): List<OrderItem> =
        dao.getItemsByOrder(orderId)

    suspend fun save(order: Order, items: List<OrderItem>) {
        val newId = dao.insertOrder(order)
        if (items.isNotEmpty()) {
            val withOrderId = items.map { it.copy(orderId = newId) }
            dao.insertItems(withOrderId)
        }
    }
}
