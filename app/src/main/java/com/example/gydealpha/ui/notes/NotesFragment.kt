package com.example.gydealpha.ui.notes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gydealpha.R
import com.example.gydealpha.model.NoteModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_notes.*
import org.jetbrains.anko.support.v4.startActivity

class NotesFragment : Fragment() {


    val auth = FirebaseAuth.getInstance()
    val user_id = auth.currentUser?.uid
    val db = Firebase.firestore


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_notes, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        notes_rv.addItemDecoration(
            DividerItemDecoration(
                requireContext(),LinearLayoutManager.VERTICAL
            )
        )
    }


    override fun onResume() {
        super.onResume()

        var listOfNotes  =  arrayListOf<NoteModel>()
        var myAdapter = NoteListAdapter(listOfNotes)

        notes_rv.apply {
            layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL,false)
            adapter = myAdapter
        }

        val docRef = db.collection("users").document("/$user_id")
        val notesCollection = docRef.collection("notes")
        //load chatRooms subcollection
        notesCollection.get().addOnSuccessListener { documents ->
            for (document in documents) {
                println("entered the collection")
                val noteDoc: NoteModel = document.toObject(NoteModel::class.java)
                listOfNotes.add(noteDoc)
            }
            myAdapter.notifyDataSetChanged()
        }.addOnFailureListener { exception ->
            println("couldnt read note document")
        }


    }

    override fun onStart() {
        super.onStart()

        notes_add_imageView.setOnClickListener {
            val intent = Intent(requireActivity(),NotesActivity::class.java)
            intent.putExtra("extra_note_exists", false)
            startActivity(intent,null)
        }



    }



}



