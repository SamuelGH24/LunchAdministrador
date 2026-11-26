package com.example.lunchadministrador.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.example.lunchadministrador.ui.theme.Carbon
import com.example.lunchadministrador.ui.theme.Gris
import com.example.lunchadministrador.ui.theme.Naranja
import com.example.lunchadministrador.ui.theme.Superficie
import com.example.lunchadministrador.ui.theme.Tarjeta

/** Color de texto por defecto según el fondo donde esté (blanco sobre botones naranja). */
val LocalColorTexto = compositionLocalOf { Carbon }

@Composable
fun Txt(
    texto: String,
    size: Int = 14,
    negrita: Boolean = false,
    color: Color = Color.Unspecified,
    align: TextAlign = TextAlign.Center,
    modifier: Modifier = Modifier
) {
    Text(
        text = texto,
        color = if (color == Color.Unspecified) LocalColorTexto.current else color,
        fontSize = size.sp, textAlign = align, modifier = modifier,
        fontWeight = if (negrita) FontWeight.Bold else FontWeight.Normal
    )
}

/**
 * Tarjeta de la app.
 * - Sin onClick: tarjeta blanca con sombra suave (informativa).
 * - Con onClick: botón naranja relleno con texto blanco. Si se usa otro color de borde (ej. Rojo),
 *   queda como botón blanco con ese borde.
 */
@Composable
fun Caja(
    modifier: Modifier = Modifier,
    borde: Color = Naranja,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val forma = RoundedCornerShape(14.dp)
    if (onClick == null) {
        Box(
            modifier.shadow(2.dp, forma).background(Tarjeta, forma).padding(horizontal = 14.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center, content = content
        )
    } else {
        val principal = borde == Naranja
        var m = modifier.shadow(if (principal) 3.dp else 1.dp, forma).clip(forma).background(if (principal) Naranja else Tarjeta)
        if (!principal) m = m.border(BorderStroke(1.5.dp, borde), forma)
        Box(
            m.clickable(onClick = onClick).defaultMinSize(minHeight = 48.dp).padding(horizontal = 16.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            CompositionLocalProvider(LocalColorTexto provides (if (principal) Color.White else Carbon)) { content() }
        }
    }
}

/** Botón principal de formularios (ancho completo). */
@Composable
fun Boton(texto: String, onClick: () -> Unit, modifier: Modifier = Modifier, habilitado: Boolean = true) {
    Button(
        onClick = onClick, enabled = habilitado,
        modifier = modifier.fillMaxWidth().height(52.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Naranja, contentColor = Color.White, disabledContainerColor = Gris)
    ) { Text(texto, fontSize = 16.sp, fontWeight = FontWeight.Bold) }
}

/** Botón secundario (solo borde). */
@Composable
fun BotonSecundario(texto: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(52.dp),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.5.dp, Naranja),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Naranja)
    ) { Text(texto, fontSize = 15.sp, fontWeight = FontWeight.Bold) }
}

@Composable
fun CirculoMesa(texto: String, borde: Color = Naranja, onClick: (() -> Unit)? = null) {
    var m = Modifier.size(100.dp).shadow(4.dp, CircleShape).clip(CircleShape)
        .background(Tarjeta).border(BorderStroke(3.dp, borde), CircleShape)
    if (onClick != null) m = m.clickable(onClick = onClick)
    Box(m, contentAlignment = Alignment.Center) { Txt(texto, 14, true) }
}

/**
 * Imagen de un producto. Si [icono] es un link (http...) se carga la foto con Coil;
 * si falla o todavía carga, muestra un respaldo. Si es un emoji, lo dibuja directamente.
 * [descripcion] es el texto que leen los lectores de pantalla (accesibilidad).
 */
@Composable
fun FotoProducto(
    icono: String,
    modifier: Modifier = Modifier,
    emojiSize: Int = 24,
    descripcion: String? = null
) {
    if (icono.startsWith("http")) {
        SubcomposeAsyncImage(
            model = icono,
            contentDescription = descripcion,
            contentScale = ContentScale.Crop,
            modifier = modifier,
            loading = {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Naranja, strokeWidth = 2.dp, modifier = Modifier.size(20.dp))
                }
            },
            error = {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("🍽️", fontSize = emojiSize.sp)
                }
            }
        )
    } else {
        Box(modifier, contentAlignment = Alignment.Center) { Text(icono, fontSize = emojiSize.sp) }
    }
}

/** Recuadro con la foto (o emoji) del producto. */
@Composable
fun IconoProducto(icono: String, size: Int = 64, descripcion: String? = null) {
    Box(
        Modifier.size(size.dp).clip(RoundedCornerShape(12.dp)).background(Superficie),
        contentAlignment = Alignment.Center
    ) { FotoProducto(icono, Modifier.fillMaxSize(), emojiSize = (size / 2.6).toInt(), descripcion = descripcion) }
}