package com.example.lunchadministrador.data.local

import com.example.lunchadministrador.data.local.entity.MesaEntity
import com.example.lunchadministrador.data.local.entity.ProductoEntity
import com.example.lunchadministrador.data.local.entity.UsuarioEntity
import com.example.lunchadministrador.data.model.Rol
import com.example.lunchadministrador.data.seguridad.Seguridad

/** Datos iniciales la primera vez que se abre la app. Cambia los productos por los reales. */
object Sembrador {
    const val ADMIN_CORREO = "admin@lunch.com"
    const val ADMIN_CLAVE = "admin123"
    const val MESERO_CORREO = "mesero@lunch.com"
    const val MESERO_CLAVE = "mesero123"

    suspend fun sembrar(db: AppDatabase) {
        if (db.usuarioDao().contar() == 0) {
            db.usuarioDao().insertar(nuevo("Administrador", ADMIN_CORREO, ADMIN_CLAVE, Rol.ADMIN))
            db.usuarioDao().insertar(nuevo("Mesero", MESERO_CORREO, MESERO_CLAVE, Rol.MESERO))
        }
        if (db.productoDao().contar() == 0) {
            db.productoDao().insertar(
                listOf(
                    ProductoEntity(nombre = "Producto 1", icono = "🍸", precio = 5000, stock = 3, necesarios = "Producto 2,Producto 3"),
                    ProductoEntity(nombre = "Producto 2", icono = "🍔🥤", precio = 4000, stock = 3, necesarios = "Producto 1,Producto 3"),
                    ProductoEntity(nombre = "Producto 3", icono = "🍔🥤", precio = 4000, stock = 3, necesarios = "Producto 1,Producto 2"),
                    ProductoEntity(nombre = "Producto 4", icono = "🍔🥤", precio = 4000, stock = 3, necesarios = "Producto 1"),
                    ProductoEntity(nombre = "Producto 5", icono = "🍔🥤", precio = 3500, stock = 3, necesarios = "Producto 2"),
                    ProductoEntity(nombre = "Producto 6", icono = "🍔🥤", precio = 3000, stock = 3, necesarios = "Producto 3")
                )
            )
        }
        db.mesaDao().insertarMesas((1..4).map { MesaEntity(numero = it) })
    }

    private fun nuevo(nombre: String, correo: String, clave: String, rol: Rol): UsuarioEntity {
        val sal = Seguridad.nuevaSal()
        return UsuarioEntity(
            nombre = nombre, correo = correo, claveHash = Seguridad.hash(clave, sal),
            sal = sal, rol = rol.name, creadoEn = System.currentTimeMillis()
        )
    }
}
