package com.example.lunchadministrador.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lunchadministrador.ui.navigation.Rutas
import com.example.lunchadministrador.ui.theme.Carbon
import com.example.lunchadministrador.ui.theme.Naranja
import com.example.lunchadministrador.ui.theme.Superficie
import com.example.lunchadministrador.ui.theme.Tarjeta
import com.example.lunchadministrador.ui.theme.TextoSecundario

data class Destino(val ruta: String, val icono: ImageVector, val descripcion: String, val etiqueta: String)

/** Las 5 secciones principales de la barra de navegación inferior. */
val DESTINOS = listOf(
    Destino(Rutas.CUENTA, Icons.Default.Person, "Cuenta", "Cuenta"),
    Destino(Rutas.MESAS, Icons.Default.Home, "Mesas", "Mesas"),
    Destino(Rutas.PRODUCTOS, Icons.Default.List, "Lista de productos", "Productos"),
    Destino(Rutas.COMPRA, Icons.Default.ShoppingCart, "Compra de productos", "Compra"),
    Destino(Rutas.COBRO, Icons.Default.CheckCircle, "Cobro", "Cobro")
)

@Composable
fun BarraInferior(rutaActual: String?, onIr: (String) -> Unit) {
    NavigationBar(containerColor = Tarjeta, tonalElevation = 8.dp) {
        DESTINOS.forEach { d ->
            val activo = rutaActual?.startsWith(d.ruta) == true
            NavigationBarItem(
                selected = activo,
                onClick = { onIr(d.ruta) },
                icon = { Icon(d.icono, contentDescription = d.descripcion) },
                label = { Text(d.etiqueta, fontSize = 12.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Naranja, selectedTextColor = Naranja,
                    indicatorColor = Superficie,
                    unselectedIconColor = TextoSecundario, unselectedTextColor = TextoSecundario
                )
            )
        }
    }
}

/** Pestañas tipo chip: "Mesa 1 | Mesa 2 | Mesa 3 | Mesa 4". */
@Composable
fun PestanasMesa(seleccionada: Int, onSeleccion: (Int) -> Unit) {
    Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        (1..4).forEach { n ->
            val activa = n == seleccionada
            Box(
                Modifier.weight(1f).heightIn(min = 44.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(if (activa) Naranja else Tarjeta)
                    .clickable { onSeleccion(n) },
                contentAlignment = Alignment.Center
            ) {
                Txt("Mesa $n", 14, negrita = activa, color = if (activa) Color.White else Carbon)
            }
        }
    }
}