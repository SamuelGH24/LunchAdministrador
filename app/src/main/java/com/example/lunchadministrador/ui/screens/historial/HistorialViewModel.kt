package com.example.lunchadministrador.ui.screens.historial

import androidx.lifecycle.ViewModel
import com.example.lunchadministrador.data.local.dao.PedidoConLineas
import com.example.lunchadministrador.data.repository.LunchRepository
import kotlinx.coroutines.flow.Flow

class HistorialViewModel(private val repo: LunchRepository) : ViewModel() {
    fun historial(mesa: Int): Flow<List<PedidoConLineas>> = repo.historialMesa(mesa)
}
