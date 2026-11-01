package com.example.lunchadministrador.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.lunchadministrador.data.model.Rol

@Entity(tableName = "usuarios", indices = [Index(value = ["correo"], unique = true)])
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val correo: String,
    val claveHash: String,
    val sal: String,
    val rol: String,
    val creadoEn: Long
) {
    val rolEnum: Rol get() = Rol.valueOf(rol)
}
