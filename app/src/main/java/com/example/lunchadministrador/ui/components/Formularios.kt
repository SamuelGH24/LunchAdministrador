package com.example.lunchadministrador.ui.components

import com.example.lunchadministrador.ui.theme.Carbon

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.lunchadministrador.ui.theme.Rojo
import com.example.lunchadministrador.ui.theme.Superficie
import com.example.lunchadministrador.ui.theme.TextoSecundario
import com.example.lunchadministrador.ui.theme.Naranja

@Composable
fun coloresCampo() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Carbon, unfocusedTextColor = Carbon,
    focusedBorderColor = Naranja, unfocusedBorderColor = Naranja,
    focusedLabelColor = Naranja, unfocusedLabelColor = TextoSecundario,
    cursorColor = Carbon, errorBorderColor = Rojo
)

/** Ventana emergente con un mensaje y un botón de cierre (confirmaciones y alertas de los mockups). */
@Composable
fun DialogoMensaje(mensaje: String, boton: String = "Cerrar ventana", onCerrar: () -> Unit) {
    AlertDialog(
        onDismissRequest = onCerrar,
        containerColor = Superficie,
        text = { Txt(mensaje, 16, modifier = Modifier.fillMaxWidth()) },
        confirmButton = { Caja(onClick = onCerrar) { Txt(boton, 14, true) } }
    )
}
