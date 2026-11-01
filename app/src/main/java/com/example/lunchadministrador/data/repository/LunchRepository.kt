package com.example.lunchadministrador.data.repository

import androidx.room.withTransaction
import com.example.lunchadministrador.data.local.AppDatabase
import com.example.lunchadministrador.data.local.dao.PedidoConLineas
import com.example.lunchadministrador.data.local.dao.VentaProducto
import com.example.lunchadministrador.data.local.entity.*
import com.example.lunchadministrador.data.model.MesaUi
import com.example.lunchadministrador.util.Fechas
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

/** Toda la lógica de mesas, pedidos, productos y reportes. Las pantallas nunca tocan los DAO directamente. */
class LunchRepository(private val db: AppDatabase) {

    private val mesaDao = db.mesaDao()
    private val productoDao = db.productoDao()
    private val pedidoDao = db.pedidoDao()

    // ── Lectura (Flow: la pantalla se actualiza sola) ──
    fun mesas(): Flow<List<MesaUi>> =
        combine(mesaDao.observarMesas(), mesaDao.observarLineas()) { mesas, lineas ->
            mesas.map { m -> MesaUi(m.numero, m.atendidoPorNombre, m.desde, lineas.filter { it.mesaNumero == m.numero }) }
        }

    fun productos(): Flow<List<ProductoEntity>> = productoDao.observarTodos()

    fun historialMesa(mesa: Int): Flow<List<PedidoConLineas>> = pedidoDao.historialMesa(mesa, Fechas.inicioDelDia())

    fun vendidosHoy(): Flow<List<VentaProducto>> = pedidoDao.vendidosDesde(Fechas.inicioDelDia())

    /** Cantidad vendida de cada producto en lo que va del mes (productoId -> cantidad). */
    fun ventasDelMes(): Flow<Map<Int, Int>> =
        pedidoDao.vendidosDesde(Fechas.inicioDelMes()).map { l -> l.associate { it.productoId to it.cantidad } }

    fun cantidadVendidaPor(usuarioId: Long): Flow<Int> = pedidoDao.cantidadVendidaPor(usuarioId, Fechas.inicioDelDia())

    // ── Escritura ──
    suspend fun agregarProducto(mesa: Int, productoId: Int, usuario: UsuarioEntity) {
        db.withTransaction {
            val m = mesaDao.mesa(mesa) ?: return@withTransaction
            val linea = mesaDao.linea(mesa, productoId)
            if (linea == null) mesaDao.insertarLinea(MesaLineaEntity(mesaNumero = mesa, productoId = productoId, cantidad = 1))
            else mesaDao.actualizarLinea(linea.copy(cantidad = linea.cantidad + 1))
            if (m.desde == null) {
                mesaDao.guardarMesa(m.copy(atendidoPorId = usuario.id, atendidoPorNombre = usuario.nombre, desde = System.currentTimeMillis()))
            }
        }
    }

    suspend fun quitarUnidad(mesa: Int, productoId: Int) {
        db.withTransaction {
            val linea = mesaDao.linea(mesa, productoId) ?: return@withTransaction
            if (linea.cantidad > 1) mesaDao.actualizarLinea(linea.copy(cantidad = linea.cantidad - 1))
            else mesaDao.borrarLinea(linea)
            if (mesaDao.lineasDe(mesa).isEmpty()) mesaDao.guardarMesa(MesaEntity(numero = mesa))
        }
    }

    /** "Valor pagado": guarda el pedido (productos, cantidad, fecha y hora) y deja la mesa libre. */
    suspend fun cobrar(mesa: Int) {
        db.withTransaction {
            val m = mesaDao.mesa(mesa) ?: return@withTransaction
            val lineas = mesaDao.lineasConProductoDe(mesa)
            if (lineas.isEmpty()) return@withTransaction
            val cliente = pedidoDao.contarDeMesa(mesa, Fechas.inicioDelDia()) + 1
            val pedidoId = pedidoDao.insertarPedido(
                PedidoEntity(
                    mesaNumero = mesa, cliente = cliente, fecha = System.currentTimeMillis(),
                    atendidoPorId = m.atendidoPorId ?: 0, atendidoPorNombre = m.atendidoPorNombre,
                    total = lineas.sumOf { it.precio * it.cantidad }
                )
            )
            pedidoDao.insertarLineas(lineas.map {
                PedidoLineaEntity(pedidoId = pedidoId, productoId = it.productoId, nombre = it.nombre,
                    icono = it.icono, precioUnit = it.precio, cantidad = it.cantidad)
            })
            mesaDao.borrarLineasDe(mesa)
            mesaDao.guardarMesa(MesaEntity(numero = mesa))
        }
    }

    suspend fun comprarMateriales(productoId: Int, unidades: Int, usuarioId: Long) {
        db.withTransaction {
            productoDao.sumarStock(productoId, unidades)
            productoDao.insertarCompra(CompraEntity(productoId = productoId, unidades = unidades, fecha = System.currentTimeMillis(), usuarioId = usuarioId))
        }
    }
}
