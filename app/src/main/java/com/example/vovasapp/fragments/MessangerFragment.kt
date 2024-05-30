package com.example.vovasapp.fragments

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.vovasapp.R
import com.example.vovasapp.adapter.MessageAdapter
import com.example.vovasapp.api.ApiListHistory
import com.example.vovasapp.api.ApiMessage
import com.example.vovasapp.databinding.FragmentMessangerBinding
import com.example.vovasapp.func.AuthInterceptor
import com.example.vovasapp.func.hideKeyboard
import com.example.vovasapp.func.showSimpleDialog
import com.example.vovasapp.func.showToast
import com.google.android.material.textfield.TextInputLayout
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.time.LocalDateTime
import java.util.Locale
import java.util.Objects

class MessangerFragment : Fragment() {

    private lateinit var binding: FragmentMessangerBinding
    private lateinit var listView: ListView
    private lateinit var messageList : List<String>
    private lateinit var arrayAdapter : ArrayAdapter<String>

    lateinit var outputTV: TextView
    private val REQUEST_CODE_SPEECH_INPUT = 1
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
        outputTV = binding.outPut
//        val time = LocalDateTime.now().toString()
//        val inflater = LayoutInflater.from(context)
//        val layout = inflater.inflate(R.layout.fragment_messanger, null)
//        var textview = layout.findViewById<TextView>(R.id.time)
//        textview.text = time

        val bundle = arguments
        if (bundle != null) {
            val params = bundle.getString("Name Neuron")
            idModel = bundle.getString("ID Model").toString()
            Log.d("BUNDLE", idModel)
            binding.nameNeuro.text = params
        }
        takeAllMessageList()
        messageList = mutableListOf()
        arrayAdapter = MessageAdapter(requireContext(), messageList)
        listView.adapter = arrayAdapter
        binding.send.setOnClickListener {
            if (binding.textEdit.text?.isNotEmpty()!!){
                sendMessageToServer(binding.textEdit.text.toString())
                arrayAdapter.notifyDataSetChanged()
                binding.textEdit.text = null
                hideKeyboard()
            }
        }
        binding.micOn.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text")
            try {
                startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(), "Error: " + e.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.info.setOnClickListener {
            showSimpleDialog(requireContext(), "Справка", "Общение с нейронкой, отправляйте ваше сообщение и получите ответ от нейронки. Хорошего использования!")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                val res: ArrayList<String> =
                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>
                binding.textEdit.setText(res[0])
            }
        }
    }

    fun sendMessageToServer(message : String){
        val editor = sharedPref.getString("token", "")
        val authInterceptor = AuthInterceptor(editor!!)
        val sharedPrefKey = context?.getSharedPreferences("Server", Context.MODE_PRIVATE)
        val editorServer = sharedPrefKey?.getString("serverAddress", "")

        val retrofit = Retrofit.Builder()
            .baseUrl("${editorServer}networks/${idModel}/")
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
                Log.d("MessengerFragment", t.message.toString())
            }
        })
    }

    fun takeAllMessageList(){
        val editor = sharedPref.getString("token", "")
        val authInterceptor = AuthInterceptor(editor!!)
        val sharedPrefKey = context?.getSharedPreferences("Server", Context.MODE_PRIVATE)
        val editorServer = sharedPrefKey?.getString("serverAddress", "")

        val retrofit = Retrofit.Builder()
            .baseUrl("${editorServer}networks/${idModel}/")
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(authInterceptor)
                    .build()
            )
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiListHistory::class.java)
        val postRequest = apiService.takeList()

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
                Log.d("MessengerFragment", t.message.toString())
            }
        })
    }
}