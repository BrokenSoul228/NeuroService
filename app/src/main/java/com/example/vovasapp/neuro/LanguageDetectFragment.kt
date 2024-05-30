package com.example.vovasapp.neuro

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.vovasapp.R
import com.example.vovasapp.databinding.FragmentLanguageDetectBinding
import com.google.mlkit.nl.languageid.LanguageIdentification

class LanguageDetectFragment : Fragment() {

    lateinit var binding : FragmentLanguageDetectBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLanguageDetectBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arg = arguments?.getString("key")
        binding.editor.setText(arg)
        binding.button.setOnClickListener {
            val languageIdentifier = LanguageIdentification.getClient()
            var lang = ""
            languageIdentifier.identifyPossibleLanguages(binding.editor.text.toString())
                .addOnSuccessListener { identifiedLanguages ->
                    for (identifiedLanguage in identifiedLanguages) {
                        val language = identifiedLanguage.languageTag
                        val confidence = identifiedLanguage.confidence
                        lang += languageOfCode(language) + " Вероятность: $confidence" +"\n"
                        Log.i("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!", "$language $confidence")
                    }
                    binding.outPut.setText("Нейронная сеть считает что этот текст подходит под такие языкки как:\n$lang")
                }
                .addOnFailureListener {
                    binding.outPut.setText("Ошибка определения языка")
                }
        }
    }

    fun languageOfCode(languageTag: String): String {
        return when (languageTag) {
            "af" -> "Afrikaans"
            "am" -> "Amharic"
            "ar" -> "Arabic"
            "ar-Latn" -> "Arabic"
            "az" -> "Azerbaijani"
            "be" -> "Belarusian"
            "bg" -> "Bulgarian"
            "bg-Latn" -> "Bulgarian"
            "bn" -> "Bengali"
            "bs" -> "Bosnian"
            "ca" -> "Catalan"
            "ceb" -> "Cebuano"
            "co" -> "Corsican"
            "cs" -> "Czech"
            "cy" -> "Welsh"
            "da" -> "Danish"
            "de" -> "German"
            "el" -> "Greek"
            "el-Latn" -> "Greek"
            "en" -> "English"
            "eo" -> "Esperanto"
            "es" -> "Spanish"
            "et" -> "Estonian"
            "eu" -> "Basque"
            "fa" -> "Persian"
            "fi" -> "Finnish"
            "fil" -> "Filipino"
            "fr" -> "French"
            "fy" -> "Western Frisian"
            "ga" -> "Irish"
            "gd" -> "Scots Gaelic"
            "gl" -> "Galician"
            "gu" -> "Gujarati"
            "ha" -> "Hausa"
            "haw" -> "Hawaiian"
            "he" -> "Hebrew"
            "hi" -> "Hindi"
            "hi-Latn" -> "Hindi"
            "hmn" -> "Hmong"
            "hr" -> "Croatian"
            "ht" -> "Haitian"
            "hu" -> "Hungarian"
            "hy" -> "Armenian"
            "id" -> "Indonesian"
            "ig" -> "Igbo"
            "is" -> "Icelandic"
            "it" -> "Italian"
            "ja" -> "Japanese"
            "ja-Latn" -> "Japanese"
            "jv" -> "Javanese"
            "ka" -> "Georgian"
            "kk" -> "Kazakh"
            "km" -> "Khmer"
            "kn" -> "Kannada"
            "ko" -> "Korean"
            "ku" -> "Kurdish"
            "ky" -> "Kyrgyz"
            "la" -> "Latin"
            "lb" -> "Luxembourgish"
            "lo" -> "Lao"
            "lt" -> "Lithuanian"
            "lv" -> "Latvian"
            "mg" -> "Malagasy"
            "mi" -> "Maori"
            "mk" -> "Macedonian"
            "ml" -> "Malayalam"
            "mn" -> "Mongolian"
            "mr" -> "Marathi"
            "ms" -> "Malay"
            "mt" -> "Maltese"
            "my" -> "Burmese"
            "ne" -> "Nepali"
            "nl" -> "Dutch"
            "no" -> "Norwegian"
            "ny" -> "Nyanja"
            "pa" -> "Punjabi"
            "pl" -> "Polish"
            "ps" -> "Pashto"
            "pt" -> "Portuguese"
            "ro" -> "Romanian"
            "ru" -> "Russian"
            "ru-Latn" -> "Russian"
            "sd" -> "Sindhi"
            "si" -> "Sinhala"
            "sk" -> "Slovak"
            "sl" -> "Slovenian"
            "sm" -> "Samoan"
            "sn" -> "Shona"
            "so" -> "Somali"
            "sq" -> "Albanian"
            "sr" -> "Serbian"
            "st" -> "Sesotho"
            "su" -> "Sundanese"
            "sv" -> "Swedish"
            "sw" -> "Swahili"
            "ta" -> "Tamil"
            "te" -> "Telugu"
            "tg" -> "Tajik"
            "th" -> "Thai"
            "tr" -> "Turkish"
            "uk" -> "Ukrainian"
            "ur" -> "Urdu"
            "uz" -> "Uzbek"
            "vi" -> "Vietnamese"
            "xh" -> "Xhosa"
            "yi" -> "Yiddish"
            "yo" -> "Yoruba"
            "zh" -> "Chinese"
            "zh-Latn" -> "Chinese"
            "zu" -> "Zulu"
            else -> {
                "Язык неизвестен"
            }
        }
    }}