package com.example.proyectologin005d.ui.home


import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.example.proyectologin005d.data.model.Pastel
import com.example.proyectologin005d.data.repository.PastelRepository

class HomeViewModel(
    private val repository: PastelRepository = PastelRepository()
) : ViewModel() {

    val pasteles = MutableLiveData<List<Pastel>>()

    init {
        pasteles.value = repository.getAllPasteles()
    }
}
