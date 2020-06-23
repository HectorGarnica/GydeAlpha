package com.example.gydealpha.ui.meetings

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.gydealpha.R
import com.example.gydealpha.model.MeetingModel
import kotlinx.android.synthetic.main.meetings_list_item.view.*
import java.io.Serializable



class MeetingListAdapter(private val meetingModels : List<MeetingModel>,val userId:String)
    : RecyclerView.Adapter<MeetingListAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =  LayoutInflater.from(parent.context)
            .inflate(R.layout.meetings_list_item,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return meetingModels.size
    }


    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val otherPersonTextView = itemView.meetingWithPersonTextView
        val dateTextView = itemView.meetingListDate
        val startMeetingButton =itemView.startMeeting
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val child = meetingModels[position]

        if (userId == child.meetingMentorId){
            holder.otherPersonTextView.text = "Meeting with  " + child.meetingMenteeName
        }else{
            holder.otherPersonTextView.text = "Meeting with  " + child.meetingMentorName
        }

        holder.dateTextView.text = child.meetingDate

        holder.startMeetingButton.setOnClickListener {

            val intent = Intent(context,MeetingActivity::class.java)
            intent.putExtra("extra_loadingAMeeting",true)
            intent.putExtra("extra_meeting", child as Serializable)
            startActivity(context,intent,null)

        }
    }

}

