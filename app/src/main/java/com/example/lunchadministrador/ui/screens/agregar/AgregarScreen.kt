package com.example.lunchadministrador.ui.screens.agregar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lunchadministrador.data.local.entity.ProductoEntity
import com.example.lunchadministrador.ui.components.*
import com.example.lunchadministrador.ui.fabrica
import com.example.lunchadministrador.ui.theme.NaranjaOscuro
import com.example.lunchadministrador.ui.theme.Superficie
import com.example.lunchadministrador.ui.theme.TextoSecundario
import com.example.lunchadministrador.util.Formato
import kotlinx.coroutines.launch

@Composable
fun AgregarScreen(
    mesa: Int,
    onVolver: () -> Unit,
    vm: AgregarViewModel = viewModel(factory = fabrica { AgregarViewModel(it.lunch, it.auth) })
) {
    val productos by vm.productos.collectAsState()
    val ventasMes by vm.ventasMes.collectAsState()
    var elegido by remember { mutableStateOf<ProductoEntity?>(null) }
    val snackbar = LocalSnackbar.current
    val scope = rememberCoroutineScope()

    Column(Modifier.fillMaxSize().padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Caja { Txt("¿Qué producto desea añadir el cliente?", 15, true) }
        Spacer(Modifier.height(4.dp))
        Txt("Mesa $mesa", 13, color = NaranjaOscuro)
        Spacer(Modifier.height(12.dp))
        LazyVerticalGrid(
            GridCells.Fixed(2), Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(productos) { p ->
                Column(
                    Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp)).background(Superficie)
                        .clickable { elegido = p }.padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    FotoProducto(
                        p.icono,
                        Modifier.fillMaxWidth().aspectRatio(1.3f).clip(RoundedCornerShape(8.dp)),
                        emojiSize = 40
                    )
                    Spacer(Modifier.height(6.dp))
                    Txt(p.nombre, 14, true)
                    Txt(Formato.pesos(p.precio), 13, color = NaranjaOscuro)
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Caja(Modifier.heightIn(min = 48.dp), onClick = onVolver) { Txt("Volver a la mesa $mesa", 14) }
    }

    elegido?.let { p ->
        AlertDialog(
            onDismissRequest = { elegido = null },
            containerColor = Superficie,
            confirmButton = {
                Caja(Modifier.heightIn(min = 48.dp), onClick = {
                    vm.agregar(mesa, p.id)
                    elegido = null
                    scope.launch {
                        snackbar.currentSnackbarData?.dismiss()
                        snackbar.showSnackbar("${p.nombre} añadido a la mesa $mesa")
                    }
                }) { Txt("Añadir", 14, true) }
            },
            dismissButton = { TextButton(onClick = { elegido = null }) { Text("Cerrar", color = TextoSecundario) } },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    IconoProducto(p.icono, 110)
                    Spacer(Modifier.height(8.dp))
                    Txt(p.nombre, 16, true)
                    Spacer(Modifier.height(8.dp))
                    Caja { Txt("Cantidad de ventas del mes: ${ventasMes[p.id] ?: 0}", 13) }
                    Spacer(Modifier.height(6.dp))
                    Caja { Txt("Productos necesarios:\n" + p.listaNecesarios.joinToString("\n"), 13) }
                    Spacer(Modifier.height(6.dp))
                    Caja { Txt("Precio: ${Formato.pesos(p.precio)}", 13) }
                }
            }
        )
    }
}