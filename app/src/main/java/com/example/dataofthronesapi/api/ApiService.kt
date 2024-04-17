package com.example.dataofthronesapi.api

import com.example.dataofthronesapi.data.FullCharacter
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("Characters/{id}")
    suspend fun getFullCharacter(@Path("id") id: Int): Response<FullCharacter>

}