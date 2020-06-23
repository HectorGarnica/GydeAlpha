package com.example.gydealpha.ui.home.chat

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.gydealpha.R
import com.example.gydealpha.model.TextMessage
import kotlinx.android.synthetic.main.item_sent.view.*
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.wrapContent
import java.text.SimpleDateFormat

val dateFormat = SimpleDateFormat
    .getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT)

class MessagesListAdapter(private val childModels : List<TextMessage>,val userId:String)
    : RecyclerView.Adapter<MessagesListAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =  LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sent,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return childModels.size
    }


    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val textTextView = itemView.message_text
        val dateTextview = itemView.message_time
        val rootLayout = itemView.message_root
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //val context = holder.itemView.context
        val child = childModels[position]

        holder.textTextView.text = child.textText
        holder.dateTextview.text = dateFormat.format(child.date)
        if (userId != child.authorId){// this message is from another user, layout is light
            holder.rootLayout.apply {
                backgroundResource = R.drawable.rect_round_white
                val lParams = FrameLayout.LayoutParams(wrapContent, wrapContent, Gravity.START)
                this.layoutParams = lParams
            }

        }


    }

}

