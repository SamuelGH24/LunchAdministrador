package com.example.lunchadministrador.data.model

enum class Rol { ADMIN, MESERO }

/** Línea de una mesa con los datos del producto ya unidos (resultado de un JOIN). */
data class LineaMesa(
    val mesaNumero: Int,
    val productoId: Int,
    val nombre: String,
    val icono: String,
    val precio: Int,
    val cantidad: Int
)

/** Lo que la pantalla necesita saber de una mesa. */
data class MesaUi(
    val numero: Int,
    val atendidoPor: String,
    val desde: Long?,
    val lineas: List<LineaMesa>
) {
    val ocupada: Boolean get() = lineas.isNotEmpty()
    val total: Int get() = lineas.sumOf { it.precio * it.cantidad }
}
