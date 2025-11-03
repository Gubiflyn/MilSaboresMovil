package com.example.proyectologin005d.ui.profile

data class ProfileUiState(
    val isGuest: Boolean = true,
    val nombre: String = "",
    val email: String = "",
    val edad: Int? = null,
    val codigoDescuento: String? = null,
    val tiene50: Boolean = false,
    val tiene10: Boolean = false
)
