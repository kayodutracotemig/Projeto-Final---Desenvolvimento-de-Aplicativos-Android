package com.machado.multiverso.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.machado.multiverso.data.Personagem
import com.machado.multiverso.data.Repository
import com.machado.multiverso.data.Resultado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetalheUiState(
    val carregando: Boolean = true,
    val erro: String? = null,
    val personagem: Personagem? = null
)

class DetalheViewModel(
    app: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(app) {

    private val repository = Repository.get(app)

    // A rota type-safe Detalhe(id) entrega o argumento "id" no SavedStateHandle.
    private val id: Int = savedStateHandle.get<Int>("id") ?: 0

    private val _estado = MutableStateFlow(DetalheUiState())
    val estado: StateFlow<DetalheUiState> = _estado.asStateFlow()

    val favorito: StateFlow<Boolean> = repository.favoritos
        .map { lista -> lista.any { it.id == id } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    init {
        carregar()
    }

    fun carregar() {
        viewModelScope.launch {
            _estado.update { it.copy(carregando = true, erro = null) }
            when (val r = repository.buscarPersonagem(id)) {
                is Resultado.Sucesso ->
                    _estado.value = DetalheUiState(carregando = false, personagem = r.valor)
                is Resultado.Erro -> {
                    // Sem internet: se o personagem estiver nos favoritos, mostra a cópia salva.
                    val salvo = repository.favoritoSalvo(id)
                    _estado.value =
                        if (salvo != null) DetalheUiState(carregando = false, personagem = salvo)
                        else DetalheUiState(carregando = false, erro = r.mensagem)
                }
            }
        }
    }

    fun alternarFavorito() {
        val personagem = _estado.value.personagem ?: return
        viewModelScope.launch { repository.alternarFavorito(personagem) }
    }
}
