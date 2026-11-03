package com.machado.multiverso.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.machado.multiverso.data.Repository
import com.machado.multiverso.data.Resultado
import com.machado.multiverso.data.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class AuthUiState(
    val carregando: Boolean = false,
    val erro: String? = null
)

class AuthViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = Repository.get(app)

    private val _estado = MutableStateFlow(AuthUiState())
    val estado: StateFlow<AuthUiState> = _estado.asStateFlow()

    val usuario: StateFlow<Usuario?> = repository.usuarioAtual
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun logadoAgora(): Boolean = repository.usuarioId.value != null

    fun entrar(email: String, senha: String, aoEntrar: () -> Unit) =
        executar(aoEntrar) { repository.entrar(email, senha) }

    fun cadastrar(
        nome: String,
        email: String,
        senha: String,
        confirmar: String,
        aoCadastrar: () -> Unit
    ) = executar(aoCadastrar) { repository.cadastrar(nome, email, senha, confirmar) }

    fun sair(aoSair: () -> Unit) {
        repository.sair()
        aoSair()
    }

    private fun executar(aoSucesso: () -> Unit, acao: suspend () -> Resultado<Unit>) {
        if (_estado.value.carregando) return
        viewModelScope.launch {
            _estado.value = AuthUiState(carregando = true)
            when (val resultado = acao()) {
                is Resultado.Sucesso -> {
                    _estado.value = AuthUiState()
                    aoSucesso()
                }
                is Resultado.Erro -> _estado.value = AuthUiState(erro = resultado.mensagem)
            }
        }
    }
}
