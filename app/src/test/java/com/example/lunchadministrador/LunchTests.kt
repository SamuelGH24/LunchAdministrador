package com.example.lunchadministrador

import com.example.lunchadministrador.data.model.LineaMesa
import com.example.lunchadministrador.data.model.MesaUi
import com.example.lunchadministrador.data.seguridad.Seguridad
import com.example.lunchadministrador.util.Formato
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LunchTests {

    @Test
    fun hash_esIgualConMismaSalYClave() {
        assertEquals(Seguridad.hash("clave123", "sal"), Seguridad.hash("clave123", "sal"))
    }

    @Test
    fun hash_cambiaConOtraSal() {
        assertNotEquals(Seguridad.hash("clave123", "sal1"), Seguridad.hash("clave123", "sal2"))
    }

    @Test
    fun hash_noGuardaLaClaveEnTexto() {
        assertFalse(Seguridad.hash("clave123", "sal").contains("clave123"))
    }

    @Test
    fun mesa_totalSumaPrecioPorCantidad() {
        val mesa = MesaUi(
            numero = 1, atendidoPor = "Mesero", desde = null,
            lineas = listOf(
                LineaMesa(1, 1, "Hamburguesa", "x", 15000, 2),
                LineaMesa(1, 2, "Gaseosa", "x", 3500, 1)
            )
        )
        assertEquals(33500, mesa.total)
        assertTrue(mesa.ocupada)
    }

    @Test
    fun mesa_sinLineasEstaLibre() {
        assertFalse(MesaUi(2, "", null, emptyList()).ocupada)
    }

    @Test
    fun pesos_usaPuntosDeMiles() {
        assertEquals("\$15.000", Formato.pesos(15000))
    }
}