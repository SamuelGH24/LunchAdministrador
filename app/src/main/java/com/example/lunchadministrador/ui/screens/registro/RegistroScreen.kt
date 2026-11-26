package com.example.lunchadministrador.ui.screens.registro

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lunchadministrador.ui.components.Boton
import com.example.lunchadministrador.ui.components.BotonSecundario
import com.example.lunchadministrador.ui.components.Txt
import com.example.lunchadministrador.ui.components.coloresCampo
import com.example.lunchadministrador.ui.fabrica
import com.example.lunchadministrador.ui.theme.Rojo
import com.example.lunchadministrador.ui.theme.TextoSecundario

@Composable
fun RegistroScreen(
    onRegistrado: () -> Unit,
    onVolver: () -> Unit,
    vm: RegistroViewModel = viewModel(factory = fabrica { RegistroViewModel(it.auth) })
) {
    val forma = RoundedCornerShape(14.dp)
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp), verticalArrangement = Arrangement.Center) {
        Txt("Crear cuenta", 26, true, align = TextAlign.Start, modifier = Modifier.fillMaxWidth())
        Txt("Las cuentas nuevas son de mesero", 14, color = TextoSecundario, align = TextAlign.Start, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(20.dp))
        OutlinedTextField(vm.nombre, { vm.nombre = it }, label = { Text("Nombre") }, singleLine = true,
            shape = forma, colors = coloresCampo(), modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(vm.correo, { vm.correo = it }, label = { Text("Correo") }, singleLine = true,
            shape = forma, colors = coloresCampo(), modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(vm.clave, { vm.clave = it }, label = { Text("Contraseña (mínimo 6)") }, singleLine = true,
            shape = forma, visualTransformation = PasswordVisualTransformation(), colors = coloresCampo(), modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(vm.confirmar, { vm.confirmar = it }, label = { Text("Confirmar contraseña") }, singleLine = true,
            shape = forma, visualTransformation = PasswordVisualTransformation(), colors = coloresCampo(), modifier = Modifier.fillMaxWidth())
        vm.error?.let {
            Spacer(Modifier.height(8.dp))
            Txt(it, 13, color = Rojo, align = TextAlign.Start, modifier = Modifier.fillMaxWidth())
        }
        Spacer(Modifier.height(20.dp))
        Boton("Registrarme", onClick = { vm.registrar(onRegistrado) })
        Spacer(Modifier.height(12.dp))
        BotonSecundario("Volver al inicio de sesión", onClick = onVolver)
    }
}