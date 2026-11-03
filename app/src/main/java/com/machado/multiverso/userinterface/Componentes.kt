package com.machado.multiverso.userinterface

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.machado.multiverso.data.Personagem

@Composable
fun camposColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = Cores.Superficie,
    unfocusedContainerColor = Cores.Superficie,
    focusedBorderColor = Cores.Verde,
    unfocusedBorderColor = Cores.Borda,
    cursorColor = Cores.Verde,
    focusedTextColor = Cores.Texto,
    unfocusedTextColor = Cores.Texto
)

@Composable
fun CampoTexto(
    rotulo: String,
    valor: String,
    aoMudar: (String) -> Unit,
    dica: String,
    senha: Boolean = false,
    teclado: KeyboardType = KeyboardType.Text
) {
    Column(Modifier.fillMaxWidth()) {
        Text(rotulo, color = Cores.Texto, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = valor,
            onValueChange = aoMudar,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(dica, color = Cores.TextoSuave) },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = camposColors(),
            visualTransformation = if (senha) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = if (senha) KeyboardType.Password else teclado)
        )
    }
}

@Composable
fun BotaoPrincipal(
    texto: String,
    aoClicar: () -> Unit,
    modifier: Modifier = Modifier,
    carregando: Boolean = false,
    icone: ImageVector? = null
) {
    Button(
        onClick = aoClicar,
        enabled = !carregando,
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Cores.Verde,
            contentColor = Cores.Fundo,
            disabledContainerColor = Cores.Verde.copy(alpha = 0.6f),
            disabledContentColor = Cores.Fundo
        )
    ) {
        if (carregando) {
            CircularProgressIndicator(Modifier.size(20.dp), color = Cores.Fundo, strokeWidth = 2.dp)
        } else {
            if (icone != null) {
                Icon(icone, contentDescription = null)
                Spacer(Modifier.width(8.dp))
            }
            Text(texto, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }
    }
}

@Composable
fun BotaoCirculo(
    icone: ImageVector,
    descricao: String,
    aoClicar: () -> Unit,
    tamanho: Dp = 44.dp,
    fundo: Color = Cores.SuperficieClara,
    corIcone: Color = Cores.Texto
) {
    Box(
        modifier = Modifier
            .size(tamanho)
            .clip(CircleShape)
            .background(fundo)
            .clickable(onClick = aoClicar),
        contentAlignment = Alignment.Center
    ) {
        Icon(icone, contentDescription = descricao, tint = corIcone)
    }
}

@Composable
fun Avatar(
    nome: String,
    id: Int,
    modifier: Modifier,
    tamanhoFonte: TextUnit,
    raio: Dp
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(raio))
            .background(Cores.Avatares[(id - 1).mod(Cores.Avatares.size)]),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = iniciais(nome),
            color = Cores.Fundo,
            fontSize = tamanhoFonte,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun PontoStatus(cor: Color) {
    Box(
        Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(cor)
    )
}

@Composable
fun LinhaPersonagem(
    personagem: Personagem,
    favorito: Boolean,
    aoClicar: () -> Unit,
    aoFavoritar: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Cores.Superficie)
            .border(1.dp, Cores.Borda, RoundedCornerShape(16.dp))
            .clickable(onClick = aoClicar)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Avatar(personagem.nome, personagem.id, Modifier.size(56.dp), 20.sp, 14.dp)
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(
                personagem.nome,
                color = Cores.Texto,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                PontoStatus(corStatus(personagem.status))
                Spacer(Modifier.width(6.dp))
                Text(
                    "${traduzStatus(personagem.status)} · ${traduzEspecie(personagem.especie)}",
                    color = Cores.TextoSuave,
                    fontSize = 13.sp
                )
            }
        }
        Spacer(Modifier.width(8.dp))
        BotaoCirculo(
            icone = if (favorito) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            descricao = if (favorito) "Remover dos favoritos" else "Salvar nos favoritos",
            aoClicar = aoFavoritar,
            corIcone = if (favorito) Cores.Verde else Cores.Texto
        )
    }
}

@Composable
fun MensagemCentral(
    texto: String,
    textoBotao: String? = null,
    aoClicarBotao: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(texto, color = Cores.TextoSuave, fontSize = 14.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        if (textoBotao != null && aoClicarBotao != null) {
            Button(
                onClick = aoClicarBotao,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Cores.Verde, contentColor = Cores.Fundo)
            ) {
                Text(textoBotao, fontWeight = FontWeight.Bold)
            }
        }
    }
}
