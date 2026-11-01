package com.example.lunchadministrador.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EsquemaClaro = lightColorScheme(
    primary = Naranja, secondary = NaranjaOscuro, tertiary = Verde,
    background = Fondo, surface = Superficie, error = Rojo,
    onPrimary = Color.White, onSecondary = Color.White,
    onBackground = Carbon, onSurface = Carbon
)

/** Único lugar donde se define el diseño global: colores y tipografía. */
@Composable
fun LunchAdministradorTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = EsquemaClaro, typography = Tipografia, content = content)
}
