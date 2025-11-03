package com.example.proyectologin005d.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectologin005d.data.repository.AuthRepository
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {

    var uiState: RegisterUiState = RegisterUiState()
        private set

    fun onNombreChange(v: String) {
        uiState = uiState.copy(nombre = v, error = null)
    }

    fun onEmailChange(v: String) {
        uiState = uiState.copy(email = v, error = null)
    }

    fun onPasswordChange(v: String) {
        uiState = uiState.copy(password = v, error = null)
    }

    fun onConfirmPasswordChange(v: String) {
        uiState = uiState.copy(confirmPassword = v, error = null)
    }

    fun submit(onSuccess: () -> Unit) {
        val s = uiState
        if (s.nombre.isBlank()) return setError("Ingresa tu nombre")
        if (!s.email.contains("@")) return setError("Email no válido")
        if (s.password.length < 6) return setError("La contraseña debe tener al menos 6 caracteres")
        if (s.password != s.confirmPassword) return setError("Las contraseñas no coinciden")

        uiState = uiState.copy(isLoading = true, error = null)

        viewModelScope.launch {
            val ok: Boolean = repo.register(s.nombre, s.email, s.password)

            uiState = if (ok) {
                uiState.copy(isLoading = false, success = true)
            } else {
                uiState.copy(isLoading = false, success = false, error = "Error al registrarse")
            }

            if (uiState.success) onSuccess()
        }
    }

    private fun setError(msg: String) {
        uiState = uiState.copy(error = msg)
    }
}
