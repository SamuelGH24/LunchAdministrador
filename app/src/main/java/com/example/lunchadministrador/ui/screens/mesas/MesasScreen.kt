package com.example.lunchadministrador.ui.screens.mesas

import com.example.lunchadministrador.ui.theme.Carbon

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lunchadministrador.ui.components.*
import com.example.lunchadministrador.ui.fabrica
import com.example.lunchadministrador.ui.theme.Naranja
import com.example.lunchadministrador.ui.theme.Rojo
import com.example.lunchadministrador.ui.theme.TextoSecundario
import com.example.lunchadministrador.ui.theme.Verde
import com.example.lunchadministrador.util.Formato

@Composable
fun MesasScreen(
    onAgregar: (Int) -> Unit,
    onHistorial: (Int) -> Unit,
    vm: MesasViewModel = viewModel(factory = fabrica { MesasViewModel(it.lunch) })
) {
    val mesas by vm.mesas.collectAsState()
    var sel by rememberSaveable { mutableIntStateOf(1) }
    val mesa = mesas.firstOrNull { it.numero == sel } ?: return

    Column(Modifier.fillMaxSize().padding(horizontal = 12.dp)) {
        PestanasMesa(sel) { sel = it }
        Row(
            Modifier.fillMaxWidth().padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CirculoMesa("MESA ${mesa.numero}")
                Spacer(Modifier.height(4.dp))
                Txt(if (mesa.ocupada) "Ocupada" else "Libre", 13, true, color = if (mesa.ocupada) Rojo else Verde)
                if (mesa.ocupada) Txt("${mesa.atendidoPor} · ${Formato.hora(mesa.desde)}", 11, color = TextoSecundario)
            }
            Icon(Icons.Default.Add, null, tint = Naranja, modifier = Modifier.size(48.dp))
            Caja(onClick = { onAgregar(mesa.numero) }) { Txt("ADD?", 14) }
        }

        LazyColumn(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (!mesa.ocupada) item {
                Txt("Esta mesa no tiene productos", 14, color = TextoSecundario, modifier = Modifier.fillMaxWidth().padding(24.dp))
            }
            items(mesa.lineas) { l ->
                Caja(Modifier.fillMaxWidth()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconoProducto(l.icono)
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Txt(l.nombre, 14, true, align = TextAlign.Start)
                            Txt("Cantidad: ${l.cantidad}", 13, align = TextAlign.Start)
                            Txt("Coste: ${Formato.pesos(l.precio * l.cantidad)}", 13, align = TextAlign.Start)
                        }
                        IconButton(onClick = { vm.quitar(mesa.numero, l.productoId) }) {
                            Icon(Icons.Default.Delete, "Quitar una unidad de ${l.nombre}", tint = Rojo)
                        }
                    }
                }
            }
        }
        Row(
            Modifier.fillMaxWidth().padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
        ) {
            Text("⏳", fontSize = 28.sp, color = Carbon)
            Spacer(Modifier.width(12.dp))
            Caja(onClick = { onHistorial(mesa.numero) }) { Txt("Historial de la mesa", 14) }
        }
    }
}
