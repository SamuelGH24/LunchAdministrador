package com.example.lunchadministrador.ui.screens.registro

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lunchadministrador.data.repository.AuthRepository
import kotlinx.coroutines.launch

class RegistroViewModel(private val auth: AuthRepository) : ViewModel() {
    var nombre by mutableStateOf("")
    var correo by mutableStateOf("")
    var clave by mutableStateOf("")
    var confirmar by mutableStateOf("")
    var error by mutableStateOf<String?>(null)

    fun registrar(onOk: () -> Unit) {
        if (clave != confirmar) { error = "Las contraseñas no coinciden"; return }
        viewModelScope.launch {
            auth.registrar(nombre, correo, clave)
                .onSuccess { error = null; onOk() }
                .onFailure { error = it.message }
        }
    }
}
