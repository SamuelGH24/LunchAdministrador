package com.example.lunchadministrador.ui.screens.cuenta

import com.example.lunchadministrador.ui.theme.Carbon

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lunchadministrador.data.model.Rol
import com.example.lunchadministrador.ui.components.*
import com.example.lunchadministrador.ui.fabrica
import com.example.lunchadministrador.ui.theme.Naranja
import com.example.lunchadministrador.ui.theme.Rojo
import com.example.lunchadministrador.ui.theme.Superficie
import com.example.lunchadministrador.ui.theme.TextoSecundario
import com.example.lunchadministrador.util.Formato

@Composable
fun CuentaScreen(
    onSalir: () -> Unit,
    vm: CuentaViewModel = viewModel(factory = fabrica { CuentaViewModel(it.auth, it.lunch) })
) {
    val usuario by vm.usuario.collectAsState()
    val vendidos by vm.vendidosHoy.collectAsState()
    var dialogo by remember { mutableStateOf<String?>(null) } // "correo" | "clave"
    var campo1 by remember { mutableStateOf("") }
    var campo2 by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    val u = usuario ?: return

    Column(Modifier.fillMaxSize().padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(80.dp).border(BorderStroke(2.dp, Naranja), RoundedCornerShape(6.dp)), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Person, "Foto de usuario", tint = Carbon, modifier = Modifier.size(48.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Caja { Txt(u.nombre, 14, true) }
                Caja { Txt(if (u.rolEnum == Rol.ADMIN) "Administrador" else "Mesero", 14) }
            }
        }
        Spacer(Modifier.height(6.dp))
        Txt(u.correo, 12, color = TextoSecundario)
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Caja(Modifier.weight(1f)) { Txt("Tiempo en la compañía:\n${Formato.tiempoEnCompania(u.creadoEn)}", 13) }
            Caja(Modifier.weight(1f)) { Txt("Productos vendidos hoy:\n$vendidos", 13) }
        }
        Spacer(Modifier.height(10.dp))
        Caja(Modifier.fillMaxWidth().height(110.dp)) { Txt("Sus horarios de trabajo son:\n(aún no se registran)", 13, color = TextoSecundario) }
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Caja(Modifier.weight(1f), onClick = { campo1 = ""; campo2 = ""; error = null; dialogo = "clave" }) { Txt("Cambio de contraseña", 13) }
            Caja(Modifier.weight(1f), onClick = { campo1 = u.correo; error = null; dialogo = "correo" }) { Txt("Cambio de correo", 13) }
        }
        Spacer(Modifier.weight(1f))
        Caja(borde = Rojo, onClick = onSalir) { Txt("Cerrar sesión", 14) }
    }

    dialogo?.let { d ->
        AlertDialog(
            onDismissRequest = { dialogo = null },
            containerColor = Superficie,
            title = { Txt(if (d == "correo") "Nuevo correo" else "Cambiar contraseña", 16, true) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (d == "correo") {
                        OutlinedTextField(campo1, { campo1 = it }, label = { Text("Correo") }, singleLine = true, colors = coloresCampo())
                    } else {
                        OutlinedTextField(campo1, { campo1 = it }, label = { Text("Contraseña actual") }, singleLine = true,
                            visualTransformation = PasswordVisualTransformation(), colors = coloresCampo())
                        OutlinedTextField(campo2, { campo2 = it }, label = { Text("Nueva contraseña") }, singleLine = true,
                            visualTransformation = PasswordVisualTransformation(), colors = coloresCampo())
                    }
                    error?.let { Txt(it, 12, color = Rojo) }
                }
            },
            confirmButton = {
                Caja(onClick = {
                    val alTerminar: (String?) -> Unit = { msg -> if (msg == null) dialogo = null else error = msg }
                    if (d == "correo") vm.cambiarCorreo(campo1, alTerminar) else vm.cambiarClave(campo1, campo2, alTerminar)
                }) { Txt("Guardar", 14, true) }
            },
            dismissButton = { TextButton(onClick = { dialogo = null }) { Text("Cancelar", color = TextoSecundario) } }
        )
    }
}
