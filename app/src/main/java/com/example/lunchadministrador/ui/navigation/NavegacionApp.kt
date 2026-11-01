package com.example.lunchadministrador.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.compose.foundation.layout.Box
import com.example.lunchadministrador.data.model.Rol
import com.example.lunchadministrador.data.repository.AuthRepository
import com.example.lunchadministrador.ui.components.BarraSuperior
import com.example.lunchadministrador.ui.components.Caja
import com.example.lunchadministrador.ui.components.Txt
import com.example.lunchadministrador.ui.screens.agregar.AgregarScreen
import com.example.lunchadministrador.ui.screens.cobro.CobroScreen
import com.example.lunchadministrador.ui.screens.compra.CompraScreen
import com.example.lunchadministrador.ui.screens.cuenta.CuentaScreen
import com.example.lunchadministrador.ui.screens.historial.HistorialScreen
import com.example.lunchadministrador.ui.screens.login.LoginScreen
import com.example.lunchadministrador.ui.screens.mesas.MesasScreen
import com.example.lunchadministrador.ui.screens.productos.ProductosScreen
import com.example.lunchadministrador.ui.screens.registro.RegistroScreen
import com.example.lunchadministrador.ui.screens.vendidos.VendidosScreen
import com.example.lunchadministrador.ui.theme.Naranja
import com.example.lunchadministrador.ui.theme.Superficie

@Composable
fun NavegacionApp(auth: AuthRepository) {
    val listo by auth.listo.collectAsState()
    val usuario by auth.usuario.collectAsState()

    // Mientras se recupera la sesión guardada no sabemos si mostrar Login o Mesas.
    if (!listo) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = Naranja) }
        return
    }

    val nav = rememberNavController()
    val inicio = remember { if (usuario != null) Rutas.MESAS else Rutas.LOGIN }
    val ruta = nav.currentBackStackEntryAsState().value?.destination?.route
    var sinPermiso by remember { mutableStateOf(false) }

    fun ir(destino: String) {
        // La compra de materiales es solo del Administrador ("No eres Jefe de compra").
        if (destino == Rutas.COMPRA && usuario?.rolEnum != Rol.ADMIN) { sinPermiso = true; return }
        nav.navigate(destino) {
            popUpTo(Rutas.MESAS) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    Column(Modifier.fillMaxSize()) {
        if (ruta != null && ruta != Rutas.LOGIN && ruta != Rutas.REGISTRO) BarraSuperior(ruta) { ir(it) }

        NavHost(nav, startDestination = inicio, modifier = Modifier.weight(1f)) {
            composable(Rutas.LOGIN) {
                LoginScreen(
                    onIngreso = { nav.navigate(Rutas.MESAS) { popUpTo(Rutas.LOGIN) { inclusive = true } } },
                    onRegistro = { nav.navigate(Rutas.REGISTRO) }
                )
            }
            composable(Rutas.REGISTRO) {
                RegistroScreen(
                    onRegistrado = { nav.navigate(Rutas.MESAS) { popUpTo(Rutas.LOGIN) { inclusive = true } } },
                    onVolver = { nav.popBackStack() }
                )
            }
            composable(Rutas.MESAS) {
                MesasScreen(
                    onAgregar = { nav.navigate(Rutas.agregar(it)) },
                    onHistorial = { nav.navigate(Rutas.historial(it)) }
                )
            }
            composable(Rutas.AGREGAR, listOf(navArgument("mesa") { type = NavType.IntType })) {
                AgregarScreen(mesa = it.arguments?.getInt("mesa") ?: 1, onVolver = { nav.popBackStack() })
            }
            composable(Rutas.HISTORIAL, listOf(navArgument("mesa") { type = NavType.IntType })) {
                HistorialScreen(mesaInicial = it.arguments?.getInt("mesa") ?: 1)
            }
            composable(Rutas.COBRO) { CobroScreen() }
            composable(Rutas.PRODUCTOS) {
                ProductosScreen(esAdmin = usuario?.rolEnum == Rol.ADMIN, onVendidos = { nav.navigate(Rutas.VENDIDOS) })
            }
            composable(Rutas.VENDIDOS) { VendidosScreen() }
            composable(Rutas.COMPRA) { CompraScreen() }
            composable(Rutas.CUENTA) {
                CuentaScreen(onSalir = {
                    auth.cerrarSesion()
                    nav.navigate(Rutas.LOGIN) { popUpTo(nav.graph.id) { inclusive = true } }
                })
            }
        }
    }

    if (sinPermiso) {
        AlertDialog(
            onDismissRequest = { sinPermiso = false },
            containerColor = Superficie,
            icon = { Icon(Icons.Default.Warning, "Advertencia", tint = Naranja, modifier = Modifier.size(56.dp)) },
            text = { Txt("No eres Jefe de compra", 16, true) },
            confirmButton = { Caja(onClick = { sinPermiso = false }) { Txt("Cerrar", 14, true) } }
        )
    }
}
