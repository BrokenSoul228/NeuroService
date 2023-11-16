package com.example.vovasapp.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.vovasapp.R
import com.example.vovasapp.adapter.GptAdapter
import com.example.vovasapp.api.ApiService
import com.example.vovasapp.databinding.FragmentMainBinding
import com.example.vovasapp.dto.GptModel
import com.example.vovasapp.func.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class MainFragment : Fragment() {

    private lateinit var binding : FragmentMainBinding
    private lateinit var listView : ListView
    private lateinit var callback: OnBackPressedCallback
    private val sharedPreferencesKey = "MyPreferences"
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {}
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        sharedPref = context?.getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE)!!
        return binding.root
    }

    fun getNeuro(selectedModel : GptModel) {
        val bundle = Bundle().apply {
            putString("Name Neuron", selectedModel.name)
            putString("ID Model", selectedModel.id.toString())
        }
        val navController = findNavController()
        navController.navigate(R.id.action_mainFragment_to_messangerFragment, bundle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        sharedPref = context?.getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE)!!
        listView = binding.listView
        val gptModelList = mutableListOf<GptModel>()
        val gptAdapter = GptAdapter(requireContext(), gptModelList)
        listView.adapter = gptAdapter
        getGptModel(gptModelList, gptAdapter)
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedModel = gptAdapter.getItem(position) as? GptModel
            selectedModel?.let { getNeuro(it) }
        }
    }

    fun getGptModel(gptListView: MutableList<GptModel>, gptAdapter: GptAdapter) {

        val editor = sharedPref.getString("token", "")
        val authInterceptor = AuthInterceptor(editor!!)
        Log.d("EDITOR", editor)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.246.171:8080/")
            .client(OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .build())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.getGptName()

        call.enqueue(object : Callback<List<GptModel>> {
            override fun onResponse(call: Call<List<GptModel>>, response: Response<List<GptModel>>) {
                if (response.isSuccessful) {
                    val gptModelList = response.body()
                    gptModelList?.let {
                        gptListView.addAll(it)
                        gptAdapter.notifyDataSetChanged()
                    }
                } else {
                    Toast.makeText(requireContext(), "Ошибка получения с сервера", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<GptModel>>, t: Throwable) {
                Log.d("GPTRETROFIT", t.message.toString())
            }
        })
    }
//    private fun saveDescription(key : String ,token: String) {
//        val editor = sharedPref.edit()
//        editor.putString(key, token)
//        editor.apply()
//    }

//    fun getNeuroDesc(): String {
//        return sharedPref.getString("token", "") ?: ""
//    }

}