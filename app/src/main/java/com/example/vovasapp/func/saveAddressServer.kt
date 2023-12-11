package com.example.vovasapp.func

import android.content.Context
import android.util.Log

fun saveAddressServer(value : String, context: Context){
    val shared = context.getSharedPreferences("Server", Context.MODE_PRIVATE)
    val editor = shared?.edit()?.putString("serverAddress", value)?.apply()
    Log.d("SERVERADDRESS", value)
}