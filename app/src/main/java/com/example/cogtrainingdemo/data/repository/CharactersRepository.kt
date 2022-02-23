package com.example.cogtrainingdemo.data.repository

import com.example.cogtrainingdemo.data.api.CharactersRemoteDataSource
import com.example.cogtrainingdemo.data.model.CharactersItem
import com.example.cogtrainingdemo.util.Resource
import java.lang.Exception

class CharactersRepository(
    private val charactersRemoteDataSource: CharactersRemoteDataSource
)  {

    suspend fun getAllCharacters(): Resource<List<CharactersItem>> {
        return try {
            val response = charactersRemoteDataSource.getAllCharacters()
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }

        } catch (e: Exception) {
            Resource.Error(e.message ?: "An Error Occurred")
        }
    }
}
