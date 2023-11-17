package com.example.vovasapp.func

import android.app.AlertDialog
import android.content.Context
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