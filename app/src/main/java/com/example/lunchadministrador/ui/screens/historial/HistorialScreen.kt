package com.example.lunchadministrador.ui.screens.historial

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lunchadministrador.data.local.dao.PedidoConLineas
import com.example.lunchadministrador.ui.components.*
import com.example.lunchadministrador.ui.fabrica
import com.example.lunchadministrador.ui.theme.TextoSecundario
import com.example.lunchadministrador.util.Formato

@Composable
fun HistorialScreen(
    mesaInicial: Int,
    vm: HistorialViewModel = viewModel(factory = fabrica { HistorialViewModel(it.lunch) })
) {
    var sel by rememberSaveable { mutableIntStateOf(mesaInicial) }
    val pedidos by remember(sel) { vm.historial(sel) }.collectAsState(initial = emptyList<PedidoConLineas>())

    Column(Modifier.fillMaxSize().padding(horizontal = 12.dp)) {
        PestanasMesa(sel) { sel = it }
        Caja(Modifier.fillMaxWidth()) { Txt("Productos vendidos mesa $sel", 14, true) }
        Spacer(Modifier.height(8.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            if (pedidos.isEmpty()) item {
                Txt("Todavía no hay clientes atendidos hoy en esta mesa", 14, color = TextoSecundario,
                    modifier = Modifier.fillMaxWidth().padding(24.dp))
            }
            items(pedidos) { p ->
                Column {
                    Caja { Txt("${Formato.hora(p.pedido.fecha)} cliente ${p.pedido.cliente}  ·  ${Formato.pesos(p.pedido.total)}", 13) }
                    Spacer(Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        p.lineas.forEach { l ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                IconoProducto(l.icono, 48)
                                Txt("x${l.cantidad}", 12)
                            }
                        }
                    }
                }
            }
        }
    }
}
