package com.example.vovasapp.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.vovasapp.R
import com.example.vovasapp.RetrofitToken
import com.example.vovasapp.api.ApiToken
import com.example.vovasapp.databinding.FragmentLoginBinding
import com.example.vovasapp.func.saveAddressServer
import com.example.vovasapp.func.setGif
import com.example.vovasapp.func.showErrorMessage
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


class LoginFragment : Fragment() {

    private lateinit var binding : FragmentLoginBinding
    lateinit var outputTV: TextView
    private val REQUEST_CODE_SPEECH_INPUT = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun checkTokenInSharedPreferences(): Boolean {
        val sharedPreferences = context?.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val token = sharedPreferences?.getString("token", "")
        Log.d("checkTokenInSharedPreferences", token!!)
        return token.isNotEmpty()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setGif(requireContext(), R.drawable.registr, binding.background)
        binding.serverId.setOnClickListener{
            showAlertWithEditText(requireContext())
        }
        val shared = context?.getSharedPreferences("Server", Context.MODE_PRIVATE)
        val editor = shared?.getString("server", "")
        outputTV = binding.outPut
        val generateToken = RetrofitToken(requireContext())
        binding.register.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrFragment2)
        }
        val checkExpired = MainFragment().checkExpired(requireContext()) { response, notResp ->
                if (response.isNullOrEmpty() || response.isNullOrBlank()) {
                    Log.d("NOTEXPIRED", response)
                    binding.singIn.setOnClickListener {
                        if (binding.edEmail.text?.isEmpty() == true) {
                            binding.textInputLayout2.error = "Empty login"

                            binding.textInputLayout2.editText?.doAfterTextChanged {
                                if (binding.textInputLayout2.editText?.text.isNullOrEmpty()) {
                                    showErrorMessage(binding.textInputLayout2, "Empty")
                                } else {
                                    binding.textInputLayout2.error = null
                                }
                            }
                        } else if (binding.edPassword.text?.isEmpty() == true) {
                            binding.textInputLayout3.error = "Empty password"
                            binding.textInputLayout3.editText?.doAfterTextChanged {
                                if (binding.textInputLayout3.editText?.text.isNullOrEmpty()) {
                                    showErrorMessage(binding.textInputLayout3, "Empty")
                                } else {
                                    binding.textInputLayout3.error = null
                                }
                            }
                        } else {
                            val sharedPreferences =
                                context?.getSharedPreferences("ExpiredToken", Context.MODE_PRIVATE)
                            val editorLogin = sharedPreferences?.edit()
                                ?.putString("login", binding.edEmail.text.toString())?.apply()
                            val editorPassword = sharedPreferences?.edit()
                                ?.putString("password", binding.edPassword.text.toString())?.apply()
                            generateToken.takeToken(
                                binding.edEmail.text.toString(),
                                binding.edPassword.text.toString()
                            ) { token, passwordFailed, loginFailed ->
                                if (token.isNotEmpty()) {
                                    saveTokenToSharedPreferences(token)
                                    Log.d("TOKEN", token)
                                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                                } else if (loginFailed?.isNotEmpty()!!) {
                                    Toast.makeText(
                                        requireContext(),
                                        "Пользователь не найден",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else if (passwordFailed?.isNotEmpty()!!) {
                                    Toast.makeText(
                                        requireContext(),
                                        "Неверный пароль",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            }
                        }
                    }
                } else {
                    if (checkTokenInSharedPreferences()) {
                        findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                    }
                }
        }
    }


    fun showAlertWithEditText(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Введите адрес")
        val inflater = LayoutInflater.from(context)
        val inputLayout = inflater.inflate(R.layout.edit_text_view, null) as TextInputLayout
        builder.setView(inputLayout)
        val input = inputLayout.editText?.text
        builder.setPositiveButton("OK") { dialog, which ->
            binding.serverId.text = input
            val enteredText = input
            saveAddressServer(enteredText.toString(), requireContext())
        }

        // Установка кнопки "Отмена" для закрытия диалогового окна
        builder.setNegativeButton("Отмена") { dialog, which ->
            dialog.cancel()
        }

        // Отображение диалогового окна
        builder.show()
    }

    fun saveTokenToSharedPreferences(token: String) {
        val sharedPreferences = context?.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putString("token", token)?.apply()
        Log.d("saveTokenToSharedPreferences", token)
    }
}