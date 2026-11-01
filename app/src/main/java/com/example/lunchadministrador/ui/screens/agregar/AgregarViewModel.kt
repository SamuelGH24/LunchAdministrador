package com.example.lunchadministrador.ui.screens.agregar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lunchadministrador.data.local.entity.ProductoEntity
import com.example.lunchadministrador.data.repository.AuthRepository
import com.example.lunchadministrador.data.repository.LunchRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AgregarViewModel(private val repo: LunchRepository, private val auth: AuthRepository) : ViewModel() {
    val productos: StateFlow<List<ProductoEntity>> =
        repo.productos().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val ventasMes: StateFlow<Map<Int, Int>> =
        repo.ventasDelMes().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    fun agregar(mesa: Int, productoId: Int) {
        val usuario = auth.usuario.value ?: return
        viewModelScope.launch { repo.agregarProducto(mesa, productoId, usuario) }
    }
}
