package com.example.proyectologin005d.ui.history

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.proyectologin005d.data.database.PastelDatabase
import com.example.proyectologin005d.data.model.Order
import com.example.proyectologin005d.data.model.OrderItem
import com.example.proyectologin005d.data.repository.OrderRepository
import com.example.proyectologin005d.session.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "HistoryVM"

data class OrderAggregate(val itemsCount: Int, val total: Int)

data class HistoryUiState(
    val loading: Boolean = true,
    val orders: List<Order> = emptyList(),
    val selectedOrderId: Long? = null,
    val selectedItems: List<OrderItem> = emptyList(),
    val aggregates: Map<Long, OrderAggregate> = emptyMap(),
    val error: String? = null,
    val userEmail: String? = null
)

class HistoryViewModel(
    application: Application,
    private val repo: OrderRepository
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        val email = SessionManager.currentUser?.email.orEmpty()
        _uiState.value = _uiState.value.copy(userEmail = email, loading = true)

        if (email.isNotBlank()) {
            viewModelScope.launch {
                repo.observeOrders(email).collectLatest { list ->
                    _uiState.value = _uiState.value.copy(orders = list, loading = false)
                    seedAggregatesFromOrder(list)
                    computeAggregatesFromRoomIfMissing(list)
                }
            }
        } else {
            _uiState.value = _uiState.value.copy(loading = false)
        }
    }

    private fun seedAggregatesFromOrder(orders: List<Order>) {
        val map = buildMap<Long, OrderAggregate> {
            for (o in orders) put(o.id, OrderAggregate(o.itemsCount, o.total))
        }
        _uiState.value = _uiState.value.copy(aggregates = map)
    }

    private fun computeAggregatesFromRoomIfMissing(orders: List<Order>) {
        viewModelScope.launch(Dispatchers.IO) {
            val current = _uiState.value.aggregates.toMutableMap()
            var changed = false
            for (o in orders) {
                if (!current.containsKey(o.id)) {
                    val items = runCatching { repo.getItems(o.id) }.getOrDefault(emptyList())
                    val count = items.sumOf { it.cantidad }
                    val total = items.sumOf { it.precio * it.cantidad }
                    current[o.id] = OrderAggregate(count, total)
                    changed = true
                }
            }
            if (changed) _uiState.value = _uiState.value.copy(aggregates = current)
        }
    }

    fun loadItemsFor(orderId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val items = runCatching { repo.getItems(orderId) }.getOrDefault(emptyList())
            _uiState.value = _uiState.value.copy(selectedOrderId = orderId, selectedItems = items)
        }
    }

    fun clearAllForCurrentUser() {
        val email = _uiState.value.userEmail ?: return
        viewModelScope.launch(Dispatchers.IO) {
            repo.clearAllForUser(email)
        }
    }

    fun clearInvalidForCurrentUser() {
        val email = _uiState.value.userEmail ?: return
        viewModelScope.launch(Dispatchers.IO) {
            repo.clearInvalidForUser(email)
        }
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun factory(app: Application): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val db = PastelDatabase.getInstance(app)
                val repo = OrderRepository(db.orderDao())
                return HistoryViewModel(app, repo) as T
            }
        }
    }
}
