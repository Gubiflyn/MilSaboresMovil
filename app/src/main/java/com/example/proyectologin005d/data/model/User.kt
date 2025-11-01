package com.example.proyectologin005d.data.model

data class User(
    val nombre: String,
    val email: String,
    val edad: Int,
    val codigoDescuento: String? = null,
    val password: String? = null
) {
    val tiene50: Boolean get() = edad >= 50
    val tiene10: Boolean get() = codigoDescuento == "FELICES50"
}
