package com.example.gydealpha.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gydealpha.R
import com.example.gydealpha.model.ParentModelFindMentor
import kotlinx.android.synthetic.main.mentor_row_item.view.*

class ParentAdapterFindMentor(private val parents : ArrayList<ParentModelFindMentor>) : RecyclerView.Adapter<ParentAdapterFindMentor.ViewHolder>(){

    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.mentor_row_item,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return parents.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val parent = parents[position]
        holder.textView.text = parent.titleSubject
        holder.recyclerView.apply {
            layoutManager = LinearLayoutManager(holder.recyclerView.context, RecyclerView.HORIZONTAL, false)
            adapter = ChildAdapterFindMentor(parent.childrenMentors)
            setRecycledViewPool(RecyclerView.RecycledViewPool())
        }
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val recyclerView : RecyclerView = itemView.rv_child_findMentor
        val textView: TextView = itemView.mentor_row_subject_textView

    }

}