package com.example.vovasapp.api


import retrofit2.Call
import retrofit2.http.GET

interface ApiListHistory {
    @GET(".")
    fun takeList() : Call<List<String>>
}