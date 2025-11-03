package com.example.proyectologin005d.session

import com.example.proyectologin005d.data.model.User


object SessionManager {
    @Volatile
    private var _currentUser: User? = null

    var currentUser: User?
        get() = _currentUser
        set(value) { _currentUser = value }

    fun logout() { _currentUser = null }
}
