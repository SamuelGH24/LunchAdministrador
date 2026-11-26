package com.example.lunchadministrador.ui.screens.historial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.lunchadministrador.data.local.dao.PedidoConLineas
import com.example.lunchadministrador.data.repository.LunchRepository
import kotlinx.coroutines.flow.Flow

class HistorialViewModel(private val repo: LunchRepository) : ViewModel() {

    // Un flujo por mesa, guardado en caché para no recargar al cambiar de pestaña.
    private val flujos = mutableMapOf<Int, Flow<PagingData<PedidoConLineas>>>()

    fun historial(mesa: Int): Flow<PagingData<PedidoConLineas>> =
        flujos.getOrPut(mesa) { repo.historialMesaPaginado(mesa).cachedIn(viewModelScope) }
}