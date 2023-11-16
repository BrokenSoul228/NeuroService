package com.example.vovasapp.api

import com.example.vovasapp.dto.GptModel
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("networks")
    fun getGptName(): Call<List<GptModel>>
}