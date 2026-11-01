package com.example.lunchadministrador.ui.screens.login

import com.example.lunchadministrador.ui.theme.Carbon

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lunchadministrador.data.local.Sembrador
import com.example.lunchadministrador.ui.components.Caja
import com.example.lunchadministrador.ui.components.Txt
import com.example.lunchadministrador.ui.components.coloresCampo
import com.example.lunchadministrador.ui.fabrica
import com.example.lunchadministrador.ui.theme.Naranja
import com.example.lunchadministrador.ui.theme.Rojo
import com.example.lunchadministrador.ui.theme.TextoSecundario
import com.example.lunchadministrador.ui.theme.Verde

@Composable
fun LoginScreen(
    onIngreso: () -> Unit,
    onRegistro: () -> Unit,
    vm: LoginViewModel = viewModel(factory = fabrica { LoginViewModel(it.auth) })
) {
    Column(
        Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            Modifier.size(120.dp).clip(CircleShape).border(BorderStroke(3.dp, Naranja), CircleShape),
            contentAlignment = Alignment.Center
        ) { Icon(Icons.Default.Person, "Usuario", tint = Carbon, modifier = Modifier.size(64.dp)) }
        Spacer(Modifier.height(32.dp))

        OutlinedTextField(
            value = vm.correo, onValueChange = { vm.correo = it },
            label = { Text("Correo") }, singleLine = true,
            leadingIcon = { Icon(Icons.Default.Email, null, tint = Naranja) },
            colors = coloresCampo(), modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = vm.clave, onValueChange = { vm.clave = it },
            label = { Text("Contraseña") }, singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            leadingIcon = { Icon(Icons.Default.Lock, null, tint = Naranja) },
            colors = coloresCampo(), modifier = Modifier.fillMaxWidth()
        )
        vm.error?.let {
            Spacer(Modifier.height(8.dp))
            Txt(it, 13, color = Rojo)
        }
        Spacer(Modifier.height(24.dp))
        Caja(onClick = { if (!vm.cargando) vm.ingresar(onIngreso) }) {
            Txt(if (vm.cargando) "Ingresando..." else "Iniciar sesión", 16, true)
        }
        Spacer(Modifier.height(16.dp))
        Caja(borde = Naranja, onClick = onRegistro) { Txt("Crear cuenta de mesero", 14) }
        Spacer(Modifier.height(24.dp))
        Txt(
            "Pruebas:\n${Sembrador.ADMIN_CORREO} / ${Sembrador.ADMIN_CLAVE}\n${Sembrador.MESERO_CORREO} / ${Sembrador.MESERO_CLAVE}",
            12, color = TextoSecundario
        )
    }
}
