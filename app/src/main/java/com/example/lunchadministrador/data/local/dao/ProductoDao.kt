package com.example.lunchadministrador.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.lunchadministrador.data.local.entity.CompraEntity
import com.example.lunchadministrador.data.local.entity.ProductoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {
    @Query("SELECT * FROM productos ORDER BY id") fun observarTodos(): Flow<List<ProductoEntity>>
    @Query("SELECT COUNT(*) FROM productos") suspend fun contar(): Int
    @Insert suspend fun insertar(lista: List<ProductoEntity>)
    @Query("UPDATE productos SET stock = stock + :unidades WHERE id = :id") suspend fun sumarStock(id: Int, unidades: Int)
    @Insert suspend fun insertarCompra(compra: CompraEntity)
}
