package com.example.lunchadministrador.ui.navigation

object Rutas {
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val MESAS = "mesas"
    const val AGREGAR = "agregar/{mesa}"
    const val HISTORIAL = "historial/{mesa}"
    const val COBRO = "cobro"
    const val PRODUCTOS = "productos"
    const val VENDIDOS = "vendidos"
    const val COMPRA = "compra"
    const val CUENTA = "cuenta"

    fun agregar(mesa: Int) = "agregar/$mesa"
    fun historial(mesa: Int) = "historial/$mesa"
}
