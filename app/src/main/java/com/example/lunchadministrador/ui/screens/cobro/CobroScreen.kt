package com.example.lunchadministrador.ui.screens.cobro

import com.example.lunchadministrador.ui.theme.Gris

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import com.example.lunchadministrador.ui.theme.Verde
import com.example.lunchadministrador.util.Formato

@Composable
fun CobroScreen(vm: CobroViewModel = viewModel(factory = fabrica { CobroViewModel(it.lunch) })) {
    val mesas by vm.mesas.collectAsState()
    var sel by rememberSaveable { mutableStateOf<Int?>(null) }
    var pagada by remember { mutableStateOf<Int?>(null) }
    val mesa = mesas.firstOrNull { it.numero == sel }

    Column(Modifier.fillMaxSize().padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        if (mesa == null) {
            // Paso 1: ¿Qué mesa desea pagar?
            Caja { Txt("¿Qué mesa desea pagar?", 16, true) }
            Spacer(Modifier.height(24.dp))
            mesas.chunked(2).forEach { fila ->
                Row(horizontalArrangement = Arrangement.spacedBy(24.dp), modifier = Modifier.padding(vertical = 10.dp)) {
                    fila.forEach { m ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CirculoMesa(
                                "mesa ${m.numero}", borde = if (m.ocupada) Naranja else Gris,
                                onClick = if (m.ocupada) ({ sel = m.numero }) else null
                            )
                            Txt(if (m.ocupada) "Ocupada" else "Libre", 12, color = if (m.ocupada) Rojo else Verde)
                        }
                    }
                }
            }
        } else {
            // Paso 2: detalle y valor total
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                CirculoMesa("mesa ${mesa.numero}")
                Caja { Txt("Atendido por: ${mesa.atendidoPor}\nA las: ${Formato.hora(mesa.desde)}", 13) }
                IconButton(onClick = { sel = null }) { Icon(Icons.Default.Close, "Cerrar detalle", tint = Naranja) }
            }
            Spacer(Modifier.height(12.dp))
            LazyColumn(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(mesa.lineas) { l ->
                    Caja(Modifier.fillMaxWidth(), borde = Naranja) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(l.icono, fontSize = 28.sp)
                            Spacer(Modifier.width(10.dp))
                            Txt("${l.nombre} x${l.cantidad}", 14, align = TextAlign.Start, modifier = Modifier.weight(1f))
                            Txt(Formato.pesos(l.precio * l.cantidad), 14)
                        }
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Caja { Txt("Valor total: ${Formato.pesos(mesa.total)}", 16, true) }
            Spacer(Modifier.height(10.dp))
            Caja(onClick = { val n = mesa.numero; vm.cobrar(n); sel = null; pagada = n }) { Txt("Valor pagado?", 15, true) }
        }
    }

    // Paso 3: confirmación
    pagada?.let { n -> DialogoMensaje("Productos pagados, mesa $n ahora está libre para su uso!") { pagada = null } }
}
