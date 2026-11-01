package com.example.lunchadministrador.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class ProductoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val icono: String,
    val precio: Int,
    val stock: Int,
    /** Materiales necesarios, separados por coma. */
    val necesarios: String
) {
    val listaNecesarios: List<String> get() = necesarios.split(",").map { it.trim() }.filter { it.isNotEmpty() }
}
