package com.example.proyectologin005d.data.repository

import com.example.proyectologin005d.data.model.Credential
import com.example.proyectologin005d.data.model.User

class AuthRepository(
    private val validCredential: Credential = Credential.Admin
) {
    // --- ADMIN (usuario/clave) ---
    fun loginAdmin(username: String, password: String): Boolean =
        username == validCredential.username && password == validCredential.password

    // --- CLIENTES (correo/clave) ---
    private val usuarios = listOf(
        User(
            nombre = "Ana Mayor",
            email = "ana@example.com",
            edad = 55,               // 50% por edad
            password = "ana123"
        ),
        User(
            nombre = "Ben Código",
            email = "ben@example.com",
            edad = 25,
            codigoDescuento = "FELICES50", // 10%
            password = "ben123"
        )
    )

    fun loginCliente(email: String, password: String): User? =
        usuarios.find { it.email.equals(email, true) && it.password == password }

    // (si la pantalla de registro aún existe)
    fun register(nombre: String, email: String, password: String): Boolean {
        val emailValido = email.contains("@")
        val passValido = password.length >= 6
        val nombreValido = nombre.isNotBlank()
        return emailValido && passValido && nombreValido
    }

    fun obtenerUsuarios(): List<User> = usuarios
}
