package com.example.cogtrainingdemo.ui.main.intent

sealed class MainIntent {
    object FetchUser : MainIntent()
    object FetchEpisodes : MainIntent()
    data class GetEpisodesWithCharacter(val characterName: String? = null) : MainIntent()
}
