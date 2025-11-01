package com.example.proyectologin005d.ui.auth

import androidx.lifecycle.ViewModel
import com.example.proyectologin005d.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    fun setUser(u: User?) {
        _user.value = u
    }

    fun logout() {
        _user.value = null
    }
}
