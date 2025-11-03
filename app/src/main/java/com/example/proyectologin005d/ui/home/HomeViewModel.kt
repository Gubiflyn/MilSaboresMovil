package com.example.proyectologin005d.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectologin005d.data.model.Pastel
import com.example.proyectologin005d.data.repository.PastelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class HomeUiState(
    val loading: Boolean = true,
    val items: List<Pastel> = emptyList(),
    val filtroCategoria: String? = null,
    val filtrados: List<Pastel> = emptyList(),
    val error: String? = null
)

class HomeViewModel(
    private val repo: PastelRepository
) : ViewModel() {

    private val _ui = MutableStateFlow(HomeUiState())
    val ui: StateFlow<HomeUiState> = _ui

    init {
        viewModelScope.launch {
            try {
                repo.seedIfEmpty()
            } catch (e: Exception) {
                _ui.value = _ui.value.copy(loading = false, error = e.message)
            }
        }
        viewModelScope.launch {
            repo.observeAll().collectLatest { list ->
                _ui.value = _ui.value.copy(
                    loading = false,
                    items = list,
                    filtrados = if (_ui.value.filtroCategoria.isNullOrBlank()) list
                    else list.filter { it.categoria.equals(_ui.value.filtroCategoria, ignoreCase = true) },
                    error = null
                )
            }
        }
    }

    fun setCategoria(cat: String?) {
        val base = _ui.value.items
        val lista = if (cat.isNullOrBlank()) base
        else base.filter { it.categoria.equals(cat, ignoreCase = true) }
        _ui.value = _ui.value.copy(filtroCategoria = cat, filtrados = lista)
    }

    fun refresh() {
    }
}
