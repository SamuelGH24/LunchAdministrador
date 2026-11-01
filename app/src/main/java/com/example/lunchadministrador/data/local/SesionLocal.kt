package com.example.lunchadministrador.data.local

import android.content.Context

/** Recuerda qué usuario inició sesión para no pedir el login cada vez que se abre la app. */
class SesionLocal(context: Context) {
    private val prefs = context.getSharedPreferences("sesion", Context.MODE_PRIVATE)
    fun usuarioId(): Long = prefs.getLong("usuarioId", -1L)
    fun guardar(id: Long) = prefs.edit().putLong("usuarioId", id).apply()
    fun cerrar() = prefs.edit().remove("usuarioId").apply()
}
