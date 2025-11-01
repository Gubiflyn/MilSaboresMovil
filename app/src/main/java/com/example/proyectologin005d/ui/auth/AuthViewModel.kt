package com.example.proyectologin005d.ui.auth

import androidx.lifecycle.ViewModel
import com.example.proyectologin005d.data.model.User
import com.example.proyectologin005d.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel : ViewModel() {
    private val repo = AuthRepository()

    private val _user = MutableStateFlow<User?>(null)
    /** Usuario autenticado (null si es admin o invitado). */
    val user = _user.asStateFlow()

    /**
     * Retorna true si las credenciales son válidas.
     * - Admin: username=admin, password=123 (no tiene descuentos)
     * - Cliente: usa correo/clave y deja el User en `user`.
     */
    fun login(emailOrUser: String, password: String): Boolean {
        // Admin clásico
        if (repo.loginAdmin(emailOrUser, password)) {
            _user.value = null
            return true
        }
        // Cliente (con beneficios)
        val u = repo.loginCliente(emailOrUser, password) ?: return false
        _user.value = u
        return true
    }

    fun logout() { _user.value = null }
}
