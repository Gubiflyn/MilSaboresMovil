package com.example.proyectologin005d.ui.history

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.proyectologin005d.data.database.PastelDatabase
import com.example.proyectologin005d.data.repository.OrderRepository
import com.example.proyectologin005d.ui.auth.AuthViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val repo: OrderRepository,
    private val authVm: AuthViewModel
) : ViewModel() {

    private val _ui = MutableStateFlow(HistoryUiState(loading = true))
    val ui: StateFlow<HistoryUiState> = _ui

    init {
        // Observa el usuario y luego el historial
        viewModelScope.launch {
            authVm.user.collectLatest { user ->
                val email = user?.email
                _ui.value = _ui.value.copy(userEmail = email, loading = true, error = null)
                if (email == null) {
                    _ui.value = _ui.value.copy(orders = emptyList(), loading = false)
                } else {
                    repo.observeOrders(email).collectLatest { list ->
                        _ui.value = _ui.value.copy(orders = list, loading = false)
                    }
                }
            }
        }
    }

    fun selectOrder(orderId: Long) {
        viewModelScope.launch {
            _ui.value = _ui.value.copy(selectedOrderId = orderId, loading = true)
            val items = repo.getItems(orderId)
            _ui.value = _ui.value.copy(selectedItems = items, loading = false)
        }
    }
}

/** Factory sencillo para crear HistoryViewModel desde AppNav */
class HistoryViewModelFactory(
    private val context: Context,
    private val authVm: AuthViewModel
) : ViewModelProvider.Factory {

    private val repo: OrderRepository by lazy {
        val db = PastelDatabase.getInstance(context)
        OrderRepository(db.orderDao())
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HistoryViewModel(repo, authVm) as T
    }
}
