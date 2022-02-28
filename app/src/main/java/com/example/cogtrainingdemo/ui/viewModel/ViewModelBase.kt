package com.example.cogtrainingdemo.ui.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.cogtrainingdemo.data.repository.CharactersRepository
import com.example.cogtrainingdemo.ui.main.intent.MainIntent
import com.example.cogtrainingdemo.ui.main.viewState.MainState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class ViewModelBase: ViewModel() {
    lateinit var repository: CharactersRepository
    val userIntent = Channel<MainIntent>(Channel.UNLIMITED)
    protected open val _viewState = MutableStateFlow<MainState>(MainState.Idle)
    val viewState: StateFlow<MainState>
        get() = _viewState

    abstract fun handleIntent(context: Context)
}