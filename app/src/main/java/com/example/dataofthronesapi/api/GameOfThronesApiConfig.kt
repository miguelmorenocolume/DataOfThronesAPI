package com.example.dataofthronesapi.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GameOfThronesApiConfig {

    private const val BASE_URL = "https://thronesapi.com/api/v2/"

    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}