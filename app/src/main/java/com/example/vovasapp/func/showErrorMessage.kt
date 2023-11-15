package com.example.vovasapp.func

import com.google.android.material.textfield.TextInputLayout

fun showErrorMessage(item : TextInputLayout, message: String) {
    item.error = message
}