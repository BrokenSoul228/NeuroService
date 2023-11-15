package com.example.vovasapp.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.example.vovasapp.R
import com.example.vovasapp.RetrofitToken
import com.example.vovasapp.databinding.FragmentLoginBinding
import com.example.vovasapp.func.showErrorMessage

class LoginFragment : Fragment() {

    private lateinit var binding : FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun checkTokenInSharedPreferences(): Boolean {
        val sharedPreferences = context?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences?.getString("token", "")
        return token?.isNotEmpty()!!
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val generateToken = RetrofitToken(requireContext())
        binding.register.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrFragment2)
        }
        if (checkTokenInSharedPreferences()) {
            findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
        } else {
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
                }
                else if (binding.edPassword.text?.isEmpty() == true){
                    binding.textInputLayout3.error = "Empty password"
                    binding.textInputLayout3.editText?.doAfterTextChanged {
                            if (binding.textInputLayout3.editText?.text.isNullOrEmpty()) {
                                showErrorMessage(binding.textInputLayout3, "Empty")
                            } else {
                                binding.textInputLayout3.error = null
                            }
                    }
                }
                else {
                    generateToken.takeToken(binding.edEmail.text.toString(), binding.edPassword.text.toString()) { token, passwordFailed ,loginFailed ->
                        if (token.isNotEmpty()){
                            saveTokenToSharedPreferences(token)
                            Log.d("TOKEN",token)
                            findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                        }
                        else if (loginFailed?.isNotEmpty()!!){
                            Toast.makeText(requireContext(), "Пользователь не найден", Toast.LENGTH_SHORT).show()
                        }
                        else if (passwordFailed?.isNotEmpty()!!){
                            Toast.makeText(requireContext(), "Неверный пароль", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    fun saveTokenToSharedPreferences(token: String) {
        val sharedPreferences = context?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putString("token", token)?.apply()
    }

}