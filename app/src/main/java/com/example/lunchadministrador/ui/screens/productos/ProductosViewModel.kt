package com.example.lunchadministrador.ui.screens.productos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lunchadministrador.data.local.entity.ProductoEntity
import com.example.lunchadministrador.data.repository.LunchRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ProductosViewModel(repo: LunchRepository) : ViewModel() {
    val productos: StateFlow<List<ProductoEntity>> =
        repo.productos().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
