package com.example.lunchadministrador.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.lunchadministrador.data.local.entity.MesaEntity
import com.example.lunchadministrador.data.local.entity.MesaLineaEntity
import com.example.lunchadministrador.data.model.LineaMesa
import kotlinx.coroutines.flow.Flow

@Dao
interface MesaDao {
    @Query("SELECT * FROM mesas ORDER BY numero") fun observarMesas(): Flow<List<MesaEntity>>

    @Query(
        "SELECT l.mesaNumero AS mesaNumero, l.productoId AS productoId, p.nombre AS nombre, " +
            "p.icono AS icono, p.precio AS precio, l.cantidad AS cantidad " +
            "FROM mesa_lineas l INNER JOIN productos p ON p.id = l.productoId ORDER BY l.id"
    )
    fun observarLineas(): Flow<List<LineaMesa>>

    @Query(
        "SELECT l.mesaNumero AS mesaNumero, l.productoId AS productoId, p.nombre AS nombre, " +
            "p.icono AS icono, p.precio AS precio, l.cantidad AS cantidad " +
            "FROM mesa_lineas l INNER JOIN productos p ON p.id = l.productoId WHERE l.mesaNumero = :mesa ORDER BY l.id"
    )
    suspend fun lineasConProductoDe(mesa: Int): List<LineaMesa>

    @Query("SELECT * FROM mesa_lineas WHERE mesaNumero = :mesa") suspend fun lineasDe(mesa: Int): List<MesaLineaEntity>
    @Query("SELECT * FROM mesa_lineas WHERE mesaNumero = :mesa AND productoId = :producto LIMIT 1")
    suspend fun linea(mesa: Int, producto: Int): MesaLineaEntity?

    @Insert suspend fun insertarLinea(linea: MesaLineaEntity)
    @Update suspend fun actualizarLinea(linea: MesaLineaEntity)
    @Delete suspend fun borrarLinea(linea: MesaLineaEntity)
    @Query("DELETE FROM mesa_lineas WHERE mesaNumero = :mesa") suspend fun borrarLineasDe(mesa: Int)

    @Query("SELECT * FROM mesas WHERE numero = :numero LIMIT 1") suspend fun mesa(numero: Int): MesaEntity?
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun guardarMesa(mesa: MesaEntity)
    @Insert(onConflict = OnConflictStrategy.IGNORE) suspend fun insertarMesas(lista: List<MesaEntity>)
}
