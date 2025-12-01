package com.example.proyectologin005d.ui.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectologin005d.data.repository.LocationRepository
import com.example.proyectologin005d.data.source.remote.IpLocationDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LocationUiState(
    val isLoading: Boolean = false,
    val location: IpLocationDto? = null,
    val errorMessage: String? = null
)

class LocationViewModel : ViewModel() {

    private val repository = LocationRepository()

    private val _uiState = MutableStateFlow(LocationUiState())
    val uiState: StateFlow<LocationUiState> = _uiState

    fun loadLocation() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            repository.getMyLocation()
                .onSuccess { loc ->
                    _uiState.value = LocationUiState(
                        isLoading = false,
                        location = loc,
                        errorMessage = null
                    )
                }
                .onFailure { e ->
                    _uiState.value = LocationUiState(
                        isLoading = false,
                        location = null,
                        errorMessage = e.message ?: "Error al obtener la ubicación"
                    )
                }
        }
    }
}
