package com.example.vovasapp.neuro

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.vovasapp.R
import com.example.vovasapp.ViewModels
import com.example.vovasapp.databinding.FragmentScanDocBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions

class LablingImageFragment : Fragment() {

    lateinit var binding : FragmentScanDocBinding
    lateinit var imageUrl : Uri
    private val list = mutableListOf<String>()
    val viewModel : ViewModels by viewModels()

    val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent(),
        fun(it: Uri?) {
            try {
                imageUrl = it!!
                binding.mainImage.setImageURI(imageUrl)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScanDocBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
        var visible = true
        binding.load.setOnClickListener {
            try {
                initNeuro(labeler, imageUrl)
            } catch (e : Exception){
                Toast.makeText(requireContext(), "Фотография не загружена", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btn.setOnClickListener {
            galleryLauncher.launch("image/*")
        }
        binding.getResult.setOnClickListener {
            binding.listRes.visibility = if (visible){
                View.VISIBLE
            }
            else {
                View.GONE
            }
            binding.mainImage.visibility = if (!visible){
                View.VISIBLE
            }
            else {
                View.GONE
            }
            visible = !visible
        }
    }

    fun initNeuro(labeler: ImageLabeler, images : Uri){
        val image = InputImage.fromFilePath(requireContext(),images)
        list.clear()
        labeler.process(image)
            .addOnSuccessListener { labels ->
                for (label in labels) {
                    list.add("Объект: ${label.text}\nКоэффициент уверенности: ${label.confidence}")
                }
            }
            .addOnFailureListener { e ->
            }
        binding.listRes.adapter = ArrayAdapter(requireContext(), R.layout.messanger_item_style,list)
    }
}