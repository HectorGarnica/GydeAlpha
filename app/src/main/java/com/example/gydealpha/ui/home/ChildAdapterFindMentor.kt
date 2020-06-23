package com.example.gydealpha.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.gydealpha.R
import com.example.gydealpha.model.UserModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.mentor_row_item_child.view.*
import java.io.Serializable


class ChildAdapterFindMentor(private val childModels : List<UserModel>)
    : RecyclerView.Adapter<ChildAdapterFindMentor.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =  LayoutInflater.from(parent.context)
            .inflate(R.layout.mentor_row_item_child,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return childModels.size
    }


    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val nameTextView = itemView.mentor_row_item_name_textView
        val imageImageView = itemView.mentor_r_i_c_ImageView
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val child = childModels[position]

        val path : String? = child.userImageUrl
        Picasso.get().load(path).fit().centerCrop().into(holder.imageImageView)
        //holder.imageImageView.setImageResource(R.drawable.ic_profile_black_24dp)
        holder.nameTextView.text = child.fname
        holder.imageImageView.setOnClickListener {

            val intent = Intent(context,MentorProfileActivity::class.java)
            intent.putExtra("extra_user", child as Serializable)
            startActivity(context,intent,null)

        }
    }

}

