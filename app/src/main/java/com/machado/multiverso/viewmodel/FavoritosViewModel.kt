package com.machado.multiverso.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.machado.multiverso.data.Personagem
import com.machado.multiverso.data.Repository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritosViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = Repository.get(app)

    val favoritos: StateFlow<List<Personagem>> = repository.favoritos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun remover(personagem: Personagem) {
        viewModelScope.launch { repository.alternarFavorito(personagem) }
    }
}
