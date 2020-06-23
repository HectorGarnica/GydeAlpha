package com.example.gydealpha.profileBuilder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.gydealpha.R

public class TopicsGridViewAdapter(val context: Context, val images: IntArray, val stringList: Array<String>) : BaseAdapter(){

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
        val view: View = LayoutInflater.from(context).inflate(R.layout.grid_view_topic_item,parent,false)

        val gridItemString = view.findViewById<TextView>(R.id.gridTextViewTopic) as TextView
        gridItemString.text = stringList.get(position)

        val gridItemImage= view.findViewById<ImageView>(R.id.skill_icons) as ImageView
        gridItemImage.setImageResource(images.get(position))

        return view
    }
}
