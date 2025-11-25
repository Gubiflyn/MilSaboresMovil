package com.example.proyectologin005d.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyectologin005d.data.database.PastelDatabase
import com.example.proyectologin005d.data.repository.PastelRepository
import com.example.proyectologin005d.data.source.remote.ApiClient

class HomeViewModelFactory(private val appContext: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = PastelDatabase.getInstance(appContext)
        val dao = db.pastelDao()
        val api = ApiClient.pastelApi
        val repo = PastelRepository(dao, api)
        return HomeViewModel(repo) as T
    }
}
