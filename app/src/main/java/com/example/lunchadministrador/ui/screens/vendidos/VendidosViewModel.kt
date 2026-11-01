package com.example.lunchadministrador.ui.screens.vendidos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lunchadministrador.data.local.dao.VentaProducto
import com.example.lunchadministrador.data.repository.LunchRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class VendidosViewModel(repo: LunchRepository) : ViewModel() {
    val vendidos: StateFlow<List<VentaProducto>> =
        repo.vendidosHoy().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
