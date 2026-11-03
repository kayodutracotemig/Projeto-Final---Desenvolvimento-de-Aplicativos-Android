package com.machado.multiverso.userinterface

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.machado.multiverso.viewmodel.AuthViewModel

@Composable
fun TelaCadastro(
    aoVoltar: () -> Unit,
    aoCadastrar: () -> Unit,
    vm: AuthViewModel = viewModel()
) {
    val estado by vm.estado.collectAsState()
    var nome by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var senha by rememberSaveable { mutableStateOf("") }
    var confirmar by rememberSaveable { mutableStateOf("") }

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
                .padding(top = 24.dp, bottom = 72.dp)
        ) {
            BotaoCirculo(
                icone = Icons.AutoMirrored.Filled.ArrowBack,
                descricao = "Voltar",
                aoClicar = aoVoltar
            )
            Spacer(Modifier.height(24.dp))
            Text("Criar conta", color = Cores.Texto, fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(8.dp))
            Text(
                "Seus favoritos ficam guardados na sua conta, neste aparelho.",
                color = Cores.TextoSuave,
                fontSize = 16.sp,
                lineHeight = 22.sp
            )
            Spacer(Modifier.height(24.dp))
            CampoTexto("Nome", nome, { nome = it }, "Como quer ser chamado")
            Spacer(Modifier.height(14.dp))
            CampoTexto("E-mail", email, { email = it }, "voce@email.com", teclado = KeyboardType.Email)
            Spacer(Modifier.height(14.dp))
            CampoTexto("Senha", senha, { senha = it }, "Mínimo de 6 caracteres", senha = true)
            Spacer(Modifier.height(14.dp))
            CampoTexto("Confirmar senha", confirmar, { confirmar = it }, "Repita a senha", senha = true)
            Spacer(Modifier.height(12.dp))
            Text(
                text = estado.erro ?: "O e-mail precisa ter formato válido e não pode já estar cadastrado.",
                color = if (estado.erro != null) Cores.Vermelho else Cores.TextoSuave,
                fontSize = 12.sp
            )
            Spacer(Modifier.height(16.dp))
            BotaoPrincipal(
                texto = "Criar conta",
                aoClicar = { vm.cadastrar(nome, email, senha, confirmar, aoCadastrar) },
                carregando = estado.carregando
            )
        }

        Row(
            Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        ) {
            Text("Já tem conta? ", color = Cores.TextoSuave, fontSize = 14.sp)
            Text(
                "Entrar",
                color = Cores.Verde,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable(onClick = aoVoltar)
            )
        }
    }
}
