package com.example.cogtrainingdemo.data.api

import com.example.cogtrainingdemo.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestApi {
    companion object {

        private var services: CharactersRemoteDataSource? = null

        fun getRetrofitInstance(): CharactersRemoteDataSource {
            if (services == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                services = retrofit.create(CharactersRemoteDataSource::class.java)

            }
            return requireNotNull(services)
        }
    }
}
