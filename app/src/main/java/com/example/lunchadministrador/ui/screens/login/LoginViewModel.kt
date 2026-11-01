package com.example.lunchadministrador.ui.screens.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lunchadministrador.data.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val auth: AuthRepository) : ViewModel() {
    var correo by mutableStateOf("")
    var clave by mutableStateOf("")
    var error by mutableStateOf<String?>(null)
    var cargando by mutableStateOf(false)

    fun ingresar(onOk: () -> Unit) {
        viewModelScope.launch {
            cargando = true
            auth.iniciarSesion(correo, clave)
                .onSuccess { error = null; onOk() }
                .onFailure { error = it.message }
            cargando = false
        }
    }
}
