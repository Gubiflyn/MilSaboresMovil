package com.example.proyectologin005d.domain

import com.example.proyectologin005d.data.model.User

/** Utilidad opcional: calcula solo los descuentos sin tocar tu ViewModel. */
data class Discounts(val desc50: Int, val desc10: Int)

/**
 * Reglas:
 * - Si el usuario tiene 50% (edad >= 50) => aplica 50%.
 * - Si NO aplica 50%, pero tiene FELICES50 => 10%.
 * - No se acumulan.
 */
fun calcDiscounts(subtotal: Int, user: User?): Discounts {
    val d50 = if (user?.tiene50 == true) (subtotal * 0.5).toInt() else 0
    val d10 = if (d50 == 0 && user?.tiene10 == true) (subtotal * 0.1).toInt() else 0
    // 👇 usar los nombres de parámetros correctos del data class
    return Discounts(desc50 = d50, desc10 = d10)
}
