package com.example.lunchadministrador.ui.components

import com.example.lunchadministrador.ui.theme.Carbon

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.lunchadministrador.ui.navigation.Rutas
import com.example.lunchadministrador.ui.theme.Naranja
import com.example.lunchadministrador.ui.theme.NaranjaOscuro

data class Destino(val ruta: String, val icono: ImageVector, val descripcion: String)

/** Los 5 íconos de la barra superior de los mockups. */
val DESTINOS = listOf(
    Destino(Rutas.CUENTA, Icons.Default.Person, "Cuenta"),
    Destino(Rutas.MESAS, Icons.Default.Home, "Mesas"),
    Destino(Rutas.PRODUCTOS, Icons.Default.List, "Lista de productos"),
    Destino(Rutas.COMPRA, Icons.Default.ShoppingCart, "Compra de productos"),
    Destino(Rutas.COBRO, Icons.Default.CheckCircle, "Cobro")
)

@Composable
fun BarraSuperior(rutaActual: String?, onIr: (String) -> Unit) {
    Row(
        Modifier.fillMaxWidth().padding(8.dp).border(BorderStroke(1.5.dp, Naranja), RoundedCornerShape(8.dp)),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        DESTINOS.forEach { d ->
            val activo = rutaActual?.startsWith(d.ruta) == true
            IconButton(onClick = { onIr(d.ruta) }) {
                Icon(d.icono, contentDescription = d.descripcion, tint = if (activo) NaranjaOscuro else Carbon)
            }
        }
    }
}

/** Pestañas "Mesa 1 | Mesa 2 | Mesa 3 | Mesa 4". */
@Composable
fun PestanasMesa(seleccionada: Int, onSeleccion: (Int) -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        (1..4).forEach { n ->
            Txt(
                "Mesa $n", 14, negrita = n == seleccionada,
                color = if (n == seleccionada) NaranjaOscuro else Carbon,
                modifier = Modifier.clickable { onSeleccion(n) }.padding(8.dp)
            )
        }
    }
}
