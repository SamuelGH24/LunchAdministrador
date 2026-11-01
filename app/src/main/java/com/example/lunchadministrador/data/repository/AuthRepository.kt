package com.example.lunchadministrador.data.repository

import com.example.lunchadministrador.data.local.SesionLocal
import com.example.lunchadministrador.data.local.dao.UsuarioDao
import com.example.lunchadministrador.data.local.entity.UsuarioEntity
import com.example.lunchadministrador.data.model.Rol
import com.example.lunchadministrador.data.seguridad.Seguridad
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Registro, inicio de sesión y manejo de la cuenta. Hoy usa Room; mañana se puede cambiar por Firebase Auth. */
class AuthRepository(private val dao: UsuarioDao, private val sesion: SesionLocal) {

    private val _usuario = MutableStateFlow<UsuarioEntity?>(null)
    val usuario: StateFlow<UsuarioEntity?> = _usuario.asStateFlow()

    /** true cuando ya se intentó recuperar la sesión guardada (para decidir la pantalla inicial). */
    private val _listo = MutableStateFlow(false)
    val listo: StateFlow<Boolean> = _listo.asStateFlow()

    suspend fun restaurarSesion() {
        val id = sesion.usuarioId()
        _usuario.value = if (id > 0) dao.porId(id) else null
        _listo.value = true
    }

    suspend fun iniciarSesion(correo: String, clave: String): Result<UsuarioEntity> {
        val c = correo.trim().lowercase()
        if (c.isEmpty() || clave.isEmpty()) return fallo("Escribe tu correo y contraseña")
        val u = dao.porCorreo(c)
        if (u == null || Seguridad.hash(clave, u.sal) != u.claveHash) return fallo("Correo o contraseña incorrectos")
        sesion.guardar(u.id)
        _usuario.value = u
        return Result.success(u)
    }

    /** Los registros nuevos son Meseros; el Administrador viene creado desde el inicio. */
    suspend fun registrar(nombre: String, correo: String, clave: String): Result<UsuarioEntity> {
        val c = correo.trim().lowercase()
        if (nombre.isBlank()) return fallo("Escribe tu nombre")
        if (!c.contains("@") || !c.contains(".")) return fallo("El correo no es válido")
        if (clave.length < 6) return fallo("La contraseña debe tener mínimo 6 caracteres")
        if (dao.porCorreo(c) != null) return fallo("Ese correo ya está registrado")
        val sal = Seguridad.nuevaSal()
        val nuevo = UsuarioEntity(
            nombre = nombre.trim(), correo = c, claveHash = Seguridad.hash(clave, sal),
            sal = sal, rol = Rol.MESERO.name, creadoEn = System.currentTimeMillis()
        )
        val id = dao.insertar(nuevo)
        val guardado = nuevo.copy(id = id)
        sesion.guardar(id)
        _usuario.value = guardado
        return Result.success(guardado)
    }

    fun cerrarSesion() {
        sesion.cerrar()
        _usuario.value = null
    }

    suspend fun cambiarCorreo(nuevo: String): Result<Unit> {
        val actual = _usuario.value ?: return fallo("No hay sesión")
        val c = nuevo.trim().lowercase()
        if (!c.contains("@") || !c.contains(".")) return fallo("El correo no es válido")
        if (dao.porCorreo(c)?.id.let { it != null && it != actual.id }) return fallo("Ese correo ya está registrado")
        val actualizado = actual.copy(correo = c)
        dao.actualizar(actualizado)
        _usuario.value = actualizado
        return Result.success(Unit)
    }

    suspend fun cambiarClave(claveActual: String, claveNueva: String): Result<Unit> {
        val actual = _usuario.value ?: return fallo("No hay sesión")
        if (Seguridad.hash(claveActual, actual.sal) != actual.claveHash) return fallo("La contraseña actual no es correcta")
        if (claveNueva.length < 6) return fallo("La nueva contraseña debe tener mínimo 6 caracteres")
        val sal = Seguridad.nuevaSal()
        val actualizado = actual.copy(sal = sal, claveHash = Seguridad.hash(claveNueva, sal))
        dao.actualizar(actualizado)
        _usuario.value = actualizado
        return Result.success(Unit)
    }

    private fun <T> fallo(mensaje: String): Result<T> = Result.failure(IllegalArgumentException(mensaje))
}
