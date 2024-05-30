package com.example.vovasapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.vovasapp.databinding.FragmentOfflineModeBinding


class OfflineModeFragment : Fragment() {

    lateinit var binding : FragmentOfflineModeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOfflineModeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.objRec.setOnClickListener {
            findNavController().navigate(R.id.action_offlineModeFragment_to_objectRegFragment)
        }

        binding.textRec.setOnClickListener {
            findNavController().navigate(R.id.action_offlineModeFragment_to_textRegFragment)
        }

        binding.labling.setOnClickListener {
            findNavController().navigate(R.id.action_offlineModeFragment_to_lablingFragment)
        }

        binding.languageRecog.setOnClickListener {
            findNavController().navigate(R.id.action_offlineModeFragment_to_languageDetectFragment)
        }
    }
}