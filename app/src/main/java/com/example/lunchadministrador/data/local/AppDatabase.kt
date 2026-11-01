package com.example.lunchadministrador.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lunchadministrador.data.local.dao.MesaDao
import com.example.lunchadministrador.data.local.dao.PedidoDao
import com.example.lunchadministrador.data.local.dao.ProductoDao
import com.example.lunchadministrador.data.local.dao.UsuarioDao
import com.example.lunchadministrador.data.local.entity.*

@Database(
    entities = [
        UsuarioEntity::class, ProductoEntity::class, MesaEntity::class, MesaLineaEntity::class,
        PedidoEntity::class, PedidoLineaEntity::class, CompraEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun productoDao(): ProductoDao
    abstract fun mesaDao(): MesaDao
    abstract fun pedidoDao(): PedidoDao

    companion object {
        fun crear(context: Context): AppDatabase =
            Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "lunch_db").build()
    }
}
