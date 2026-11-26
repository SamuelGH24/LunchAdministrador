package com.example.lunchadministrador.ui.screens.productos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lunchadministrador.ui.components.*
import com.example.lunchadministrador.ui.fabrica
import com.example.lunchadministrador.ui.theme.NaranjaOscuro
import com.example.lunchadministrador.ui.theme.Rojo
import com.example.lunchadministrador.ui.theme.TextoSecundario
import com.example.lunchadministrador.ui.theme.Verde
import com.example.lunchadministrador.util.Formato

@Composable
fun ProductosScreen(
    esAdmin: Boolean,
    onVendidos: () -> Unit,
    vm: ProductosViewModel = viewModel(factory = fabrica { ProductosViewModel(it.lunch) })
) {
    val productos by vm.productos.collectAsState()
    Column(Modifier.fillMaxSize().padding(12.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column {
                Txt("Productos", 22, true, align = TextAlign.Start)
                Txt("${productos.size} en el menú", 13, color = TextoSecundario, align = TextAlign.Start)
            }
            if (esAdmin) Caja(onClick = onVendidos) { Txt("Vendidos hoy", 13, true) }
        }
        Spacer(Modifier.height(12.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(productos) { p ->
                val color = when {
                    p.stock <= 0 -> Rojo
                    p.stock <= 3 -> Rojo
                    else -> Verde
                }
                val estado = when {
                    p.stock <= 0 -> "Agotado"
                    p.stock <= 3 -> "Quedan ${p.stock}"
                    else -> "${p.stock} disponibles"
                }
                Caja(Modifier.fillMaxWidth()) {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        IconoProducto(p.icono)
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Txt(p.nombre, 15, true, align = TextAlign.Start)
                            Txt(Formato.pesos(p.precio), 14, color = NaranjaOscuro, align = TextAlign.Start)
                        }
                        Box(
                            Modifier.clip(RoundedCornerShape(50)).background(color.copy(alpha = 0.12f))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) { Txt(estado, 12, true, color = color) }
                    }
                }
            }
        }
    }
}