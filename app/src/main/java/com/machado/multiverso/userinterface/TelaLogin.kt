package com.machado.multiverso.userinterface

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.machado.multiverso.viewmodel.AuthViewModel

@Composable
fun Logo() {
    Box(
        modifier = Modifier
            .size(52.dp)
            .border(3.dp, Cores.Verde, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Cores.VerdeEscuro),
            contentAlignment = Alignment.Center
        ) {
            Box(
                Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(Cores.Verde)
            )
        }
    }
}

@Composable
fun TelaLogin(
    aoEntrar: () -> Unit,
    aoCriarConta: () -> Unit,
    vm: AuthViewModel = viewModel()
) {
    val estado by vm.estado.collectAsState()
    var email by rememberSaveable { mutableStateOf("") }
    var senha by rememberSaveable { mutableStateOf("") }

    Box(
        Modifier
            .fillMaxSize()
            .imePadding()
            .padding(horizontal = 24.dp)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 48.dp, bottom = 72.dp)
        ) {
            Logo()
            Spacer(Modifier.height(16.dp))
            Text("Multiverso", color = Cores.Texto, fontSize = 38.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(12.dp))
            Text(
                "Explore personagens de outras dimensões e guarde os seus favoritos.",
                color = Cores.TextoSuave,
                fontSize = 16.sp,
                lineHeight = 22.sp
            )
            Spacer(Modifier.height(32.dp))
            CampoTexto("E-mail", email, { email = it }, "voce@email.com", teclado = KeyboardType.Email)
            Spacer(Modifier.height(16.dp))
            CampoTexto("Senha", senha, { senha = it }, "Mínimo de 6 caracteres", senha = true)
            estado.erro?.let {
                Spacer(Modifier.height(12.dp))
                Text(it, color = Cores.Vermelho, fontSize = 13.sp)
            }
            Spacer(Modifier.height(20.dp))
            BotaoPrincipal(
                texto = "Entrar",
                aoClicar = { vm.entrar(email, senha, aoEntrar) },
                carregando = estado.carregando
            )
        }

        Row(
            Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        ) {
            Text("Não tem conta? ", color = Cores.TextoSuave, fontSize = 14.sp)
            Text(
                "Criar conta",
                color = Cores.Verde,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable(onClick = aoCriarConta)
            )
        }
    }
}
