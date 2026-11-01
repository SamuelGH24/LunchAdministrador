package com.example.lunchadministrador.ui.screens.registro

import androidx.compose.foundation.layout.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lunchadministrador.ui.components.Caja
import com.example.lunchadministrador.ui.components.Txt
import com.example.lunchadministrador.ui.components.coloresCampo
import com.example.lunchadministrador.ui.fabrica
import com.example.lunchadministrador.ui.theme.Naranja
import com.example.lunchadministrador.ui.theme.Rojo

@Composable
fun RegistroScreen(
    onRegistrado: () -> Unit,
    onVolver: () -> Unit,
    vm: RegistroViewModel = viewModel(factory = fabrica { RegistroViewModel(it.auth) })
) {
    Column(
        Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Caja { Txt("Crear cuenta de mesero", 16, true) }
        Spacer(Modifier.height(20.dp))
        OutlinedTextField(vm.nombre, { vm.nombre = it }, label = { Text("Nombre") }, singleLine = true,
            colors = coloresCampo(), modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(vm.correo, { vm.correo = it }, label = { Text("Correo") }, singleLine = true,
            colors = coloresCampo(), modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(vm.clave, { vm.clave = it }, label = { Text("Contraseña (mínimo 6)") }, singleLine = true,
            visualTransformation = PasswordVisualTransformation(), colors = coloresCampo(), modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(vm.confirmar, { vm.confirmar = it }, label = { Text("Confirmar contraseña") }, singleLine = true,
            visualTransformation = PasswordVisualTransformation(), colors = coloresCampo(), modifier = Modifier.fillMaxWidth())
        vm.error?.let { Spacer(Modifier.height(8.dp)); Txt(it, 13, color = Rojo) }
        Spacer(Modifier.height(20.dp))
        Caja(onClick = { vm.registrar(onRegistrado) }) { Txt("Registrarme", 16, true) }
        Spacer(Modifier.height(12.dp))
        Caja(borde = Naranja, onClick = onVolver) { Txt("Volver al inicio de sesión", 14) }
    }
}
