package com.example.vovasapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewModels : ViewModel() {
    val listResult : MutableLiveData<List<String>> by lazy {
        MutableLiveData<List<String>>()
    }
}