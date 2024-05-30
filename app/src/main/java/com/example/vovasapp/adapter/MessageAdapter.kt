package com.example.vovasapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.vovasapp.MessageTime
import com.example.vovasapp.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MessageAdapter(context: Context, private val data: List<String>) : ArrayAdapter<String>(context, R.layout.messenger_item, data) {

    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) {
            0 // Четные элементы
        } else {
            1 // Нечетные элементы
        }
    }

    override fun getViewTypeCount(): Int {
        return 2 // Возвращаем количество различных типов макетов
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        var view = convertView
//        val viewType = getItemViewType(position)
//        val timestampTextView = view?.findViewById<TextView>(R.id.text_gchat_timestamp_me_second)
//        val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
//        timestampTextView?.text = currentTime
//
//
////        if (view == null) {
////            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
////            view = if (viewType == 0) {
////                MessageTime(view)
////            } else {
////                inflater.inflate(R.layout.messenger_item, parent, false)
////            }
////        }
//
//        val textView = view!!.findViewById<TextView>(R.id.myTextView)
//        textView.text = getItem(position)
        var view = convertView
        if (view == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.messenger_item, parent, false)
        }

        val textView = view!!.findViewById<TextView>(R.id.myTextView)
        textView.text = getItem(position)

        if (position % 2 == 0) {
            textView.setBackgroundColor(ContextCompat.getColor(context, R.color.peach)) // Четные элементы - красный
        } else {
            textView.setBackgroundColor(ContextCompat.getColor(context, R.color.neon)) // Нечетные элементы - зеленый
        }
        return view
    }
}
