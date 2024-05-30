package com.example.vovasapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.vovasapp.R
import com.example.vovasapp.databinding.FragmentOpenBinding

class OpenFragment : Fragment() {

    lateinit var binding : FragmentOpenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOpenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(requireContext()).load(R.drawable.registr).into(binding.openBack)
        binding.offlineMode.setOnClickListener {
            findNavController().navigate(R.id.action_openFragment_to_offlineModeFragment)
        }
    }
}