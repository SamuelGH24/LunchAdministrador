package com.example.lunchadministrador.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.lunchadministrador.data.local.entity.PedidoEntity
import com.example.lunchadministrador.data.local.entity.PedidoLineaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PedidoDao {
    @Insert suspend fun insertarPedido(pedido: PedidoEntity): Long
    @Insert suspend fun insertarLineas(lineas: List<PedidoLineaEntity>)

    @Query("SELECT COUNT(*) FROM pedidos WHERE mesaNumero = :mesa AND fecha >= :desde")
    suspend fun contarDeMesa(mesa: Int, desde: Long): Int

    @Transaction
    @Query("SELECT * FROM pedidos WHERE mesaNumero = :mesa AND fecha >= :desde ORDER BY fecha ASC")
    fun historialMesa(mesa: Int, desde: Long): Flow<List<PedidoConLineas>>

    @Query(
        "SELECT pl.productoId AS productoId, pl.nombre AS nombre, pl.icono AS icono, " +
            "SUM(pl.cantidad) AS cantidad, SUM(pl.cantidad * pl.precioUnit) AS subtotal " +
            "FROM pedido_lineas pl INNER JOIN pedidos p ON p.id = pl.pedidoId " +
            "WHERE p.fecha >= :desde GROUP BY pl.productoId ORDER BY cantidad DESC"
    )
    fun vendidosDesde(desde: Long): Flow<List<VentaProducto>>

    @Query(
        "SELECT COALESCE(SUM(pl.cantidad), 0) FROM pedido_lineas pl INNER JOIN pedidos p ON p.id = pl.pedidoId " +
            "WHERE p.atendidoPorId = :usuario AND p.fecha >= :desde"
    )
    fun cantidadVendidaPor(usuario: Long, desde: Long): Flow<Int>
}
