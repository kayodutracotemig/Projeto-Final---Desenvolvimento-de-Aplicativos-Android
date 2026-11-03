package com.machado.multiverso.userinterface

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.machado.multiverso.data.Personagem
import com.machado.multiverso.viewmodel.DetalheViewModel

@Composable
fun TelaDetalhe(
    aoVoltar: () -> Unit,
    vm: DetalheViewModel = viewModel()
) {
    val estado by vm.estado.collectAsState()
    val favorito by vm.favorito.collectAsState()
    val personagem = estado.personagem

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        Spacer(Modifier.height(24.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            BotaoCirculo(Icons.AutoMirrored.Filled.ArrowBack, "Voltar", aoVoltar)
            Spacer(Modifier.width(14.dp))
            Text("Detalhe", color = Cores.Texto, fontSize = 17.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(16.dp))

        when {
            estado.carregando -> Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Cores.Verde)
            }
            personagem == null -> Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                MensagemCentral(
                    texto = estado.erro ?: "Personagem não encontrado.",
                    textoBotao = "Tentar novamente",
                    aoClicarBotao = vm::carregar
                )
            }
            else -> {
                Column(
                    Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    Conteudo(personagem)
                }
                Spacer(Modifier.height(12.dp))
                BotaoPrincipal(
                    texto = if (favorito) "Remover dos favoritos" else "Salvar nos favoritos",
                    aoClicar = vm::alternarFavorito,
                    icone = if (favorito) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder
                )
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun Conteudo(p: Personagem) {
    Avatar(
        nome = p.nome,
        id = p.id,
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp),
        tamanhoFonte = 96.sp,
        raio = 24.dp
    )
    Spacer(Modifier.height(20.dp))
    Text(p.nome, color = Cores.Texto, fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)
    Spacer(Modifier.height(12.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Etiqueta(traduzStatus(p.status), corStatus(p.status))
        Etiqueta(traduzEspecie(p.especie), null)
    }
    Spacer(Modifier.height(16.dp))
    HorizontalDivider(color = Cores.Borda)
    Informacao("Gênero", traduzGenero(p.genero))
    Informacao("Origem", traduzLugar(p.origem))
    Informacao("Última localização", traduzLugar(p.localizacao))
    Informacao("Episódios", p.episodios.toString())
}

@Composable
private fun Etiqueta(texto: String, corPonto: androidx.compose.ui.graphics.Color?) {
    val forma = RoundedCornerShape(50)
    Row(
        modifier = Modifier
            .border(1.dp, Cores.Borda, forma)
            .padding(horizontal = 12.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (corPonto != null) {
            PontoStatus(corPonto)
            Spacer(Modifier.width(6.dp))
        }
        Text(texto, color = Cores.Texto, fontSize = 13.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun Informacao(rotulo: String, valor: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(rotulo, color = Cores.TextoSuave, fontSize = 14.sp)
        Spacer(Modifier.width(16.dp))
        Text(
            valor,
            color = Cores.Texto,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f, fill = false),
            textAlign = androidx.compose.ui.text.style.TextAlign.End
        )
    }
    HorizontalDivider(color = Cores.Borda)
}
