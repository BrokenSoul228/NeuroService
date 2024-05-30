package com.example.vovasapp.neuro

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.vovasapp.R
import com.example.vovasapp.databinding.FragmentObjectRegBinding
import com.example.vovasapp.func.showSimpleDialog
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import com.google.mlkit.vision.objects.defaults.PredefinedCategory
import retrofit2.http.Url

class ObjectRegFragment : Fragment() {

    lateinit var imageUrl : Uri
    lateinit var binding : FragmentObjectRegBinding

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
        binding = FragmentObjectRegBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getPermission()
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
        val options = ObjectDetectorOptions.Builder()
            .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
            .enableMultipleObjects()
            .enableClassification()
            .build()

        val objectDetector = ObjectDetection.getClient(options)

        objectDetector.process(image)
            .addOnSuccessListener { detectedObjects ->
                Log.d("!!!!!!!!!!!!!!!!!!!!", "!!!!!!!!!!!!!!!!!!!")
                print(detectedObjects)
                for (detectedObject in detectedObjects) {
                    val boundingBox = detectedObject.boundingBox
                    val trackingId = detectedObject.trackingId
                    var text1 = ""
                    for (label in detectedObject.labels) {
                        val text = label.text
                        when(text){
                            PredefinedCategory.FASHION_GOOD -> getResultOfNeuro(text, label.confidence)
                            PredefinedCategory.FOOD -> getResultOfNeuro(text, label.confidence)
                            PredefinedCategory.HOME_GOOD -> getResultOfNeuro(text, label.confidence)
                            PredefinedCategory.PLACE ->getResultOfNeuro(text, label.confidence)
                            PredefinedCategory.PLANT -> getResultOfNeuro(text, label.confidence)
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "NOT", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getPermission() {
        if (Build.VERSION.SDK_INT < 33) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            }
        }
        else {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                AppCompatActivity().requestPermissions(
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    1
                )
            }
        }
    }

    fun getResultOfNeuro(nameOfGood : String, percent : Float){
        showSimpleDialog(requireContext(),"Результат", "Нейронная сеть считает что это: $nameOfGood \nС вероятность в $percent")
    }

}