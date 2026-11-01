package com.example.lunchadministrador.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lunchadministrador.ui.theme.Carbon
import com.example.lunchadministrador.ui.theme.Naranja
import com.example.lunchadministrador.ui.theme.Superficie

@Composable
fun Txt(
    texto: String,
    size: Int = 14,
    negrita: Boolean = false,
    color: Color = Carbon,
    align: TextAlign = TextAlign.Center,
    modifier: Modifier = Modifier
) {
    Text(
        text = texto, color = color, fontSize = size.sp, textAlign = align, modifier = modifier,
        fontWeight = if (negrita) FontWeight.Bold else FontWeight.Normal
    )
}

/**
 * Recuadro de la app. Si es informativo tiene fondo crema y borde naranja;
 * si se puede tocar (onClick) se tiñe con el color de su borde para que parezca botón.
 */
@Composable
fun Caja(
    modifier: Modifier = Modifier,
    borde: Color = Naranja,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val forma = RoundedCornerShape(10.dp)
    val fondo = if (onClick != null) borde.copy(alpha = 0.15f) else Superficie
    var m = modifier.clip(forma).background(fondo).border(BorderStroke(1.5.dp, borde), forma)
    if (onClick != null) m = m.clickable(onClick = onClick)
    Box(m.padding(horizontal = 12.dp, vertical = 10.dp), contentAlignment = Alignment.Center, content = content)
}

@Composable
fun CirculoMesa(texto: String, borde: Color = Naranja, onClick: (() -> Unit)? = null) {
    var m = Modifier.size(100.dp).clip(CircleShape).background(Superficie).border(BorderStroke(2.dp, borde), CircleShape)
    if (onClick != null) m = m.clickable(onClick = onClick)
    Box(m, contentAlignment = Alignment.Center) { Txt(texto, 14, true) }
}

/** Recuadro con el ícono del producto. */
@Composable
fun IconoProducto(icono: String, size: Int = 64) {
    Box(
        Modifier.size(size.dp).clip(RoundedCornerShape(6.dp)).background(Superficie)
            .border(BorderStroke(2.dp, Naranja), RoundedCornerShape(6.dp)),
        contentAlignment = Alignment.Center
    ) { Text(icono, fontSize = (size / 2.6).sp) }
}
