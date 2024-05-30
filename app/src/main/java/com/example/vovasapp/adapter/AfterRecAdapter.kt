package com.example.vovasapp.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.navigation.NavController
import com.example.vovasapp.R
import com.example.vovasapp.func.showDialogForTranslate

class AfterRecAdapter(private val context: Context, private val data: List<String>,private val controller : NavController) : BaseAdapter() {
    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = convertView ?: inflater.inflate(R.layout.output_messages, parent, false)
        val text = view.findViewById<TextView>(R.id.myTextView)
        text.setOnLongClickListener { view->
            showDialogForTranslate(
                context = context,
                title = "Перевод",
                message = "Желаете перевести данный текст?",
                arg = text.text.toString(),
                controller = controller
            )
            true
        }
        val currentItem = data[position]
        text.text = currentItem
        return view
    }
}