package com.example.lunchadministrador.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/** Pedido ya cerrado: el cliente pagó y se retiró. */
@Entity(tableName = "pedidos", indices = [Index("mesaNumero"), Index("fecha")])
data class PedidoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val mesaNumero: Int,
    val cliente: Int,
    val fecha: Long,
    val atendidoPorId: Long,
    val atendidoPorNombre: String,
    val total: Int
)

/** Copia del producto al momento de la venta (si luego cambia el precio, el historial no se altera). */
@Entity(tableName = "pedido_lineas", indices = [Index("pedidoId")])
data class PedidoLineaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val pedidoId: Long,
    val productoId: Int,
    val nombre: String,
    val icono: String,
    val precioUnit: Int,
    val cantidad: Int
)
