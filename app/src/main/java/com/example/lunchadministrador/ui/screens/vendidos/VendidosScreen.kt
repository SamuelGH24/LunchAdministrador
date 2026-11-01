package com.example.lunchadministrador.ui.screens.vendidos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lunchadministrador.ui.components.*
import com.example.lunchadministrador.ui.fabrica
import com.example.lunchadministrador.ui.theme.TextoSecundario
import com.example.lunchadministrador.util.Formato

@Composable
fun VendidosScreen(vm: VendidosViewModel = viewModel(factory = fabrica { VendidosViewModel(it.lunch) })) {
    val vendidos by vm.vendidos.collectAsState()
    val total = vendidos.sumOf { it.subtotal }

    Column(Modifier.fillMaxSize().padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Caja { Txt("Productos vendidos hoy\ntotal facturado: ${Formato.pesos(total)}", 15, true) }
        Spacer(Modifier.height(12.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (vendidos.isEmpty()) item {
                Txt("Aún no se ha cerrado ninguna mesa hoy", 14, color = TextoSecundario, modifier = Modifier.padding(24.dp))
            }
            items(vendidos) { v ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconoProducto(v.icono, 56)
                    Spacer(Modifier.width(12.dp))
                    Caja(Modifier.weight(1f)) { Txt("${v.nombre}\nCantidad: ${v.cantidad}\nPrecio: ${Formato.pesos(v.subtotal)}", 13) }
                }
            }
        }
    }
}
