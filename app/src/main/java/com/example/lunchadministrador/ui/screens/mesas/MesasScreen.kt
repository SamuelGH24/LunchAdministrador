package com.example.lunchadministrador.ui.screens.mesas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lunchadministrador.data.model.LineaMesa
import com.example.lunchadministrador.ui.components.*
import com.example.lunchadministrador.ui.fabrica
import com.example.lunchadministrador.ui.theme.Rojo
import com.example.lunchadministrador.ui.theme.Superficie
import com.example.lunchadministrador.ui.theme.TextoSecundario
import com.example.lunchadministrador.ui.theme.Verde
import com.example.lunchadministrador.util.Formato
import kotlinx.coroutines.launch

@Composable
fun MesasScreen(
    onAgregar: (Int) -> Unit,
    onHistorial: (Int) -> Unit,
    vm: MesasViewModel = viewModel(factory = fabrica { MesasViewModel(it.lunch) })
) {
    val mesas by vm.mesas.collectAsState()
    var sel by rememberSaveable { mutableIntStateOf(1) }
    var porQuitar by remember { mutableStateOf<LineaMesa?>(null) }
    val snackbar = LocalSnackbar.current
    val scope = rememberCoroutineScope()
    val mesa = mesas.firstOrNull { it.numero == sel } ?: return
    val colorEstado = if (mesa.ocupada) Rojo else Verde

    Column(Modifier.fillMaxSize().padding(horizontal = 12.dp)) {
        PestanasMesa(sel) { sel = it }

        // Resumen de la mesa
        Caja(Modifier.fillMaxWidth()) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                CirculoMesa("MESA ${mesa.numero}", borde = colorEstado)
                Spacer(Modifier.width(16.dp))
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(
                        Modifier.clip(RoundedCornerShape(50)).background(colorEstado.copy(alpha = 0.12f))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) { Txt(if (mesa.ocupada) "Ocupada" else "Libre", 13, true, color = colorEstado) }
                    if (mesa.ocupada) {
                        Txt("Atendida por ${mesa.atendidoPor}", 13, color = TextoSecundario, align = TextAlign.Start)
                        Txt("Desde las ${Formato.hora(mesa.desde)}", 13, color = TextoSecundario, align = TextAlign.Start)
                        Txt("Total: ${Formato.pesos(mesa.total)}", 18, true, align = TextAlign.Start)
                    } else {
                        Txt("Lista para recibir clientes", 13, color = TextoSecundario, align = TextAlign.Start)
                    }
                }
            }
        }

        Spacer(Modifier.height(10.dp))
        Boton("Añadir producto", onClick = { onAgregar(mesa.numero) })
        Spacer(Modifier.height(10.dp))

        LazyColumn(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (!mesa.ocupada) item {
                Txt("Esta mesa no tiene productos.\nToca «Añadir producto» para empezar.", 14, color = TextoSecundario,
                    modifier = Modifier.fillMaxWidth().padding(24.dp))
            }
            items(mesa.lineas) { l ->
                Caja(Modifier.fillMaxWidth()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconoProducto(l.icono)
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Txt(l.nombre, 14, true, align = TextAlign.Start)
                            Txt("Cantidad: ${l.cantidad}", 13, color = TextoSecundario, align = TextAlign.Start)
                            Txt("Coste: ${Formato.pesos(l.precio * l.cantidad)}", 13, align = TextAlign.Start)
                        }
                        IconButton(onClick = { porQuitar = l }) {
                            Icon(Icons.Default.Delete, "Quitar una unidad de ${l.nombre}", tint = Rojo)
                        }
                    }
                }
            }
        }

        BotonSecundario("Historial de la mesa", onClick = { onHistorial(mesa.numero) }, modifier = Modifier.padding(vertical = 10.dp))
    }

    porQuitar?.let { l ->
        AlertDialog(
            onDismissRequest = { porQuitar = null },
            containerColor = Superficie,
            text = { Txt("¿Quitar una unidad de ${l.nombre} de la mesa ${mesa.numero}?", 16, modifier = Modifier.fillMaxWidth()) },
            confirmButton = {
                Caja(Modifier.heightIn(min = 48.dp), borde = Rojo, onClick = {
                    vm.quitar(mesa.numero, l.productoId)
                    porQuitar = null
                    scope.launch {
                        snackbar.currentSnackbarData?.dismiss()
                        snackbar.showSnackbar("Se quitó una unidad de ${l.nombre}")
                    }
                }) { Txt("Quitar", 14, true, color = Rojo) }
            },
            dismissButton = {
                TextButton(onClick = { porQuitar = null }) { Text("Cancelar", color = TextoSecundario) }
            }
        )
    }
}