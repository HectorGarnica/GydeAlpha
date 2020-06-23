package com.example.gydealpha.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.gydealpha.R
import com.example.gydealpha.model.ChatRoom
import com.example.gydealpha.ui.home.chat.MyMentorsListAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_be_a_mentor.*

/**
 * A simple [Fragment] subclass.
 */
class BeAMentorFragment : Fragment() {


    companion object {
        private val ARG_CAUGHT = "BeAMentorFragment_caught"

        fun newInstance():BeAMentorFragment{
            val fragment = BeAMentorFragment()
            return fragment
        }
    }


    val auth = FirebaseAuth.getInstance()
    val user_id = auth.currentUser?.uid
    val db = Firebase.firestore
    val chatRoomsCollectionRef = db.collection("chatRooms")
    val docRef = db.collection("users").document("/$user_id")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_be_a_mentor, container, false)
    }

    override fun onStart() {
        super.onStart()

        if (user_id != null) {

            var listOfChatRooms  =  arrayListOf<ChatRoom>()
            var myAdapter = MyMentorsListAdapter(listOfChatRooms,false)
            beAMentorRecyler.apply {
                layoutManager = LinearLayoutManager(requireActivity(),
                    RecyclerView.VERTICAL,false)
                adapter = myAdapter
            }

            var documentExist = false
            //find chatroomDocuments where both mentorId AND mentorId are present
            val mentorsQuery = chatRoomsCollectionRef
                .whereEqualTo("mentorId", user_id)

            //once query is complete: regardless if no documents are returned
            mentorsQuery.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //loops through all documents matching query filter(should be several)
                    for (chatRoomDocument: QueryDocumentSnapshot in task.result!!) {
                        val doc_id = chatRoomDocument.id
                        val docRef = db.collection("chatRooms").document("/$doc_id")
                        val chatRoomObj: ChatRoom = chatRoomDocument.toObject(ChatRoom::class.java)
                        listOfChatRooms.add(chatRoomObj)
                        documentExist = true
                    }
                    myAdapter.notifyDataSetChanged()

                    if(!documentExist){
                        notAMentorButton.visibility = View.VISIBLE
                        notAMentorTextView.visibility= View.VISIBLE
                    }

                }
            }.addOnFailureListener { exception ->
                println("couldnt read message document")
            }

        }




    }



}
