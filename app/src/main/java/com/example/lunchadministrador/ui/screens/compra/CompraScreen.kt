package com.example.lunchadministrador.ui.screens.compra

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lunchadministrador.data.local.entity.ProductoEntity
import com.example.lunchadministrador.ui.components.*
import com.example.lunchadministrador.ui.fabrica
import com.example.lunchadministrador.ui.theme.Naranja
import com.example.lunchadministrador.ui.theme.NaranjaOscuro
import com.example.lunchadministrador.ui.theme.Superficie
import com.example.lunchadministrador.ui.theme.TextoSecundario
import com.example.lunchadministrador.util.Formato

@Composable
fun CompraScreen(vm: CompraViewModel = viewModel(factory = fabrica { CompraViewModel(it.lunch, it.auth) })) {
    val productos by vm.productos.collectAsState()
    val ventasMes by vm.ventasMes.collectAsState()
    val masVendido = productos.maxByOrNull { ventasMes[it.id] ?: 0 }
    var elegido by remember { mutableStateOf<ProductoEntity?>(null) }
    var paso by remember { mutableIntStateOf(0) } // 0 = cerrado · 1 = detalle · 2 = confirmar · 3 = comprado

    Column(Modifier.fillMaxSize().padding(12.dp)) {
        Caja { Txt("Más vendido este mes", 14, true) }
        Spacer(Modifier.height(8.dp))
        masVendido?.let { p ->
            Caja(Modifier.fillMaxWidth(), onClick = { elegido = p; paso = 1 }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconoProducto(p.icono, 56)
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Txt(p.nombre, 14, true, align = TextAlign.Start)
                        Txt("Cantidad: ${p.stock}", 13, align = TextAlign.Start)
                        Txt("Coste: ${Formato.pesos(p.precio)}", 13, align = TextAlign.Start)
                    }
                    Icon(Icons.Default.ShoppingCart, "Comprar", tint = NaranjaOscuro)
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        LazyVerticalGrid(GridCells.Fixed(3), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(productos) { p ->
                Column(
                    Modifier.fillMaxWidth().clickable { elegido = p; paso = 1 }
                        .border(BorderStroke(2.dp, Naranja), RoundedCornerShape(8.dp)).padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(p.icono, fontSize = 30.sp)
                    Txt(p.nombre, 11)
                }
            }
        }
    }

    val p = elegido
    if (p != null && paso > 0) {
        fun cerrar() { paso = 0; elegido = null }
        AlertDialog(
            onDismissRequest = ::cerrar,
            containerColor = Superficie,
            confirmButton = {
                when (paso) {
                    1 -> Caja(onClick = { paso = 2 }) { Txt("Comprar", 14, true) }
                    2 -> Caja(onClick = { vm.comprar(p.id); paso = 3 }) { Txt("Aceptar", 14, true) }
                    else -> Caja(onClick = ::cerrar) { Txt("Cerrar ventana", 14, true) }
                }
            },
            dismissButton = { if (paso < 3) TextButton(onClick = ::cerrar) { Text("Cancelar", color = TextoSecundario) } },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    IconoProducto(p.icono, if (paso == 1) 90 else 64)
                    Spacer(Modifier.height(8.dp))
                    when (paso) {
                        1 -> {
                            Txt(p.nombre, 16, true)
                            Spacer(Modifier.height(8.dp))
                            Caja { Txt("Cantidad de ventas del mes: ${ventasMes[p.id] ?: 0}", 13) }
                            Spacer(Modifier.height(6.dp))
                            Caja { Txt("Productos necesarios:\n" + p.listaNecesarios.joinToString("\n"), 13) }
                            Spacer(Modifier.height(6.dp))
                            Caja { Txt("Precio del producto: ${Formato.pesos(p.precio)}", 13) }
                        }
                        2 -> Caja { Txt("Comprar materiales de este producto?", 15) }
                        else -> Caja { Txt("¡Materiales comprados!", 15, true) }
                    }
                }
            }
        )
    }
}
