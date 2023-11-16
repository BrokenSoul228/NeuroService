package com.example.vovasapp.dto

import java.util.UUID

data class GptModel (
    val name: String  ,
    val description: String,
    val id : UUID
)

