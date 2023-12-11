package com.example.vovasapp.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.vovasapp.R
import com.example.vovasapp.adapter.GptAdapter
import com.example.vovasapp.api.ApiService
import com.example.vovasapp.databinding.FragmentMainBinding
import com.example.vovasapp.dto.GptModel
import com.example.vovasapp.func.AuthInterceptor
import com.example.vovasapp.func.showSimpleDialog
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
    private val sharedPreferencesKeyServer = "Server"
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = context?.getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE)!!
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
//        Log.d("POCHEMYYYYYY",checkExpired(requireContext()))
        listView = binding.listView
        val gptModelList = mutableListOf<GptModel>()
        val gptAdapter = GptAdapter(requireContext(), gptModelList)
        listView.adapter = gptAdapter
        getGptModel(gptModelList, gptAdapter)
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedModel = gptAdapter.getItem(position) as? GptModel
            selectedModel?.let { getNeuro(it) }
        }
        binding.info.setOnClickListener {
            showSimpleDialog(requireContext(), "Справка", "Это страница со списком нейронных сетей, которые могут выполнять различные действия , необходимые человеку в разных сферах деятельности.")
        }
    }

    fun getGptModel(gptListView: MutableList<GptModel>, gptAdapter: GptAdapter) {
        val editor = sharedPref.getString("token", "")
        val authInterceptor = AuthInterceptor(editor!!)
        Log.d("EDITOR", editor)
        val sharedPreferences = context?.getSharedPreferences(sharedPreferencesKeyServer, Context.MODE_PRIVATE)
        var editorServer = sharedPreferences?.getString("serverAddress", "")
        Log.d("SERVERRETROFIT", editorServer!!)
        if (editorServer.isEmpty() == true || editorServer.isBlank() == true){
            editorServer = "http://192.168.246.171:8080/"
        }
        val retrofit = Retrofit.Builder()
            .baseUrl(editorServer)
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
                    findNavController().navigate(R.id.action_mainFragment_to_messangerFragment)
                }
            }

            override fun onFailure(call: Call<List<GptModel>>, t: Throwable) {
                Log.d("MainFragment", t.message.toString())
            }
        })
    }
//    private fun saveDescription(key : String ,token: String) {
//        val editor = sharedPref.edit()
//        editor.putString(key, token)
//        editor.apply()
//    }

    fun checkExpired(context: Context, callback: (String, String?) -> Unit) {
        val sharedPrefKey = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)!!
        val editor = sharedPrefKey.getString("token", "")
        val authInterceptor = AuthInterceptor(editor!!)
        Log.d("EDITOR", editor)
        var endResponse : String
        val sharedPreferences = context.getSharedPreferences(sharedPreferencesKeyServer, Context.MODE_PRIVATE)
        var editorServer = sharedPreferences?.getString("serverAddress", "")
        Log.d("SERVERRETROFIT", editorServer!!)
        if (editorServer.isEmpty() || editorServer.isBlank()){
            editorServer = "http://192.168.246.171:8080/"
        }
        val retrofit = Retrofit.Builder()
            .baseUrl(editorServer)
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
                    val gptModelList = response.body().toString()
                    endResponse = gptModelList
                    Log.d("ENDRESPONSE", endResponse)
                    callback(endResponse?: "", "NotResp")
                }
                else {
                    endResponse = ""
                    callback(endResponse, "")
                    Log.d("CALLLLLLLLLL", endResponse)
                    Log.d("Server not response", "OTVEJAY")
                }
            }

            override fun onFailure(call: Call<List<GptModel>>, t: Throwable) {
                Log.d("MainFragmentCheckExpired", t.message.toString())
                Log.d("Server not response", "ON V OnFailure")
                val nothing = ""
                callback(nothing, "NotResp")
            }
        })
    }

//    fun getNeuroDesc(): String {
//        return sharedPref.getString("token", "") ?: ""
//    }

//    fun checkTokenExpired() : String {
//
//        val gson = GsonBuilder().setLenient().create()
//        val sharedPreferences = context?.getSharedPreferences("ExpiredToken", Context.MODE_PRIVATE)
//        val mainLogin = sharedPreferences?.getString("login", "")!!
//        val mainPassword = sharedPreferences.getString("password", "")!!
//        Log.d("EXPIRED", mainPassword.toString())
//
//        val sharedPreferencesKey = context?.getSharedPreferences(sharedPreferencesKeyServer, Context.MODE_PRIVATE)
//        val editorServer = sharedPreferencesKey?.getString("serverAddress", "")
//
//        val retrofit = Retrofit.Builder()
//            .baseUrl("${editorServer}network/")
//            .addConverterFactory(ScalarsConverterFactory.create())
//            .addConverterFactory(GsonConverterFactory.create(gson))
//            .build()
//
//        val apiService = retrofit.create(ApiToken::class.java)
//        val call = apiService.getJwtToken(login = mainLogin, password = mainPassword)
//
//        call.enqueue(object : Callback<String> {
//            override fun onResponse(call: Call<String>, response: Response<String>) {
//                if (response.isSuccessful) {
//                    val passwordFailed = response.toString()
//                    Log.d("AAAAAAAAAAAAA", passwordFailed)
//                } else
//                {
//                    Log.d("RETROFIT", "ПЛОХОЙ ЗАПРОС")
//                }
//            }
//            override fun onFailure(call: Call<String>, t: Throwable) {
//                Log.d("RETROFIT", "ПЛОХОЙ ОТВЕТ ${t.message}", t)
//            }
//        })
//        return mainPassword
//    }
}