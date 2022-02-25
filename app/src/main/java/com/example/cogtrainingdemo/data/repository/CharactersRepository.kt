package com.example.cogtrainingdemo.data.repository

import com.example.cogtrainingdemo.data.api.CharactersRemoteDataSource
import com.example.cogtrainingdemo.data.model.CharactersItem
import com.example.cogtrainingdemo.data.model.EpisodesItem
import com.example.cogtrainingdemo.util.Resource

class CharactersRepository {
    lateinit var charactersRemoteDataSource: CharactersRemoteDataSource
    companion object {
        private var INSTANCE: CharactersRepository? = null
        fun getInstance(charactersRemoteDataSource: CharactersRemoteDataSource): CharactersRepository {
            return INSTANCE ?: let {
                synchronized(CharactersRepository::class.java) {}
                INSTANCE = CharactersRepository()
                INSTANCE?.charactersRemoteDataSource = charactersRemoteDataSource
                INSTANCE as CharactersRepository
            }
        }

    }

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

    suspend fun getAllEpisodes(): Resource<List<EpisodesItem>> {
        return try {
            val response = charactersRemoteDataSource.getAllEpisodes()
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
