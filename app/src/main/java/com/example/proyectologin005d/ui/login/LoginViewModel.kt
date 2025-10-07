package com.example.proyectologin005d.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.proyectologin005d.data.repository.AuthRepository

class LoginViewModel (
    private val repo: AuthRepository = AuthRepository()
): ViewModel()
{// inicio view

    var uiState by mutableStateOf(LoginUiState())
        private set

    fun onUsernameChange(value:String){
        uiState =uiState.copy(username =value, error=null)
    }

    fun onpasswordChange(value:String){
        uiState =uiState.copy(password=value, error=null)
    }

    // funciones de actualizacion que se llaman desde el TextField


    fun submit(onSuccess:(String) -> Unit){
        uiState = uiState.copy(isLoading=true, error =null)
        val oK =repo.login(uiState.username.trim(), uiState.password)

        // Resultado

        uiState =uiState.copy(isLoading=false)

        if(oK) onSuccess(uiState.username.trim())
        else uiState =uiState.copy(error="Credenciales Invalidas")
    }



}// termino view
