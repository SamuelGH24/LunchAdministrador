package com.example.lunchadministrador.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.lunchadministrador.data.local.entity.UsuarioEntity

@Dao
interface UsuarioDao {
    @Insert suspend fun insertar(usuario: UsuarioEntity): Long
    @Update suspend fun actualizar(usuario: UsuarioEntity)
    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1") suspend fun porCorreo(correo: String): UsuarioEntity?
    @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1") suspend fun porId(id: Long): UsuarioEntity?
    @Query("SELECT COUNT(*) FROM usuarios") suspend fun contar(): Int
}
