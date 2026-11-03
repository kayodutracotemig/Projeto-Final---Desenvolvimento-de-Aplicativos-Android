package com.machado.multiverso.userinterface

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.machado.multiverso.viewmodel.AuthViewModel

@Composable
fun TelaPerfil(
    aoSair: () -> Unit,
    vm: AuthViewModel = viewModel()
) {
    val usuario by vm.usuario.collectAsState()

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        Spacer(Modifier.height(24.dp))
        Text("Perfil", color = Cores.Texto, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Cores.Superficie)
                .border(1.dp, Cores.Borda, RoundedCornerShape(16.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Avatar(
                nome = usuario?.nome ?: "?",
                id = 1,
                modifier = Modifier.size(56.dp),
                tamanhoFonte = 20.sp,
                raio = 14.dp
            )
            Spacer(Modifier.width(14.dp))
            Column {
                Text(
                    usuario?.nome ?: "",
                    color = Cores.Texto,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(2.dp))
                Text(usuario?.email ?: "", color = Cores.TextoSuave, fontSize = 13.sp)
            }
        }

        Spacer(Modifier.height(16.dp))
        Text(
            "Seus favoritos ficam guardados na sua conta, neste aparelho.",
            color = Cores.TextoSuave,
            fontSize = 13.sp
        )
        Spacer(Modifier.height(24.dp))

        Button(
            onClick = { vm.sair(aoSair) },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Cores.Superficie,
                contentColor = Cores.Vermelho
            )
        ) {
            Text("Sair", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }
    }
}
