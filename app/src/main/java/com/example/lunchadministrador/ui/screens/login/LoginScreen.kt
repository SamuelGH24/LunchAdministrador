package com.example.lunchadministrador.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lunchadministrador.data.local.Sembrador
import com.example.lunchadministrador.ui.components.Boton
import com.example.lunchadministrador.ui.components.BotonSecundario
import com.example.lunchadministrador.ui.components.Txt
import com.example.lunchadministrador.ui.components.coloresCampo
import com.example.lunchadministrador.ui.fabrica
import com.example.lunchadministrador.ui.theme.Naranja
import com.example.lunchadministrador.ui.theme.NaranjaClaro
import com.example.lunchadministrador.ui.theme.Rojo
import com.example.lunchadministrador.ui.theme.TextoSecundario

@Composable
fun LoginScreen(
    onIngreso: () -> Unit,
    onRegistro: () -> Unit,
    vm: LoginViewModel = viewModel(factory = fabrica { LoginViewModel(it.auth) })
) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        // Cabecera con degradado
        Box(
            Modifier.fillMaxWidth().height(250.dp)
                .clip(RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp))
                .background(Brush.verticalGradient(listOf(NaranjaClaro, Naranja))),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(Modifier.size(92.dp).clip(CircleShape).background(Color.White), contentAlignment = Alignment.Center) {
                    Text("🍽️", fontSize = 46.sp)
                }
                Spacer(Modifier.height(14.dp))
                Text("Lunch Administrador", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                Text("Mesas, pedidos y cobros", color = Color.White, fontSize = 14.sp)
            }
        }

        Column(Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 24.dp)) {
            Txt("Iniciar sesión", 22, true, align = TextAlign.Start, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = vm.correo, onValueChange = { vm.correo = it },
                label = { Text("Correo") }, singleLine = true,
                shape = RoundedCornerShape(14.dp),
                leadingIcon = { Icon(Icons.Default.Email, null, tint = Naranja) },
                colors = coloresCampo(), modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = vm.clave, onValueChange = { vm.clave = it },
                label = { Text("Contraseña") }, singleLine = true,
                shape = RoundedCornerShape(14.dp),
                visualTransformation = PasswordVisualTransformation(),
                leadingIcon = { Icon(Icons.Default.Lock, null, tint = Naranja) },
                colors = coloresCampo(), modifier = Modifier.fillMaxWidth()
            )
            vm.error?.let {
                Spacer(Modifier.height(8.dp))
                Txt(it, 13, color = Rojo, align = TextAlign.Start, modifier = Modifier.fillMaxWidth())
            }
            Spacer(Modifier.height(20.dp))
            Boton(if (vm.cargando) "Ingresando..." else "Iniciar sesión", onClick = { if (!vm.cargando) vm.ingresar(onIngreso) })
            Spacer(Modifier.height(12.dp))
            BotonSecundario("Crear cuenta de mesero", onClick = onRegistro)
            Spacer(Modifier.height(20.dp))
            Txt(
                "Cuentas de prueba:\n${Sembrador.ADMIN_CORREO} / ${Sembrador.ADMIN_CLAVE}\n${Sembrador.MESERO_CORREO} / ${Sembrador.MESERO_CLAVE}",
                12, color = TextoSecundario, modifier = Modifier.fillMaxWidth()
            )
        }
    }
}