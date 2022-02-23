package com.example.cogtrainingdemo.data.api

import com.example.cogtrainingdemo.data.model.CharactersItem
import retrofit2.Response
import retrofit2.http.GET

interface CharactersRemoteDataSource {
    @GET("characters")
    suspend fun getAllCharacters(): Response<List<CharactersItem>>
}
