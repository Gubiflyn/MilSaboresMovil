package com.example.proyectologin005d.data.repository

import com.example.proyectologin005d.data.model.Credential
import com.example.proyectologin005d.data.model.User

class AuthRepository(
    private val validCredential: Credential = Credential.Admin
) {

    fun loginAdmin(username: String, password: String): Boolean =
        username == validCredential.username && password == validCredential.password


    private val usuarios = listOf(
        User(
            nombre = "Ana Mayor",
            email = "ana@example.com",
            edad = 55,
            password = "ana123"
        ),
        User(
            nombre = "Felipe Gutierrez Zamora",
            email = "faz@example.com",
            edad = 25,
            codigoDescuento = "FELICES50",
            password = "faz123"
        )
    )

    fun loginCliente(email: String, password: String): User? =
        usuarios.find { it.email.equals(email, true) && it.password == password }


    fun register(nombre: String, email: String, password: String): Boolean {
        val emailValido = email.contains("@")
        val passValido = password.length >= 6
        val nombreValido = nombre.isNotBlank()
        return emailValido && passValido && nombreValido
    }

    fun obtenerUsuarios(): List<User> = usuarios
}
