package com.example.cogtrainingdemo.ui.main.viewState

import com.example.cogtrainingdemo.data.model.CharactersItem

sealed class MainState {
    object Idle: MainState()
    object Loading: MainState()
    data class Characters(val data: List<CharactersItem>): MainState()
    data class Error(val errorMessage: String): MainState()
}
