package com.example.proyectologin005d.ui.cart

import androidx.lifecycle.ViewModel
import com.example.proyectologin005d.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

// Modelo simple de línea y totales
data class LineaCarrito(val nombre: String, val precio: Int, val cantidad: Int)
data class Totales(val subtotal: Int, val descuento: Int, val total: Int)

class CartViewModel : ViewModel() {

    private val _items = MutableStateFlow<List<LineaCarrito>>(emptyList())
    val items = _items.asStateFlow()

    private val _totales = MutableStateFlow(Totales(0, 0, 0))
    val totales = _totales.asStateFlow()

    private var user: User? = null

    /** Llamar cuando cambia el usuario (login / logout) */
    fun setUser(u: User?) {
        user = u
        actualizarTotales()
    }

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

    fun clear() {
        _items.value = emptyList()
        actualizarTotales()
    }

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
}
