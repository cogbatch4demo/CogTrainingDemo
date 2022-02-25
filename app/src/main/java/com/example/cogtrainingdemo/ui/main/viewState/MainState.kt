package com.example.cogtrainingdemo.ui.main.viewState

import com.example.cogtrainingdemo.data.model.CharactersItem
import com.example.cogtrainingdemo.data.model.EpisodesItem

sealed class MainState {
    object Idle : MainState()
    object Loading : MainState()
    data class Characters(val data: List<CharactersItem>) : MainState()
    data class Error(val errorMessage: String) : MainState()
    data class Episodes(val data: List<EpisodesItem>) : MainState()
}
