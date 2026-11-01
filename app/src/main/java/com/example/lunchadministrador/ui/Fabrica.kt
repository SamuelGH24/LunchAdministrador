package com.example.lunchadministrador.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.lunchadministrador.LunchApp
import com.example.lunchadministrador.di.AppContainer

/** Crea ViewModels entregándoles los repositorios del AppContainer. */
inline fun <reified T : ViewModel> fabrica(
    crossinline crear: (AppContainer) -> T
): ViewModelProvider.Factory = viewModelFactory {
    initializer<T> {
        val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LunchApp
        crear(app.container)
    }
}
