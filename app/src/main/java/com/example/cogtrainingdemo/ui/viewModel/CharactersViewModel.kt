package com.example.cogtrainingdemo.ui.viewModel

import android.content.Context

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cogtrainingdemo.data.local.LocalRepo
import com.example.cogtrainingdemo.data.repository.CharactersRepository
import com.example.cogtrainingdemo.ui.main.intent.MainIntent
import com.example.cogtrainingdemo.ui.main.viewState.MainState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class CharactersViewModel : ViewModelBase() {
    private val _charactersState = MutableStateFlow<MainState>(MainState.Idle)
    val charactersState: StateFlow<MainState> = _charactersState

    fun init(context: Context, repository: CharactersRepository) {
        this.repository = repository
        handleIntent(context)
    }

    private fun fetchCharacters(context: Context) {

        viewModelScope.launch {
            if (LocalRepo.hasCharacters(context)) {
                _charactersState.value = MainState.Characters(LocalRepo.getCharacters(context))
            } else {
                _charactersState.value = MainState.Loading
                val responseData = repository.getAllCharacters()

                LocalRepo.setCharacters(
                    context,
                    requireNotNull(responseData.data)
                )

                _charactersState.value = try {
                    MainState.Characters(requireNotNull(responseData.data))
                } catch (e: Exception) {
                    MainState.Error(e.localizedMessage)
                }

            }
        }
    }

    override fun handleIntent(context: Context) {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is MainIntent.FetchUser -> fetchCharacters(context)
                }

            }
        }
    }
}
