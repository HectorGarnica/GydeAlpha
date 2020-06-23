package com.example.gydealpha.ui.meetings

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.gydealpha.R
import com.example.gydealpha.model.MeetingModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.add_meeting_dialog.*
import kotlinx.android.synthetic.main.fragment_meetings.*

class MeetingsFragment : Fragment() {

    private lateinit var meetingsViewModel: MeetingsViewModel
    val auth = FirebaseAuth.getInstance()
    val user_id = auth.currentUser?.uid
    val db = Firebase.firestore


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        meetingsViewModel =
            ViewModelProviders.of(this).get(MeetingsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_meetings, container, false)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        meetings_rv.addItemDecoration(
            DividerItemDecoration(
                requireContext(), LinearLayoutManager.VERTICAL
            )
        )
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        add_meeting_iv.setOnClickListener {
            //showDialog()
        }
    }


    override fun onResume() {
        super.onResume()

        var listOfMeetings  =  arrayListOf<MeetingModel>()
        var myAdapter = user_id?.let { MeetingListAdapter(listOfMeetings, it) }

        meetings_rv.apply {
            layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL,false)
            adapter = myAdapter
        }

        val meetingCollectionRef = db.collection("meetings")

        //find chatroomDocuments where both mentorId AND mentorId are present
        val imAMenteeQuery = meetingCollectionRef
            .whereEqualTo("meetingMenteeId", user_id)

        imAMenteeQuery.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                //loops through all documents matching query filter(should be several)
                for (meetingDocument: QueryDocumentSnapshot in task.result!!) {
                    val meetingObj: MeetingModel = meetingDocument.toObject(MeetingModel::class.java)
                    listOfMeetings.add(meetingObj)
                }
                if (myAdapter != null) {
                    myAdapter.notifyDataSetChanged()
                }
            }
        }

        val imAMentorQuery = meetingCollectionRef
            .whereEqualTo("meetingMentorId", user_id)

        imAMentorQuery.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                //loops through all documents matching query filter(should be several)
                for (meetingDocument: QueryDocumentSnapshot in task.result!!) {
                    val meetingObj: MeetingModel = meetingDocument.toObject(MeetingModel::class.java)
                    listOfMeetings.add(meetingObj)
                }
                if (myAdapter != null) {
                    myAdapter.notifyDataSetChanged()
                }
            }
        }












    }



}
