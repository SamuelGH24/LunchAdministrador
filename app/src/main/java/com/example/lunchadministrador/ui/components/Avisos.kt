package com.example.lunchadministrador.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.lunchadministrador.ui.theme.Carbon

/** Permite que cualquier pantalla muestre un aviso corto abajo sin pasarlo por parámetros. */
val LocalSnackbar = staticCompositionLocalOf<SnackbarHostState> { error("Falta el SnackbarHostState") }

@Composable
fun AvisoHost(estado: SnackbarHostState, modifier: Modifier = Modifier) {
    SnackbarHost(estado, modifier) { datos ->
        Snackbar(
            snackbarData = datos,
            shape = RoundedCornerShape(14.dp),
            containerColor = Carbon,
            contentColor = Color.White
        )
    }
}