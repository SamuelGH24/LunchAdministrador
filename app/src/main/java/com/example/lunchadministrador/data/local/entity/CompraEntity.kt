package com.example.lunchadministrador.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Compra de materiales hecha por el administrador. */
@Entity(tableName = "compras")
data class CompraEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productoId: Int,
    val unidades: Int,
    val fecha: Long,
    val usuarioId: Long
)
