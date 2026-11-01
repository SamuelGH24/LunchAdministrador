package com.example.lunchadministrador.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "mesas")
data class MesaEntity(
    @PrimaryKey val numero: Int,
    val atendidoPorId: Long? = null,
    val atendidoPorNombre: String = "",
    /** Momento (ms) en que se abrió la mesa; null si está libre. */
    val desde: Long? = null
)

/** Producto que la mesa tiene asignado en este momento (pedido en curso). */
@Entity(
    tableName = "mesa_lineas",
    indices = [Index(value = ["mesaNumero", "productoId"], unique = true)]
)
data class MesaLineaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val mesaNumero: Int,
    val productoId: Int,
    val cantidad: Int
)
