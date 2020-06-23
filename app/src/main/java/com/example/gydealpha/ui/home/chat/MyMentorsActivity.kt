package com.example.gydealpha.ui.home.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gydealpha.R
import com.example.gydealpha.model.ChatRoom
import com.example.gydealpha.util.MyPreference
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_my_mentors.*

class MyMentorsActivity : AppCompatActivity() {

    val auth = FirebaseAuth.getInstance()
    val user_id = auth.currentUser?.uid
    val db = Firebase.firestore
    val chatRoomsCollectionRef = db.collection("chatRooms")
    val docRef = db.collection("users").document("/$user_id")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_mentors)

        back_button_to_home.setOnClickListener {
            onBackPressed()
        }
        if (user_id != null) {

            var listOfChatRooms  =  arrayListOf<ChatRoom>()
            var myAdapter = MyMentorsListAdapter(listOfChatRooms,true)
            myMentorRecyler.apply {
                layoutManager = LinearLayoutManager(this@MyMentorsActivity,
                    RecyclerView.VERTICAL,false)
                adapter = myAdapter
            }


            //find chatroomDocuments where both mentorId AND mentorId are present
            val mentorsQuery = chatRoomsCollectionRef
                .whereEqualTo("menteeId", user_id)

            //once query is complete: regardless if no documents are returned
            mentorsQuery.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    //loops through all documents matching query filter(should be several)
                    for (chatRoomDocument: QueryDocumentSnapshot in task.result!!) {
                        val doc_id = chatRoomDocument.id
                        val docRef = db.collection("chatRooms").document("/$doc_id")
                        val chatRoomObj: ChatRoom = chatRoomDocument.toObject(ChatRoom::class.java)
                        listOfChatRooms.add(chatRoomObj)
                    }
                    myAdapter.notifyDataSetChanged()
                }
            }.addOnFailureListener { exception ->
                println("couldnt read message document")
            }

        }
    }
}
