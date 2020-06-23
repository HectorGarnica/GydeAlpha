package com.example.gydealpha.ui.notes

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.gydealpha.R
import com.example.gydealpha.model.NoteModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.notes_list_item.view.*
import java.io.Serializable


class NoteListAdapter(private val noteModels : List<NoteModel>)
    : RecyclerView.Adapter<NoteListAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =  LayoutInflater.from(parent.context)
            .inflate(R.layout.notes_list_item,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return noteModels.size
    }


    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val titleTextView = itemView.notes_list_item_title
        val descriptionTextView = itemView.notes_list_item_subtext
        val dateTextView = itemView.notes_list_item_date_textView
        val item =itemView.note_list_root
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val child = noteModels[position]


        //holder.imageImageView.setImageResource(R.drawable.ic_profile_black_24dp)
        holder.titleTextView.text = child.noteTitle
        holder.dateTextView.text = "5/1/2020"
        holder.descriptionTextView.text =child.noteDescription
        holder.item.setOnClickListener {

            val intent = Intent(context,NotesActivity::class.java)
            intent.putExtra("extra_note_exists",true)
            intent.putExtra("extra_note", child as Serializable)
            startActivity(context,intent,null)

        }
    }

}

