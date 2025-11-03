package com.example.proyectologin005d.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.proyectologin005d.data.model.User
import com.example.proyectologin005d.data.repository.AuthRepository
import com.example.proyectologin005d.session.SessionManager



class LoginViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {

    var uiState by mutableStateOf(LoginUiState())
        private set

    fun onUsernameChange(v: String) { uiState = uiState.copy(username = v) }
    fun onPasswordChange(v: String) { uiState = uiState.copy(password = v) }

    fun submit(onSuccess: (User) -> Unit) {
        uiState = uiState.copy(isLoading = true, error = null)
        val email = uiState.username.trim()
        val pass  = uiState.password

        if (repo.loginAdmin(email, pass)) {
            uiState = uiState.copy(isLoading = false)
            onSuccess(
                User(nombre = "Administrador", email = email, edad = 0, codigoDescuento = null, password = null)
            )
            return
        }

        val user = repo.loginCliente(email, pass)
        if (user != null) {
            SessionManager.currentUser = user
            uiState = uiState.copy(isLoading = false)
            onSuccess(user)
        } else {
            uiState = uiState.copy(
                isLoading = false,
                error = "Credenciales inválidas"
            )
        }
    }
}
