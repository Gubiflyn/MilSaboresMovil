package com.example.proyectologin005d.data.repository

import com.example.proyectologin005d.data.model.Credential

// Repositorio que gestiona autenticación (Login y Registro)
class AuthRepository(
    private val validCredential: Credential = Credential.Admin
) {

    // ===========================
    // LOGIN
    // ===========================
    fun login(username: String, password: String): Boolean {
        // Compara los datos ingresados con las credenciales válidas (por defecto: Admin)
        return username == validCredential.username && password == validCredential.password
    }

    // ===========================
    // REGISTRO (nuevo método)
    // ===========================
    fun register(nombre: String, email: String, password: String): Boolean {
        /*
         * Como este proyecto no tiene base de datos todavía, este método
         * solo simula un registro válido verificando que:
         * - El email contenga '@'
         * - La contraseña tenga al menos 6 caracteres
         * - El nombre no esté vacío
         */
        val emailValido = email.contains("@")
        val passValido = password.length >= 6
        val nombreValido = nombre.isNotBlank()

        return emailValido && passValido && nombreValido
    }
}
