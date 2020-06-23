package com.example.gydealpha.ui.home.chat

import android.content.Intent
import android.transition.ChangeTransform
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.gydealpha.R
import com.example.gydealpha.model.ChatRoom
import com.example.gydealpha.model.UserModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_my_mentors.view.*
import java.io.Serializable


class MyMentorsListAdapter(private val childModels : List<ChatRoom>,val imAMentee:Boolean)
    : RecyclerView.Adapter<MyMentorsListAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =  LayoutInflater.from(parent.context)
            .inflate(R.layout.item_my_mentors,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return childModels.size
    }


    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val nameTextView = itemView.myMentorNamextView
        val lastMessageTextview = itemView.myMentors_lastMessage_textView
        val profileImage = itemView.myMentors_ImageView
        val parentLayout = itemView.item_myMentors_parentLayout

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val child = childModels[position]
        var otherUserId:String
        if(imAMentee){
            holder.nameTextView.text = child.mentorName
            holder.lastMessageTextview.text = child.recentMessage
            val path : String? = child.mentorImageUrl
            Picasso.get().load(path).fit().centerCrop().into(holder.profileImage)
            otherUserId= child.mentorId

        }
        else{
            holder.nameTextView.text = child.menteeName
            holder.lastMessageTextview.text = child.recentMessage
            val path : String? = child.menteeImageUrl
            Picasso.get().load(path).fit().centerCrop().into(holder.profileImage)
            otherUserId = child.menteeId
        }
        holder.parentLayout.setOnClickListener {
            val db = Firebase.firestore
            val docRef = db.collection("users").document("/$otherUserId")

            docRef.get().addOnSuccessListener {
                val userModelInDoc: UserModel? = it.toObject(UserModel::class.java)
                val intent = Intent(context,ChatActivity::class.java)
                intent.putExtra("extra_user", userModelInDoc as Serializable)
                intent.putExtra("extra_imAMentee",imAMentee)
                startActivity(context,intent,null)
            }







        }



    }

}

