package com.machado.multiverso.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "usuarios",
    indices = [Index(value = ["email"], unique = true)]
)
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String,
    val email: String,
    val senhaHash: String // nunca a senha em texto puro
)

// Cada favorito pertence a um usuário (usuarioId), por isso as contas não se misturam.
@Entity(
    tableName = "favoritos",
    primaryKeys = ["usuarioId", "personagemId"]
)
data class Favorito(
    val usuarioId: Int,
    val personagemId: Int,
    val nome: String,
    val status: String,
    val especie: String,
    val genero: String,
    val origem: String,
    val localizacao: String,
    val episodios: Int
)

fun Favorito.toPersonagem() = Personagem(
    id = personagemId,
    nome = nome,
    status = status,
    especie = especie,
    genero = genero,
    origem = origem,
    localizacao = localizacao,
    episodios = episodios
)

fun Personagem.toFavorito(usuarioId: Int) = Favorito(
    usuarioId = usuarioId,
    personagemId = id,
    nome = nome,
    status = status,
    especie = especie,
    genero = genero,
    origem = origem,
    localizacao = localizacao,
    episodios = episodios
)
