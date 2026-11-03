package com.machado.multiverso.data

import android.content.Context
import android.util.Patterns
import com.machado.multiverso.network.RetrofitInstance
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

/**
 * Único ponto de acesso a rede (Retrofit), banco (Room) e sessão.
 * Nenhuma tela e nenhum ViewModel falam com o Retrofit ou com o DAO diretamente.
 */
class Repository private constructor(context: Context) {

    private val banco = AppDatabase.get(context)
    private val api = RetrofitInstance.api
    private val prefs = context.applicationContext
        .getSharedPreferences("sessao", Context.MODE_PRIVATE)

    // ---------- Sessão ----------

    private val _usuarioId = MutableStateFlow(
        prefs.getInt(CHAVE_USUARIO, -1).takeIf { it != -1 }
    )
    val usuarioId: StateFlow<Int?> = _usuarioId.asStateFlow()

    private fun salvarSessao(id: Int) {
        prefs.edit().putInt(CHAVE_USUARIO, id).apply()
        _usuarioId.value = id
    }

    fun sair() {
        prefs.edit().remove(CHAVE_USUARIO).apply()
        _usuarioId.value = null
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val usuarioAtual: Flow<Usuario?> = _usuarioId.flatMapLatest { id ->
        if (id == null) flowOf(null) else banco.usuarioDao().observarPorId(id)
    }

    // ---------- Cadastro e login ----------

    suspend fun cadastrar(
        nome: String,
        email: String,
        senha: String,
        confirmar: String
    ): Resultado<Unit> {
        val nomeLimpo = nome.trim()
        val emailLimpo = email.trim().lowercase()

        if (nomeLimpo.isEmpty()) return Resultado.Erro("Informe como quer ser chamado.")
        if (!Patterns.EMAIL_ADDRESS.matcher(emailLimpo).matches())
            return Resultado.Erro("Digite um e-mail com formato válido.")
        if (senha.length < 6) return Resultado.Erro("A senha precisa ter 6 ou mais caracteres.")
        if (senha != confirmar) return Resultado.Erro("As senhas não são iguais.")
        if (banco.usuarioDao().buscarPorEmail(emailLimpo) != null)
            return Resultado.Erro("Este e-mail já está cadastrado.")

        val id = banco.usuarioDao()
            .inserir(Usuario(nome = nomeLimpo, email = emailLimpo, senhaHash = senha.sha256()))
            .toInt()
        salvarSessao(id)
        return Resultado.Sucesso(Unit)
    }

    suspend fun entrar(email: String, senha: String): Resultado<Unit> {
        val usuario = banco.usuarioDao().buscarPorEmail(email.trim().lowercase())
        // Mesma mensagem para e-mail ou senha errados (não revela qual campo errou).
        if (usuario == null || usuario.senhaHash != senha.sha256())
            return Resultado.Erro("E-mail ou senha incorretos.")
        salvarSessao(usuario.id)
        return Resultado.Sucesso(Unit)
    }

    // ---------- API ----------

    suspend fun buscarPersonagens(
        nome: String,
        status: String?,
        pagina: Int
    ): Resultado<PaginaPersonagens> = try {
        val resposta = api.personagens(
            pagina = pagina,
            nome = nome.trim().ifEmpty { null },
            status = status
        )
        Resultado.Sucesso(
            PaginaPersonagens(
                itens = resposta.results.map { it.toPersonagem() },
                temMais = resposta.info.next != null
            )
        )
    } catch (e: CancellationException) {
        throw e
    } catch (e: HttpException) {
        // A API responde 404 quando a busca não encontra ninguém.
        if (e.code() == 404) Resultado.Sucesso(PaginaPersonagens(emptyList(), false))
        else Resultado.Erro("O servidor respondeu com erro (${e.code()}). Tente novamente.")
    } catch (e: IOException) {
        Resultado.Erro("Sem conexão com a internet. Verifique e tente novamente.")
    } catch (e: Exception) {
        Resultado.Erro("Não foi possível carregar os dados.")
    }

    suspend fun buscarPersonagem(id: Int): Resultado<Personagem> = try {
        Resultado.Sucesso(api.personagem(id).toPersonagem())
    } catch (e: CancellationException) {
        throw e
    } catch (e: IOException) {
        Resultado.Erro("Sem conexão com a internet. Verifique e tente novamente.")
    } catch (e: Exception) {
        Resultado.Erro("Não foi possível carregar este personagem.")
    }

    // ---------- Favoritos (Room, por usuário) ----------

    @OptIn(ExperimentalCoroutinesApi::class)
    val favoritos: Flow<List<Personagem>> = _usuarioId.flatMapLatest { id ->
        if (id == null) flowOf(emptyList())
        else banco.favoritoDao().observar(id).map { lista -> lista.map { it.toPersonagem() } }
    }

    suspend fun favoritoSalvo(personagemId: Int): Personagem? {
        val id = _usuarioId.value ?: return null
        return banco.favoritoDao().buscar(id, personagemId)?.toPersonagem()
    }

    suspend fun alternarFavorito(personagem: Personagem) {
        val id = _usuarioId.value ?: return
        val dao = banco.favoritoDao()
        if (dao.buscar(id, personagem.id) != null) dao.remover(id, personagem.id)
        else dao.inserir(personagem.toFavorito(id))
    }

    companion object {
        private const val CHAVE_USUARIO = "usuario_id"

        @Volatile
        private var instancia: Repository? = null

        fun get(context: Context): Repository =
            instancia ?: synchronized(this) {
                instancia ?: Repository(context.applicationContext).also { instancia = it }
            }
    }
}
