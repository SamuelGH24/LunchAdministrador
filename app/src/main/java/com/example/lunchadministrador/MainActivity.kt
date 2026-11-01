package com.example.lunchadministrador

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.lunchadministrador.ui.navigation.NavegacionApp
import com.example.lunchadministrador.ui.theme.Fondo
import com.example.lunchadministrador.ui.theme.LunchAdministradorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Barras del sistema transparentes con íconos oscuros (la app es de fondo blanco).
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        val auth = (application as LunchApp).container.auth
        setContent {
            LunchAdministradorTheme {
                Surface(Modifier.fillMaxSize(), color = Fondo) {
                    Box(Modifier.fillMaxSize().systemBarsPadding()) { NavegacionApp(auth) }
                }
            }
        }
    }
}
