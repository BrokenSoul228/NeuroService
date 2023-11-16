package com.example.vovasapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.vovasapp.R

class MessageAdapter(context: Context, private val data: List<String>) : ArrayAdapter<String>(context, R.layout.messenger_item, data) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
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