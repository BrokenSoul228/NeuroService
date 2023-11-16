package com.example.vovasapp.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.vovasapp.adapter.MessageAdapter
import com.example.vovasapp.api.ApiMessage
import com.example.vovasapp.databinding.FragmentMessangerBinding
import com.example.vovasapp.func.AuthInterceptor
import com.example.vovasapp.func.showToast
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class MessangerFragment : Fragment() {

    private lateinit var binding: FragmentMessangerBinding
    private lateinit var listView: ListView
    private lateinit var messageList : List<String>
    private lateinit var arrayAdapter : ArrayAdapter<String>

    private val sharedPreferencesKey = "MyPreferences"
    private lateinit var sharedPref: SharedPreferences

    private var idModel : String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMessangerBinding.inflate(inflater, container, false)
        sharedPref = requireContext().getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView = binding.listView
        messageList = mutableListOf("Hello")
        arrayAdapter = MessageAdapter(requireContext(), messageList)
        listView.adapter = arrayAdapter
        val bundle = arguments
        if (bundle != null) {
            val params = bundle.getString("Name Neuron")
            idModel = bundle.getString("ID Model").toString()
            Log.d("BUNDLE", idModel)
            binding.nameNeuro.text = params
        }
        binding.send.setOnClickListener {
            if (binding.textEdit.text?.isNotEmpty()!!){
                sendMessageToServer(binding.textEdit.text.toString())
                arrayAdapter.notifyDataSetChanged()
                binding.textEdit.text = null
            }
        }
    }

    fun sendMessageToServer(message : String){
        val editor = sharedPref.getString("token", "")
        val authInterceptor = AuthInterceptor(editor!!)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.246.171:8080/networks/${idModel}/")
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(authInterceptor)
                    .build()
            )
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiMessage::class.java)
        val postRequest = apiService.sendRequest(requestBody = message)

        postRequest.enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>> ) {
                if (response.isSuccessful) {
                    val messageResponse = response.body()
                    messageResponse?.let {
                        messageList = it
                        arrayAdapter.clear()
                        arrayAdapter.addAll(messageList)
                        arrayAdapter.notifyDataSetChanged()
                    }
                }
            else {
                showToast(requireContext(), "Ошибка получения с сервера")
                }
            }

            override fun onFailure(call: Call<List<String>> , t: Throwable) {
                Log.d("GPTRETROFIT", t.message.toString())
            }
        })
    }
}