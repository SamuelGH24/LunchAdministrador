package com.example.lunchadministrador.ui.screens.cuenta

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lunchadministrador.data.local.entity.UsuarioEntity
import com.example.lunchadministrador.data.repository.AuthRepository
import com.example.lunchadministrador.data.repository.LunchRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class CuentaViewModel(private val auth: AuthRepository, repo: LunchRepository) : ViewModel() {
    val usuario: StateFlow<UsuarioEntity?> = auth.usuario

    val vendidosHoy: StateFlow<Int> = auth.usuario
        .flatMapLatest { u -> if (u == null) flowOf(0) else repo.cantidadVendidaPor(u.id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    /** onResultado recibe null si salió bien, o el mensaje de error. */
    fun cambiarCorreo(nuevo: String, onResultado: (String?) -> Unit) {
        viewModelScope.launch { onResultado(auth.cambiarCorreo(nuevo).exceptionOrNull()?.message) }
    }

    fun cambiarClave(actual: String, nueva: String, onResultado: (String?) -> Unit) {
        viewModelScope.launch { onResultado(auth.cambiarClave(actual, nueva).exceptionOrNull()?.message) }
    }
}
