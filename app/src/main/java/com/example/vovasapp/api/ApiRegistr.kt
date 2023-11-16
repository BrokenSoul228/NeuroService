package com.example.vovasapp.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiRegistr {
    @GET("register")
    fun getJwtToken(@Query("login") login: String, @Query("password") password: String): Call<String>
}