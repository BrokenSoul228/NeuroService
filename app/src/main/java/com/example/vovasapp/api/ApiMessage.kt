package com.example.vovasapp.api

import com.example.vovasapp.dto.GptModel
import com.example.vovasapp.dto.MessageFromServer
import retrofit2.Call
import retrofit2.http.GET

interface ApiMessage  {
    @GET("")
    fun sendRequest(): Call<List<MessageFromServer>>

}