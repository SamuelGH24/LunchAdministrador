package com.example.lunchadministrador.ui.screens.mesas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lunchadministrador.data.model.MesaUi
import com.example.lunchadministrador.data.repository.LunchRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MesasViewModel(private val repo: LunchRepository) : ViewModel() {
    val mesas: StateFlow<List<MesaUi>> =
        repo.mesas().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun quitar(mesa: Int, productoId: Int) {
        viewModelScope.launch { repo.quitarUnidad(mesa, productoId) }
    }
}
