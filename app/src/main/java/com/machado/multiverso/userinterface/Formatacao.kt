package com.machado.multiverso.userinterface

import androidx.compose.ui.graphics.Color

// A API devolve tudo em inglês; aqui traduzimos para exibir como nas telas.

fun traduzStatus(status: String): String = when (status.lowercase()) {
    "alive" -> "Vivo"
    "dead" -> "Morto"
    else -> "Desconhecido"
}

fun corStatus(status: String): Color = when (status.lowercase()) {
    "alive" -> Cores.Verde
    "dead" -> Cores.Vermelho
    else -> Cores.Cinza
}

fun traduzEspecie(especie: String): String = when (especie.lowercase()) {
    "human" -> "Humano"
    "alien" -> "Alienígena"
    "humanoid" -> "Humanoide"
    "robot" -> "Robô"
    "animal" -> "Animal"
    "mythological creature" -> "Criatura mitológica"
    "disease" -> "Doença"
    "parasite" -> "Parasita"
    "unknown" -> "Desconhecida"
    else -> especie
}

fun traduzGenero(genero: String): String = when (genero.lowercase()) {
    "male" -> "Masculino"
    "female" -> "Feminino"
    "genderless" -> "Sem gênero"
    else -> "Desconhecido"
}

fun traduzLugar(lugar: String): String =
    if (lugar.lowercase() == "unknown" || lugar.isBlank()) "Desconhecido" else lugar

fun iniciais(nome: String): String {
    val palavras = nome.trim().split(" ").filter { it.isNotEmpty() }
    return when {
        palavras.size >= 2 -> "${palavras[0].first()}${palavras[1].first()}"
        palavras.size == 1 -> palavras[0].take(2)
        else -> "?"
    }.uppercase()
}
