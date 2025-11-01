package com.example.proyectologin005d.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectologin005d.data.model.Pastel
import com.example.proyectologin005d.data.repository.PastelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val loading: Boolean = true,
    val items: List<Pastel> = emptyList(),
    val filtroCategoria: String? = null,   // null = todos
    val filtrados: List<Pastel> = emptyList(),
    val error: String? = null
)

class HomeViewModel(private val repo: PastelRepository) : ViewModel() {
    private val _ui = MutableStateFlow(HomeUiState())
    val ui: StateFlow<HomeUiState> = _ui

    init {
        viewModelScope.launch {
            try {
                repo.seedIfEmpty()
                val all = repo.getAllPasteles()
                _ui.value = _ui.value.copy(
                    loading = false,
                    items = all,
                    filtrados = all
                )
            } catch (e: Exception) {
                _ui.value = _ui.value.copy(loading = false, error = e.message)
            }
        }
    }

    fun setCategoria(cat: String?) {
        val base = _ui.value.items
        val lista = if (cat.isNullOrBlank()) base
        else base.filter { it.categoria.equals(cat, ignoreCase = true) }
        _ui.value = _ui.value.copy(filtroCategoria = cat, filtrados = lista)
    }
}

