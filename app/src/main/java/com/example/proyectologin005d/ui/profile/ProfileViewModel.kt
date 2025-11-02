package com.example.proyectologin005d.ui.profile

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.proyectologin005d.data.model.User

class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    /**
     * user == null  -> Sesión de invitado
     * user != null  -> Pobla con los campos reales de tu modelo
     */
    fun setFromUser(user: User?) {
        if (user == null) {
            _uiState.value = ProfileUiState(isGuest = true)
        } else {
            _uiState.value = ProfileUiState(
                isGuest = false,
                nombre = user.nombre,
                email = user.email,
                edad = user.edad,
                codigoDescuento = user.codigoDescuento,
                tiene50 = user.tiene50, // (edad >= 50)
                tiene10 = user.tiene10  // (codigoDescuento == "FELICES50")
            )
        }
    }
}
