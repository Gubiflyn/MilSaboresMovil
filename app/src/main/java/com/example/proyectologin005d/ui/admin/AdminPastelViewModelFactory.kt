package com.example.proyectologin005d.ui.admin

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyectologin005d.data.database.PastelDatabase
import com.example.proyectologin005d.data.repository.PastelRepository
import com.example.proyectologin005d.data.source.remote.ApiClient

class AdminPastelViewModelFactory(
    private val appContext: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminPastelViewModel::class.java)) {

            // Room
            val db = PastelDatabase.getInstance(appContext)
            val dao = db.pastelDao()

            // API (misma forma que usas en el resto del proyecto)
            val api = ApiClient.pastelApi

            val repo = PastelRepository(dao, api)

            return AdminPastelViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class $modelClass")
    }
}
