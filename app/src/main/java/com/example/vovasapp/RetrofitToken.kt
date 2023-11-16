package com.example.vovasapp

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.vovasapp.api.ApiRegistr
import com.example.vovasapp.api.ApiToken
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class RetrofitToken(val context: Context) {

    private val sharedPreferencesKey = "MyPreferences"
    private var sharedPref: SharedPreferences

    init {
        sharedPref = context.getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE)
    }

    fun takeToken(mainLogin : String, mainPassword : String, callback: (String, String?, String?) -> Unit) {

        val gson = GsonBuilder().setLenient().create()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.246.171:8080/auth/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val apiService = retrofit.create(ApiToken::class.java)
        val call = apiService.getJwtToken(login = mainLogin, password = mainPassword)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    if (response.body().toString().contains("Неверный пароль")){
                        val passwordFailed = response.toString()
                        callback("", passwordFailed ,"")
                    }
                    else if (response.body().toString().contains("Пользователь не найден")) {
                        val loginFailed = response.toString()
                        callback("", "" ,loginFailed)
                    }
                    else {
                        val tokenResponse: String? = response.body()
                        saveToken(mainLogin, tokenResponse ?: "")
                        callback(tokenResponse!!, "", "")
                    }
                } else {
                    Log.d("RETROFIT", "ПЛОХОЙ ЗАПРОС")
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("RETROFIT", "ПЛОХОЙ ОТВЕТ ${t.message}", t)
            }
        })
    }

    fun takeTokenAfterReg(mainLogin : String, mainPassword : String, callback: (String, String?, String?) -> Unit){
        val gson = GsonBuilder().setLenient().create()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.246.171:8080/auth/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val apiService = retrofit.create(ApiRegistr::class.java)
        val call = apiService.getJwtToken(login = mainLogin, password = mainPassword)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    if (response.body().toString().contains("Пароль должен содержать минимум 8 символов.")){
                        val passwordFailed = response.toString()
                        callback("", passwordFailed ,"")
                    }
                    else if (response.body().toString().contains("Такой пользователь уже зарегистрирован.")) {
                        val loginFailed = response.toString()
                        callback("", "" ,loginFailed)
                    }
                    else {
                        val tokenResponse: String? = response.body()
                        saveToken(mainLogin, tokenResponse ?: "")
                        callback(tokenResponse!!, "", "")
                    }
                } else {
                    Log.d("RETROFIT", "ПЛОХОЙ ЗАПРОС")
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("RETROFIT", "ПЛОХОЙ ОТВЕТ ${t.message}", t)
            }
        })
    }

    // Функция для сохранения токена
    private fun saveToken(key : String ,token: String) {
        val editor = sharedPref.edit()
        editor.putString(key, token)
        editor.apply()
    }
}