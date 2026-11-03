package com.machado.multiverso.userinterface

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.machado.multiverso.viewmodel.FiltroStatus
import com.machado.multiverso.viewmodel.PersonagensViewModel

@Composable
fun TelaPersonagens(
    aoAbrirDetalhe: (Int) -> Unit,
    vm: PersonagensViewModel = viewModel()
) {
    val estado by vm.estado.collectAsState()
    val favoritos by vm.favoritosIds.collectAsState()
    val listState = rememberLazyListState()

    // Carrega a próxima página quando o usuário chega perto do fim da lista.
    val pertoDoFim by remember {
        derivedStateOf {
            val ultimo = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            ultimo >= listState.layoutInfo.totalItemsCount - 4
        }
    }
    LaunchedEffect(pertoDoFim, estado.personagens.size) {
        if (pertoDoFim && estado.personagens.isNotEmpty()) vm.carregarMais()
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        Spacer(Modifier.height(24.dp))
        Text("Personagens", color = Cores.Texto, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = estado.busca,
            onValueChange = vm::alterarBusca,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Buscar por nome", color = Cores.TextoSuave) },
            leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null, tint = Cores.TextoSuave) },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = camposColors()
        )
        Spacer(Modifier.height(12.dp))

        Row(
            Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FiltroStatus.values().forEach { filtro ->
                ChipFiltro(
                    texto = filtro.rotulo,
                    selecionado = estado.filtro == filtro,
                    aoClicar = { vm.alterarFiltro(filtro) }
                )
            }
        }
        Spacer(Modifier.height(12.dp))

        Box(Modifier.weight(1f)) {
            when {
                estado.carregando && estado.personagens.isEmpty() -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Cores.Verde
                )
                estado.erro != null -> MensagemCentral(
                    texto = estado.erro!!,
                    textoBotao = "Tentar novamente",
                    aoClicarBotao = vm::tentarNovamente
                )
                estado.personagens.isEmpty() -> MensagemCentral("Nenhum personagem encontrado.")
                else -> LazyColumn(
                    state = listState,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 16.dp)
                ) {
                    items(estado.personagens, key = { it.id }) { personagem ->
                        LinhaPersonagem(
                            personagem = personagem,
                            favorito = personagem.id in favoritos,
                            aoClicar = { aoAbrirDetalhe(personagem.id) },
                            aoFavoritar = { vm.alternarFavorito(personagem) }
                        )
                    }
                    if (estado.carregandoMais) {
                        item {
                            Box(Modifier.fillMaxWidth().padding(8.dp), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(Modifier.size(24.dp), color = Cores.Verde, strokeWidth = 2.dp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChipFiltro(texto: String, selecionado: Boolean, aoClicar: () -> Unit) {
    val forma = RoundedCornerShape(50)
    Box(
        modifier = Modifier
            .clip(forma)
            .background(if (selecionado) Cores.Verde else Cores.Superficie)
            .border(1.dp, if (selecionado) Cores.Verde else Cores.Borda, forma)
            .clickable(onClick = aoClicar)
            .padding(horizontal = 18.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            texto,
            color = if (selecionado) Cores.Fundo else Cores.Texto,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
