package com.example.lunchadministrador.ui.screens.productos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lunchadministrador.ui.components.*
import com.example.lunchadministrador.ui.fabrica
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
            Caja { Txt("Lista de productos", 15, true) }
            if (esAdmin) Caja(onClick = onVendidos) { Txt("Vendidos hoy", 13) }
        }
        Spacer(Modifier.height(12.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(productos) { p ->
                Caja(Modifier.fillMaxWidth()) {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        IconoProducto(p.icono)
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Txt(p.nombre, 14, true, align = TextAlign.Start)
                            Txt("Cantidad: ${p.stock}", 13, align = TextAlign.Start)
                            Txt("Coste: ${Formato.pesos(p.precio)}", 13, align = TextAlign.Start)
                        }
                    }
                }
            }
        }
    }
}
