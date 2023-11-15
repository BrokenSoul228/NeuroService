package com.example.vovasapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.vovasapp.R
import com.example.vovasapp.databinding.FragmentRegistrBinding
import com.example.vovasapp.func.showErrorMessage

class RegistrFragment : Fragment() {

    private lateinit var binding: FragmentRegistrBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSingIn.setOnClickListener {
                if (binding.edEmail.text?.isEmpty() == true && binding.edPassword.text?.isEmpty() == true) {
                    binding.textInputLayout2.error = "Empty login"
                    binding.textInputLayout3.error = "Empty password"

                    binding.textInputLayout2.editText?.setOnFocusChangeListener { _, hasFocus ->
                        if (!hasFocus) {
                            if (binding.textInputLayout2.editText?.text.isNullOrEmpty()) {
                                showErrorMessage(binding.textInputLayout2, "Empty")
                            } else {
                                binding.textInputLayout2.error = null
                            }
                        }
                    }

                    binding.textInputLayout3.editText?.setOnFocusChangeListener { _, hasFocus ->
                        if (!hasFocus) {
                            if (binding.textInputLayout3.editText?.text.isNullOrEmpty()) {
                                showErrorMessage(binding.textInputLayout3, "Empty")
                            } else {
                                binding.textInputLayout3.error = null
                            }
                        }
                    }
                }
                else {
                    findNavController().navigate(R.id.action_registrFragment2_to_loginFragment)
                }
        }
        binding.singIn.setOnClickListener {
            findNavController().navigate(R.id.action_registrFragment2_to_loginFragment)
        }
    }
}