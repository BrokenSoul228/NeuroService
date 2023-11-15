package com.example.vovasapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.example.vovasapp.R
import com.example.vovasapp.api.ApiMessage
import com.example.vovasapp.api.ApiService
import com.example.vovasapp.databinding.FragmentMessangerBinding
import com.example.vovasapp.dto.GptModel
import com.example.vovasapp.dto.MessageFromServer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class MessangerFragment : Fragment() {

    private lateinit var binding: FragmentMessangerBinding
    private lateinit var listView: ListView
    private lateinit var message : List<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMessangerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView = binding.listView
        message = mutableListOf(
            ""
        )
        val arrayAdapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, message)
        listView.adapter = arrayAdapter
        val bundle = arguments
        if (bundle != null) {
            val params = bundle.getString("Name Neuron")
            binding.nameNeuro.text = params
        }
        binding.send.setOnClickListener {
            if (binding.textEdit.text?.isNotEmpty()!!){
                sendMessageToServer()
                arrayAdapter.notifyDataSetChanged()
            }
        }
    }

    fun sendMessageToServer(){
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.246.171:8080/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiMessage::class.java)
        val call = apiService.sendRequest()

        call.enqueue(object : Callback<List<MessageFromServer>> {
            override fun onResponse(call: Call<List<MessageFromServer>>, response: Response<List<MessageFromServer>>) {
                if (response.isSuccessful) {
                    val messageResponse = response.body()
                    (message as MutableList<String>).add(messageResponse.toString())
                }
            else {
                    Toast.makeText(requireContext(), "Ошибка получения с сервера", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<MessageFromServer>>, t: Throwable) {
                Log.d("GPTRETROFIT", t.message.toString())
            }
        })
    }

}