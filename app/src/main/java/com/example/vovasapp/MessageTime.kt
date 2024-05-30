package com.example.vovasapp

import android.content.Context
import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class MessageTime(view: View, context: Context) : View(context) {
    val formatter = DateTimeFormatter.ISO_TIME
    val time = LocalDateTime.now().format(formatter)
    var data = view.findViewById<TextView>(R.id.text_gchat_timestamp_me_second)

    fun setTime(){
        data.text = time
    }
}