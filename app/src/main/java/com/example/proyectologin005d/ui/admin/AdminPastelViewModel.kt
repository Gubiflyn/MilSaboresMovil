package com.example.proyectologin005d.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectologin005d.data.model.Pastel
import com.example.proyectologin005d.data.repository.PastelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class AdminPastelUiState(
    val loading: Boolean = true,
    val lista: List<Pastel> = emptyList(),
    val error: String? = null
)

class AdminPastelViewModel(
    private val repository: PastelRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminPastelUiState())
    val uiState: StateFlow<AdminPastelUiState> = _uiState

    init {
        observarPasteles()
        refresh()
    }

    private fun observarPasteles() {
        viewModelScope.launch {
            repository.observeAll().collectLatest { lista ->
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    lista = lista,
                    error = null
                )
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(loading = true, error = null)
                repository.syncFromRemote()
                _uiState.value = _uiState.value.copy(loading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    error = e.message ?: "Error al sincronizar"
                )
            }
        }
    }

    // --------- CRUD ADMIN ---------

    fun crearPastel(pastel: Pastel) {
        viewModelScope.launch {
            repository.crearPastel(pastel)
            refresh()
        }
    }

    fun actualizarPastel(pastel: Pastel) {
        viewModelScope.launch {
            repository.actualizarPastel(pastel)
            refresh()
        }
    }

    fun eliminarPastel(codigo: String) {
        viewModelScope.launch {
            repository.eliminarPastel(codigo)
            refresh()
        }
    }
}