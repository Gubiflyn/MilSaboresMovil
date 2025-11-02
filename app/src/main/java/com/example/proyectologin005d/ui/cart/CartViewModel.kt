package com.example.proyectologin005d.ui.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectologin005d.data.model.Order
import com.example.proyectologin005d.data.model.OrderItem
import com.example.proyectologin005d.data.model.User
import com.example.proyectologin005d.data.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "CartVM"

// Modelo simple de línea y totales
data class LineaCarrito(val nombre: String, val precio: Int, val cantidad: Int)
data class Totales(val subtotal: Int, val descuento: Int, val total: Int)

class CartViewModel : ViewModel() {

    private val _items = MutableStateFlow<List<LineaCarrito>>(emptyList())
    val items = _items.asStateFlow()

    private val _totales = MutableStateFlow(Totales(0, 0, 0))
    val totales = _totales.asStateFlow()

    private var user: User? = null

    // Repo para guardar órdenes en Room
    private var orderRepo: OrderRepository? = null
    fun setOrderRepository(repo: OrderRepository) {
        orderRepo = repo
    }

    /** Se llama desde AppNav al iniciar/cerrar sesión */
    fun setUser(u: User?) {
        user = u
        actualizarTotales()
    }

    /** Agrega productos al carrito */
    fun add(nombre: String, precio: Int) {
        val lista = _items.value.toMutableList()
        val idx = lista.indexOfFirst { it.nombre == nombre }
        if (idx >= 0) {
            val l = lista[idx]
            lista[idx] = l.copy(cantidad = l.cantidad + 1)
        } else {
            lista += LineaCarrito(nombre, precio, 1)
        }
        _items.value = lista
        actualizarTotales()
    }

    /** Quita un producto o reduce cantidad */
    fun remove(nombre: String) {
        val lista = _items.value.toMutableList()
        val idx = lista.indexOfFirst { it.nombre == nombre }
        if (idx >= 0) {
            val l = lista[idx]
            val nueva = l.cantidad - 1
            if (nueva <= 0) lista.removeAt(idx) else lista[idx] = l.copy(cantidad = nueva)
        }
        _items.value = lista
        actualizarTotales()
    }

    /** Limpia el carrito */
    fun clear() {
        _items.value = emptyList()
        actualizarTotales()
    }

    /** Calcula totales y descuentos según usuario */
    private fun actualizarTotales() {
        val subtotal = _items.value.sumOf { it.precio * it.cantidad }
        val descuento = when {
            user?.tiene50 == true -> (subtotal * 0.50).toInt()
            user?.tiene10 == true -> (subtotal * 0.10).toInt()
            else -> 0
        }
        val total = (subtotal - descuento).coerceAtLeast(0)
        _totales.value = Totales(subtotal, descuento, total)
    }

    fun debugUserName() = user?.nombre ?: "null"
    fun debugTiene50() = (user?.tiene50 == true).toString()
    fun debugTiene10() = (user?.tiene10 == true).toString()

    /** Guarda compra en historial y luego limpia carrito */
    fun placeOrder(userEmail: String?, onSaved: (() -> Unit)? = null) {
        val email = userEmail ?: return
        val repo = orderRepo ?: return

        val itemsOrder = _items.value.mapIndexed { index, line ->
            OrderItem(
                id = 0L,          // autogenerado por Room
                orderId = 0L,     // se setea en DAO.insertOrderWithItems
                codigo = line.nombre, // si no tienes código, usa nombre
                nombre = line.nombre,
                precio = line.precio,
                cantidad = line.cantidad,
                subtotal = line.precio * line.cantidad
            )
        }

        val resumen = _totales.value
        val itemsCount = _items.value.sumOf { it.cantidad } // cantidad total de ítems

        // 👉 Guarda el TOTAL CON DESCUENTO
        val order = Order(
            id = 0L, // autogenerado
            userEmail = email,
            fechaMillis = System.currentTimeMillis(),
            total = resumen.total,          // <— total con descuento
            itemsCount = itemsCount         // <— suma de cantidades
        )

        viewModelScope.launch {
            try {
                Log.d(TAG, "placeOrder: email=$email lines=${itemsOrder.size} subtotal=${resumen.subtotal} desc=${resumen.descuento} total=${resumen.total}")
                orderRepo?.save(order, itemsOrder)
                clear()
                onSaved?.invoke()
            } catch (e: Exception) {
                Log.e(TAG, "placeOrder error: ${e.message}", e)
            }
        }
    }
}
