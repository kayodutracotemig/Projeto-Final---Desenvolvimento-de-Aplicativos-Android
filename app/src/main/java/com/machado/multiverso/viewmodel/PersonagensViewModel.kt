package com.machado.multiverso.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.machado.multiverso.data.Personagem
import com.machado.multiverso.data.Repository
import com.machado.multiverso.data.Resultado
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class FiltroStatus(val rotulo: String, val parametro: String?) {
    TODOS("Todos", null),
    VIVO("Vivo", "alive"),
    MORTO("Morto", "dead"),
    DESCONHECIDO("Desconhecido", "unknown")
}

data class PersonagensUiState(
    val carregando: Boolean = true,
    val carregandoMais: Boolean = false,
    val erro: String? = null,
    val personagens: List<Personagem> = emptyList(),
    val busca: String = "",
    val filtro: FiltroStatus = FiltroStatus.TODOS,
    val temMais: Boolean = false
)

class PersonagensViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = Repository.get(app)

    private val _estado = MutableStateFlow(PersonagensUiState())
    val estado: StateFlow<PersonagensUiState> = _estado.asStateFlow()

    val favoritosIds: StateFlow<Set<Int>> = repository.favoritos
        .map { lista -> lista.map { it.id }.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    private var pagina = 1
    private var job: Job? = null

    init {
        carregar()
    }

    fun alterarBusca(texto: String) {
        _estado.update { it.copy(busca = texto) }
        carregar(atrasoMs = 400) // espera o usuário terminar de digitar
    }

    fun alterarFiltro(filtro: FiltroStatus) {
        _estado.update { it.copy(filtro = filtro) }
        carregar()
    }

    fun tentarNovamente() = carregar()

    private fun carregar(atrasoMs: Long = 0) {
        job?.cancel()
        job = viewModelScope.launch {
            if (atrasoMs > 0) delay(atrasoMs)
            pagina = 1
            _estado.update { it.copy(carregando = true, carregandoMais = false, erro = null) }
            val atual = _estado.value
            when (val r = repository.buscarPersonagens(atual.busca, atual.filtro.parametro, 1)) {
                is Resultado.Sucesso -> _estado.update {
                    it.copy(carregando = false, personagens = r.valor.itens, temMais = r.valor.temMais)
                }
                is Resultado.Erro -> _estado.update {
                    it.copy(carregando = false, erro = r.mensagem, personagens = emptyList(), temMais = false)
                }
            }
        }
    }

    fun carregarMais() {
        val atual = _estado.value
        if (!atual.temMais || atual.carregando || atual.carregandoMais) return
        job = viewModelScope.launch {
            _estado.update { it.copy(carregandoMais = true) }
            when (val r = repository.buscarPersonagens(atual.busca, atual.filtro.parametro, pagina + 1)) {
                is Resultado.Sucesso -> {
                    pagina += 1
                    _estado.update {
                        it.copy(
                            carregandoMais = false,
                            personagens = it.personagens + r.valor.itens,
                            temMais = r.valor.temMais
                        )
                    }
                }
                is Resultado.Erro -> _estado.update { it.copy(carregandoMais = false, temMais = false) }
            }
        }
    }

    fun alternarFavorito(personagem: Personagem) {
        viewModelScope.launch { repository.alternarFavorito(personagem) }
    }
}
