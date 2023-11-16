package com.example.vovasapp.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiMessage  {
    @POST(".")
    fun sendRequest(@Body requestBody: String) : Call<List<String>>
}