package com.example.lunchadministrador.ui.screens.historial

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.lunchadministrador.ui.components.*
import com.example.lunchadministrador.ui.fabrica
import com.example.lunchadministrador.ui.theme.Naranja
import com.example.lunchadministrador.ui.theme.TextoSecundario
import com.example.lunchadministrador.util.Formato

@Composable
fun HistorialScreen(
    mesaInicial: Int,
    onVolver: () -> Unit,
    vm: HistorialViewModel = viewModel(factory = fabrica { HistorialViewModel(it.lunch) })
) {
    var sel by rememberSaveable { mutableIntStateOf(mesaInicial) }
    val pedidos = vm.historial(sel).collectAsLazyPagingItems()
    val vacio = pedidos.loadState.refresh is LoadState.NotLoading && pedidos.itemCount == 0

    Column(Modifier.fillMaxSize().padding(horizontal = 12.dp)) {
        PestanasMesa(sel) { sel = it }
        Caja(Modifier.fillMaxWidth()) { Txt("Productos vendidos mesa $sel", 14, true) }
        Spacer(Modifier.height(8.dp))
        LazyColumn(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            if (vacio) item {
                Txt("Todavía no hay clientes atendidos hoy en esta mesa", 14, color = TextoSecundario,
                    modifier = Modifier.fillMaxWidth().padding(24.dp))
            }
            items(count = pedidos.itemCount, key = pedidos.itemKey { it.pedido.id }) { index ->
                val p = pedidos[index]
                if (p != null) {
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
            // Indicador mientras carga la primera página o la siguiente
            if (pedidos.loadState.refresh is LoadState.Loading || pedidos.loadState.append is LoadState.Loading) item {
                Box(Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Naranja)
                }
            }
        }
        Boton("Volver a la mesa $sel", onClick = onVolver, modifier = Modifier.padding(vertical = 10.dp))
    }
}