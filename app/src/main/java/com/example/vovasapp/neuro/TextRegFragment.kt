package com.example.vovasapp.neuro

import android.net.Uri
import android.os.Bundle
import android.renderscript.ScriptGroup.Input
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.example.vovasapp.R
import com.example.vovasapp.adapter.AfterRecAdapter
import com.example.vovasapp.databinding.FragmentTextRegBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions


class TextRegFragment : Fragment() {

    lateinit var imageUrl : Uri
    lateinit var binding: FragmentTextRegBinding
    val list = mutableListOf<String>()

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
        binding = FragmentTextRegBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btn.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        binding.load.setOnClickListener {
            try {
                initNeuro(imageUrl)
            } catch (e : Exception){
                Toast.makeText(requireContext(), "Фотография не загружена", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun initNeuro(images : Uri){
        val image = InputImage.fromFilePath(requireContext(), images)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        list.clear()
        val result = recognizer.process(image)
            .addOnSuccessListener { visionText ->
                val resultText = visionText.text
                for (block in visionText.textBlocks) {
                    val blockText = block.text
                    val blockCornerPoints = block.cornerPoints
                    val blockFrame = block.boundingBox
                    for (line in block.lines) {
                        val lineText = line.text
                        val lineCornerPoints = line.cornerPoints
                        val lineFrame = line.boundingBox
                        for (element in line.elements) {
                            val elementText = element.text
                            val elementCornerPoints = element.cornerPoints
                            val elementFrame = element.boundingBox
                            list.add(elementText)
                        }
                    }
                }
                binding.listView.adapter = AfterRecAdapter(requireContext(), list, findNavController())
            }
            .addOnFailureListener { e ->
                // Task failed with an exception
                // ...
            }
    }
}