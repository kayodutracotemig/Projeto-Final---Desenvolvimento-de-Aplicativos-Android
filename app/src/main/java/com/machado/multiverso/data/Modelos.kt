package com.machado.multiverso.data

import kotlinx.serialization.Serializable

// ---------- Respostas da API (Rick and Morty) ----------

@Serializable
data class LocalDto(val name: String = "")

@Serializable
data class PersonagemDto(
    val id: Int,
    val name: String,
    val status: String = "unknown",
    val species: String = "unknown",
    val gender: String = "unknown",
    val origin: LocalDto = LocalDto(),
    val location: LocalDto = LocalDto(),
    val episode: List<String> = emptyList()
)

@Serializable
data class InfoDto(
    val pages: Int = 1,
    val next: String? = null
)

@Serializable
data class RespostaPersonagens(
    val info: InfoDto = InfoDto(),
    val results: List<PersonagemDto> = emptyList()
)

// ---------- Modelos usados pelo app ----------

data class Personagem(
    val id: Int,
    val nome: String,
    val status: String,
    val especie: String,
    val genero: String,
    val origem: String,
    val localizacao: String,
    val episodios: Int
)

data class PaginaPersonagens(
    val itens: List<Personagem>,
    val temMais: Boolean
)

sealed class Resultado<out T> {
    data class Sucesso<T>(val valor: T) : Resultado<T>()
    data class Erro(val mensagem: String) : Resultado<Nothing>()
}

fun PersonagemDto.toPersonagem() = Personagem(
    id = id,
    nome = name,
    status = status,
    especie = species,
    genero = gender,
    origem = origin.name,
    localizacao = location.name,
    episodios = episode.size
)
