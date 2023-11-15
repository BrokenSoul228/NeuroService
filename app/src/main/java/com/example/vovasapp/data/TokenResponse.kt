package com.example.vovasapp.data

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("jwtToken")
    val jwtToken: String
)
