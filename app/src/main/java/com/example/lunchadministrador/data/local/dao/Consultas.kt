package com.example.lunchadministrador.data.local.dao

import androidx.room.Embedded
import androidx.room.Relation
import com.example.lunchadministrador.data.local.entity.PedidoEntity
import com.example.lunchadministrador.data.local.entity.PedidoLineaEntity

/** Resultado del reporte: un producto con lo vendido. */
data class VentaProducto(
    val productoId: Int,
    val nombre: String,
    val icono: String,
    val cantidad: Int,
    val subtotal: Int
)

data class PedidoConLineas(
    @Embedded val pedido: PedidoEntity,
    @Relation(parentColumn = "id", entityColumn = "pedidoId") val lineas: List<PedidoLineaEntity>
)
