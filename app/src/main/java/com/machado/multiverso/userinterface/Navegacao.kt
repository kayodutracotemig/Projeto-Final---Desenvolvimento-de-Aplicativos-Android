package com.machado.multiverso.userinterface

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.machado.multiverso.viewmodel.AuthViewModel
import kotlinx.serialization.Serializable

// ---------- Rotas type-safe ----------

@Serializable
object Login

@Serializable
object Cadastro

@Serializable
object Personagens

@Serializable
object Favoritos

@Serializable
object Perfil

@Serializable
data class Detalhe(val id: Int)

private class Aba(val rota: Any, val titulo: String, val icone: ImageVector)

private val abas = listOf(
    Aba(Personagens, "Personagens", Icons.Outlined.People),
    Aba(Favoritos, "Favoritos", Icons.Outlined.FavoriteBorder),
    Aba(Perfil, "Perfil", Icons.Outlined.Person)
)

@Composable
fun AppNav(auth: AuthViewModel = viewModel()) {
    val nav = rememberNavController()

    // Quem já entrou (sessão salva) abre direto no app; os demais começam no login.
    val inicio: Any = remember { if (auth.logadoAgora()) Personagens else Login }

    val entrada by nav.currentBackStackEntryAsState()
    val destino = entrada?.destination
    val mostrarBarra = abas.any { destino?.hasRoute(it.rota::class) == true }

    Scaffold(
        containerColor = Cores.Fundo,
        bottomBar = {
            if (mostrarBarra) {
                Column {
                    HorizontalDivider(color = Cores.Borda)
                    NavigationBar(containerColor = Cores.Fundo, tonalElevation = 0.dp) {
                        abas.forEach { aba ->
                            val selecionada = destino?.hasRoute(aba.rota::class) == true
                            NavigationBarItem(
                                selected = selecionada,
                                onClick = {
                                    nav.navigate(aba.rota) {
                                        popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = { Icon(aba.icone, contentDescription = aba.titulo) },
                                label = {
                                    Text(
                                        aba.titulo,
                                        fontSize = 12.sp,
                                        fontWeight = if (selecionada) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = Cores.Verde,
                                    selectedTextColor = Cores.Verde,
                                    indicatorColor = Cores.VerdeEscuro,
                                    unselectedIconColor = Cores.TextoSuave,
                                    unselectedTextColor = Cores.TextoSuave
                                )
                            )
                        }
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = nav,
            startDestination = inicio,
            modifier = Modifier.padding(padding)
        ) {
            composable<Login> {
                TelaLogin(
                    aoEntrar = {
                        nav.navigate(Personagens) { popUpTo(nav.graph.id) { inclusive = true } }
                    },
                    aoCriarConta = { nav.navigate(Cadastro) }
                )
            }
            composable<Cadastro> {
                TelaCadastro(
                    aoVoltar = { nav.popBackStack() },
                    aoCadastrar = {
                        nav.navigate(Personagens) { popUpTo(nav.graph.id) { inclusive = true } }
                    }
                )
            }
            composable<Personagens> {
                TelaPersonagens(aoAbrirDetalhe = { id -> nav.navigate(Detalhe(id)) })
            }
            composable<Favoritos> {
                TelaFavoritos(aoAbrirDetalhe = { id -> nav.navigate(Detalhe(id)) })
            }
            composable<Perfil> {
                TelaPerfil(
                    aoSair = {
                        nav.navigate(Login) { popUpTo(nav.graph.id) { inclusive = true } }
                    }
                )
            }
            composable<Detalhe> {
                TelaDetalhe(aoVoltar = { nav.popBackStack() })
            }
        }
    }
}
