package com.example.lunchadministrador.di

import android.content.Context
import com.example.lunchadministrador.data.local.AppDatabase
import com.example.lunchadministrador.data.local.SesionLocal
import com.example.lunchadministrador.data.local.Sembrador
import com.example.lunchadministrador.data.repository.AuthRepository
import com.example.lunchadministrador.data.repository.LunchRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/** Crea una sola vez la base de datos y los repositorios, y los comparte con todos los ViewModel. */
class AppContainer(context: Context) {
    private val db = AppDatabase.crear(context)
    val auth = AuthRepository(db.usuarioDao(), SesionLocal(context))
    val lunch = LunchRepository(db)

    init {
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            Sembrador.sembrar(db)
            auth.restaurarSesion()
        }
    }
}
