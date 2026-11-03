package com.machado.multiverso.userinterface

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object Cores {
    val Fundo = Color(0xFF0B1210)
    val Superficie = Color(0xFF16211E)
    val SuperficieClara = Color(0xFF1E2C28)
    val Borda = Color(0xFF26332F)
    val Verde = Color(0xFF5DE58E)
    val VerdeEscuro = Color(0xFF1E3A2E)
    val Texto = Color(0xFFEAF2EE)
    val TextoSuave = Color(0xFF9DADA7)
    val Vermelho = Color(0xFFFF6B5E)
    val Cinza = Color(0xFF9AA5A1)

    // Cores dos quadradinhos de iniciais
    val Avatares = listOf(
        Color(0xFF7FD1E8), // azul
        Color(0xFFF2C46D), // amarelo
        Color(0xFFF29B8B), // salmão
        Color(0xFFC9A7F5)  // roxo
    )
}

@Composable
fun MultiversoTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Cores.Verde,
            onPrimary = Cores.Fundo,
            background = Cores.Fundo,
            onBackground = Cores.Texto,
            surface = Cores.Superficie,
            onSurface = Cores.Texto
        ),
        content = content
    )
}
