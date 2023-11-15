package com.example.vovasapp.api

import com.example.vovasapp.data.gptName
import com.example.vovasapp.dto.GptModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface ApiService {
    @Headers("Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ2b3ZhMSIsImlhdCI6MTcwMDAzNTgzMCwiZXhwIjoxNzAyNjI3ODMwfQ.QP7o9Us84DM8q8FChbzzCvbVRfLJXOOcU2VNUHUrtRs")
    @GET("networks")
    fun getGptName(): Call<List<GptModel>>
}