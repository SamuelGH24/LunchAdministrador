package com.example.lunchadministrador.data.seguridad

import java.security.MessageDigest
import java.security.SecureRandom

/** Las contraseñas nunca se guardan en texto: se guarda SHA-256(sal + clave). */
object Seguridad {
    fun nuevaSal(): String = ByteArray(16).also { SecureRandom().nextBytes(it) }.toHex()

    fun hash(clave: String, sal: String): String =
        MessageDigest.getInstance("SHA-256").digest((sal + clave).toByteArray()).toHex()

    private fun ByteArray.toHex() = joinToString("") { "%02x".format(it) }
}
