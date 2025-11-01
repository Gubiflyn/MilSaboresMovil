package com.example.proyectologin005d.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectologin005d.data.model.User
import com.example.proyectologin005d.data.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val repo = AuthRepository()

    var uiState by mutableStateOf(LoginUiState())
        private set

    fun onUsernameChange(value: String) {
        uiState = uiState.copy(username = value)
    }

    fun onPasswordChange(value: String) {
        uiState = uiState.copy(password = value)
    }

    /**
     * Intenta login de cliente (email/clave) y, si no, admin (usuario/clave).
     * onOk se invoca con el User resuelto.
     */
    fun submit(onOk: (User) -> Unit) {
        val emailOrUser = uiState.username.trim()
        val pass = uiState.password

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)

            // Cliente (correo/clave)
            val cliente = repo.loginCliente(emailOrUser, pass)
            if (cliente != null) {
                uiState = uiState.copy(isLoading = false)
                onOk(cliente)
                return@launch
            }

            // Admin (usuario/clave)
            val adminOk = repo.loginAdmin(emailOrUser, pass)
            if (adminOk) {
                uiState = uiState.copy(isLoading = false)
                onOk(User(nombre = "Administrador", email = emailOrUser, edad = 0))
                return@launch
            }

            // Error
            uiState = uiState.copy(
                isLoading = false,
                error = "Credenciales inválidas"
            )
        }
    }
}
