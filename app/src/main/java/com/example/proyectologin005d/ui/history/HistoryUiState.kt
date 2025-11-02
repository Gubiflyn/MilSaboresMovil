package com.example.proyectologin005d.ui.history

import com.example.proyectologin005d.data.model.Order
import com.example.proyectologin005d.data.model.OrderItem

data class HistoryUiState(
    val loading: Boolean = true,
    val orders: List<Order> = emptyList(),
    val selectedOrderId: Long? = null,
    val selectedItems: List<OrderItem> = emptyList(),
    val error: String? = null,
    val userEmail: String? = null
)
