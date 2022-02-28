package com.example.cogtrainingdemo.ui.viewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cogtrainingdemo.data.local.database.EpisodesDatabase
import com.example.cogtrainingdemo.data.repository.CharactersRepository
import com.example.cogtrainingdemo.ui.main.intent.MainIntent
import com.example.cogtrainingdemo.ui.main.viewState.MainState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EpisodesViewModel : ViewModelBase() {
    private lateinit var repositoryDB: EpisodesDatabase
    val selected = MutableLiveData<String>()
    fun init(context: Context, repository: CharactersRepository) {
        this.repositoryDB = EpisodesDatabase.getInstance(context)
        this.repository = repository
        handleIntent(context)
    }

    fun selectedItem(item: String) {
        selected.value = item
    }

    override fun handleIntent(context: Context) {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is MainIntent.FetchEpisodes -> fetchEposides(context)
                    is MainIntent.GetEpisodesWithCharacter -> getEpisodesWithCharacter(it.characterName)
                }

            }
        }
    }

    private fun getEpisodesWithCharacter(characterName: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = repositoryDB.episodesDao().getEpisodesList()
            if (list.isNullOrEmpty()) {
                _viewState.value = MainState.Loading
                val responseData = repository.getAllEpisodes()
                if (!responseData.data.isNullOrEmpty()) {
                    responseData.data.forEach {
                        repositoryDB.episodesDao()
                            .insertEpisodesItem(it)
                    }

                    characterName?.let {
                        // query with character name
                        repositoryDB.episodesDao()
                            .findEpisodesCharacterName(characterName).let { episodeItemList ->
                                _viewState.value = MainState.Episodes(episodeItemList)
                            }
                    }
                } else {
                    _viewState.value = MainState.Episodes(listOf())
                }

            } else {
                characterName?.let {
                    repositoryDB.episodesDao()
                        .findEpisodesCharacterName(characterName).let { episodeItemList ->
                            _viewState.value = MainState.Episodes(episodeItemList)
                        }
                } ?: withContext(Dispatchers.Main) {
                    _viewState.value =
                        MainState.Episodes(listOf())
                }
            }
        }
    }

    private fun fetchEposides(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            if (repositoryDB.episodesDao().getEpisodesList().isNullOrEmpty()) {
                _viewState.value = MainState.Loading
                val responseData = repository.getAllEpisodes()
                if (!responseData.data.isNullOrEmpty())
                    responseData.data.forEach {
                        repositoryDB.episodesDao()
                            .insertEpisodesItem(it)
                    }

                _viewState.value = try {
                    MainState.Episodes(requireNotNull(responseData.data))
                } catch (e: Exception) {
                    MainState.Error(e.localizedMessage)
                }
            } else {
                _viewState.value =
                    MainState.Episodes(
                        requireNotNull(
                            repositoryDB.episodesDao().getEpisodesList()
                        )
                    )
            }
        }
    }
}




























