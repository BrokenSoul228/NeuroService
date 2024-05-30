package com.example.vovasapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.vovasapp.R
import com.example.vovasapp.dto.GptModel

class GptAdapter(private val context: Context, private val data: List<GptModel>) : BaseAdapter() {

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
        val view = convertView ?: inflater.inflate(R.layout.list_item, parent, false)

        var droped : Boolean = false

        val neuroNameTextView = view.findViewById<TextView>(R.id.neuroName)
        val descriptionTextView = view.findViewById<TextView>(R.id.description)
        val uuid = view.findViewById<TextView>(R.id.uuid)
        val dropdown = view.findViewById<ImageView>(R.id.dropdown)
        dropdown.setOnClickListener {
            if(!droped){
                descriptionTextView.visibility = View.VISIBLE
                droped = true
                dropdown.setImageResource(R.drawable.untitled)
            } else {
                descriptionTextView.visibility = View.GONE
                droped = false
                dropdown.setImageResource(R.drawable.drop)
            }
        }

        val currentItem = data[position]

        neuroNameTextView.text = currentItem.name
        descriptionTextView.text = currentItem.description
        uuid.text = currentItem.id.toString()

        return view
    }
}