package com.example.proyectologin005d.ui.jsonplaceholder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectologin005d.data.repository.JsonPlaceholderRepository
import com.example.proyectologin005d.data.source.remote.JsonPlaceholderPostDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class JsonPlaceholderUiState(
    val isLoading: Boolean = false,
    val posts: List<JsonPlaceholderPostDto> = emptyList(),
    val errorMessage: String? = null,
    val isActionRunning: Boolean = false,
    val actionMessage: String? = null
)

class JsonPlaceholderViewModel(
    private val repository: JsonPlaceholderRepository = JsonPlaceholderRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(JsonPlaceholderUiState(isLoading = true))
    val uiState: StateFlow<JsonPlaceholderUiState> = _uiState

    init {
        loadPosts()
    }

    fun loadPosts() {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = null,
            actionMessage = null
        )
        viewModelScope.launch {
            val result = repository.getPosts()
            _uiState.value = result.fold(
                onSuccess = { posts ->
                    _uiState.value.copy(
                        isLoading = false,
                        posts = posts,
                        errorMessage = null
                    )
                },
                onFailure = { error ->
                    _uiState.value.copy(
                        isLoading = false,
                        posts = emptyList(),
                        errorMessage = error.message ?: "Error desconocido"
                    )
                }
            )
        }
    }

    fun createDemoPost() {
        _uiState.value = _uiState.value.copy(
            isActionRunning = true,
            actionMessage = null
        )
        viewModelScope.launch {
            val result = repository.createDemoPost()
            _uiState.value = result.fold(
                onSuccess = { post ->
                    _uiState.value.copy(
                        isActionRunning = false,
                        actionMessage = "POST de prueba enviado (id devuelto: ${post.id})."
                    )
                },
                onFailure = { error ->
                    _uiState.value.copy(
                        isActionRunning = false,
                        actionMessage = "Error en POST de prueba: ${error.message}"
                    )
                }
            )
        }
    }

    fun updateDemoPost(id: Int = 1) {
        _uiState.value = _uiState.value.copy(
            isActionRunning = true,
            actionMessage = null
        )
        viewModelScope.launch {
            val result = repository.updateDemoPost(id)
            _uiState.value = result.fold(
                onSuccess = { post ->
                    _uiState.value.copy(
                        isActionRunning = false,
                        actionMessage = "PUT de prueba al id=$id enviado. Título devuelto: \"${post.title}\""
                    )
                },
                onFailure = { error ->
                    _uiState.value.copy(
                        isActionRunning = false,
                        actionMessage = "Error en PUT de prueba: ${error.message}"
                    )
                }
            )
        }
    }

    fun deleteDemoPost(id: Int = 1) {
        _uiState.value = _uiState.value.copy(
            isActionRunning = true,
            actionMessage = null
        )
        viewModelScope.launch {
            val result = repository.deleteDemoPost(id)
            _uiState.value = result.fold(
                onSuccess = {
                    _uiState.value.copy(
                        isActionRunning = false,
                        actionMessage = "DELETE de prueba al id=$id enviado correctamente."
                    )
                },
                onFailure = { error ->
                    _uiState.value.copy(
                        isActionRunning = false,
                        actionMessage = "Error en DELETE de prueba: ${error.message}"
                    )
                }
            )
        }
    }
}
