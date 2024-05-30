package com.example.vovasapp.func

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import com.example.vovasapp.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun showSimpleDialog(context: Context, title : String, message : String, isCancelable: Boolean = true){
    MaterialAlertDialogBuilder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        .setCancelable(isCancelable)
        .show()
}

fun showDialogForTranslate(context: Context, title: String, message: String, isCancelable: Boolean = true, arg : String, controller: NavController) {
    MaterialAlertDialogBuilder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton("Да") { dialog, _ ->
            goToTranslate(arg, controller)
            dialog.dismiss()
        }
        .setNegativeButton("Нет") { dialog, _ -> dialog.dismiss() }
        .setCancelable(isCancelable)
        .show()
}

private fun goToTranslate(text : String, controller : NavController){
    val arg = Bundle()
    arg.putString("key", text)
    controller.navigate(R.id.action_textRegFragment_to_languageDetectFragment, arg)
}