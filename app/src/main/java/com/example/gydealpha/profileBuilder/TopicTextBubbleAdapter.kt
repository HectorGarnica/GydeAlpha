package com.example.gydealpha.profileBuilder


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.gydealpha.R

public class TopicTextBubbleAdapter(val context: Context, val stringList: ArrayList<String>) :
    BaseAdapter() {

    override fun getItem(position: Int): Any {
        return stringList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return stringList.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View = LayoutInflater.from(context).inflate(R.layout.selected_topic_item,parent,false)

        val gridItemString = view.findViewById<TextView>(R.id.topic_item_bubble) as TextView
        gridItemString.text = stringList.get(position)

        return view
    }
}
