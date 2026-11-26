package com.example.lunchadministrador.data.local

import com.example.lunchadministrador.data.local.entity.MesaEntity
import com.example.lunchadministrador.data.local.entity.ProductoEntity
import com.example.lunchadministrador.data.local.entity.UsuarioEntity
import com.example.lunchadministrador.data.model.Rol
import com.example.lunchadministrador.data.seguridad.Seguridad

/** Datos iniciales la primera vez que se abre la app. */
object Sembrador {
    const val ADMIN_CORREO = "admin@lunch.com"
    const val ADMIN_CLAVE = "admin123"
    const val MESERO_CORREO = "mesero@lunch.com"
    const val MESERO_CLAVE = "mesero123"

    /** Foto desde Wikimedia Commons. Para cambiarla, pega cualquier otro link de imagen. */
    private fun foto(archivo: String) = "https://commons.wikimedia.org/wiki/Special:FilePath/$archivo?width=400"

    suspend fun sembrar(db: AppDatabase) {
        if (db.usuarioDao().contar() == 0) {
            db.usuarioDao().insertar(nuevo("Administrador", ADMIN_CORREO, ADMIN_CLAVE, Rol.ADMIN))
            db.usuarioDao().insertar(nuevo("Mesero", MESERO_CORREO, MESERO_CLAVE, Rol.MESERO))
        }
        if (db.productoDao().contar() == 0) {
            db.productoDao().insertar(
                listOf(
                    ProductoEntity(nombre = "Hamburguesa", icono = foto("Hamburger_(black_bg).jpg"), precio = 15000, stock = 12, necesarios = "Pan,Carne,Queso,Lechuga"),
                    ProductoEntity(nombre = "Perro caliente", icono = foto("Hotdog_-_Evan_Swigart.jpg"), precio = 10000, stock = 9, necesarios = "Pan,Salchicha,Salsas"),
                    ProductoEntity(nombre = "Pizza", icono = foto("Pizza-3007395.jpg"), precio = 8000, stock = 8, necesarios = "Masa,Queso,Salsa de tomate"),
                    ProductoEntity(nombre = "Papas fritas", icono = foto("Fries_2.jpg"), precio = 6000, stock = 15, necesarios = "Papa,Aceite,Sal"),
                    ProductoEntity(nombre = "Jugo de naranja", icono = foto("Orange_juice_1.jpg"), precio = 5000, stock = 10, necesarios = "Naranja,Hielo"),
                    ProductoEntity(nombre = "Gaseosa", icono = "🥤", precio = 3500, stock = 20, necesarios = "Gaseosa,Hielo"),
                    ProductoEntity(nombre = "Sándwich", icono = "🥪", precio = 9000, stock = 6, necesarios = "Pan,Jamón,Queso"),
                    ProductoEntity(nombre = "Ensalada", icono = "🥗", precio = 7000, stock = 3, necesarios = "Lechuga,Tomate,Aderezo"),
                    ProductoEntity(nombre = "Helado", icono = "🍦", precio = 4000, stock = 2, necesarios = "Helado,Cono"),
                    ProductoEntity(nombre = "Café", icono = "☕", precio = 3000, stock = 14, necesarios = "Café,Leche,Azúcar"),
                    ProductoEntity(nombre = "Pollo frito", icono = "🍗", precio = 13000, stock = 7, necesarios = "Pollo,Aceite,Harina"),
                    ProductoEntity(nombre = "Tacos", icono = "🌮", precio = 11000, stock = 5, necesarios = "Tortilla,Carne,Cebolla")
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