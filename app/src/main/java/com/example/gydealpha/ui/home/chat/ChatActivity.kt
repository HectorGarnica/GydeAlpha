package com.example.gydealpha.ui.home.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gydealpha.R
import com.example.gydealpha.model.TextMessage
import com.example.gydealpha.model.UserModel
import com.example.gydealpha.ui.meetings.MeetingActivity
import com.example.gydealpha.util.MyPreference
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_chat.*
import java.io.Serializable
import java.util.*


//this class takes in parameters of User(other user), and a boolean isAMentee: depends on where activity is called
class ChatActivity : AppCompatActivity() {

    val auth = FirebaseAuth.getInstance()
    val user_id = auth.currentUser?.uid
    lateinit var menteeId:String
    lateinit var mentorId:String
    lateinit var menteeName:String
    lateinit var mentorName:String
    val db = Firebase.firestore
    val chatRoomsCollectionRef = db.collection("chatRooms")
    val docRef = db.collection("users").document("/$user_id")
    var newlyCreated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        //handles Back button navigation
        back_to_mentor_or_messages.setOnClickListener {
            onBackPressed()
        }

        //gets intent information from past activity/fragment
        val user = intent.getSerializableExtra("extra_user") as? UserModel
        val iAmAMentee = intent.getBooleanExtra("extra_imAMentee",true)

        //if both users have IDs
        if (user != null&& user_id != null) {

            var  doc_id = user_id

            //set title of chat to other user's name
            titleChat_textView.text = user.fname + " " + user.lname

            var listOfMessages  =  arrayListOf<TextMessage>()
            var myAdapter = MessagesListAdapter(listOfMessages,user_id)
            messages.apply {
                    layoutManager = LinearLayoutManager(this@ChatActivity,RecyclerView.VERTICAL,false)
                    adapter = myAdapter
            }


            //who is the mentor and mentee
            //get myName
            val mypreference = MyPreference(this)
            var myUserName = mypreference.getUserName()
            //sets identifiers to search for chatroom

            if (iAmAMentee&&myUserName!=null) {
                menteeId = user_id //(me)
                mentorId = user.userId
                mentorName = user.fname+" "+user.lname
                menteeName = myUserName
            } else {
                menteeId = user.userId
                mentorId = user_id
                menteeName =  user.fname+" "+user.lname
                if (myUserName != null) {
                    mentorName= myUserName
                }
            }

            //find chatroomDocuments where both mentorId AND mentorId are present
            val mentorsQuery = chatRoomsCollectionRef
                .whereEqualTo("menteeId", menteeId)
                .whereEqualTo("mentorId", mentorId)

            //once query is complete: regardless if no documents are returned
            mentorsQuery.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    println("query succsful")
                    var documentFound = false
                    //loops through all documents matching query filter(should only be one)
                    for (chatRoomDocument: QueryDocumentSnapshot in task.result!!) {
                        doc_id = chatRoomDocument.id
                        val docRef = db.collection("chatRooms").document("/$doc_id")
                        documentFound = true
                        val messagesCollection = docRef.collection("messages")
                        //load chatRooms subcollection
                        messagesCollection.get().addOnSuccessListener { documents ->
                            for (document in documents) {
                                println("entered the collection")
                                val textMessageDoc: TextMessage = document.toObject(TextMessage::class.java)
                                println(textMessageDoc.textText)
                                listOfMessages.add(textMessageDoc)
                            }
                            myAdapter.notifyDataSetChanged()
                        }.addOnFailureListener { exception ->
                                println("couldnt read message document")
                            }
                    }

                    ///////////////////////////////////////////////////
                    //No Chatroom currently exist for these two users// //sets send button behavior
                    //////////////////////////////////////////////////
                    if (documentFound == false) {

                        add_event_from_chatActivity.visibility= View.GONE
                        val uuid= UUID.randomUUID().toString()
                        send_message.setOnClickListener {
                            if (!TextUtils.isEmpty(enter_message.text)) {
                                if (!newlyCreated) {//prevents from this click listenr to create another chat room if clicked again
                                    //check if edit text is not blank
                                    docRef.get().addOnSuccessListener {

                                        var imageUrl = it.get("userImageUrl").toString()

                                        //creates chatroom document when mentee sends first message
                                        val chatRoomObject = hashMapOf<String, Any>(
                                            "mentorId" to mentorId,
                                            "menteeId" to menteeId,
                                            "mentorName" to mentorName,
                                            "menteeName" to menteeName,
                                            "recentMessage" to enter_message.text.toString(),
                                            "sentByMentor" to false,
                                            "seenByRecipient" to false,
                                            "mentorImageUrl" to user.userImageUrl!!,
                                            "menteeImageUrl" to imageUrl
                                        )


                                        val newChatRoom = chatRoomsCollectionRef.document(uuid)

                                        newChatRoom.set(chatRoomObject)
                                            .addOnSuccessListener {
                                                println("chat room document created")
                                                //dont allow to enter this Onclick listener
                                                newlyCreated = true

                                                //chat rooecm created, now to add the current message to the subcollection
                                                val textMessage = hashMapOf<String, Any>(
                                                    "textText" to enter_message.text.toString(),
                                                    "authorId" to user_id,
                                                    "timestamp" to FieldValue.serverTimestamp(),
                                                    "date" to Calendar.getInstance().time
                                                )
                                                val newMessage =
                                                    newChatRoom.collection("messages").document()

                                                //add textmessage to messageList( temporary, until we can have an update listener)
                                                newMessage.set(textMessage)
                                                listOfMessages.add(
                                                    TextMessage(
                                                        enter_message.text.toString(),
                                                        user_id,
                                                        Calendar.getInstance().time
                                                    )
                                                )
                                                myAdapter.notifyDataSetChanged()

                                            }.addOnFailureListener {
                                                println("Failed to create chat room document")
                                            }
                                    }

                                } //end of "if newly created
                                else{//if chatroom is newly created but i need to send a message
                                    //sends message when newly created,but havent read chatroom info
                                    docRef.get().addOnSuccessListener {

                                        val newChatRoom = chatRoomsCollectionRef.document(uuid)

                                        //chat rooecm created, now to add the current message to the subcollection
                                        val textMessage = hashMapOf<String, Any>(
                                            "textText" to enter_message.text.toString(),
                                            "authorId" to user_id,
                                            "timestamp" to FieldValue.serverTimestamp(),
                                            "date" to Calendar.getInstance().time
                                        )
                                        val newMessage = newChatRoom.collection("messages").document()

                                        //add textmessage to messageList( temporary, until we can have an update listener)
                                        newMessage.set(textMessage)
                                        listOfMessages.add(TextMessage(enter_message.text.toString(), user_id, Calendar.getInstance().time))
                                        myAdapter.notifyDataSetChanged()

                                    }

                                }
                            }//ennd of edit text is empty
                            }//end of send button listener

                    } else{ //if document was found set up behavior for send button

                        add_event_from_chatActivity.visibility = View.VISIBLE

                        add_event_from_chatActivity.setOnClickListener {
                            //pass mentor and mentee
                            val intent = Intent(this,MeetingActivity::class.java)
                            intent.putExtra("extra_mentorId", mentorId)
                            intent.putExtra("extra_menteeId",menteeId)
                            startActivity(intent,null)

                        }

                        send_message.setOnClickListener {
                            if (!TextUtils.isEmpty(enter_message.text)) {


                                val curChatRoom = chatRoomsCollectionRef.document("/$doc_id")

                                //chat rooecm created, now to add the current message to the subcollection
                                val textMessage = hashMapOf<String, Any>(
                                    "textText" to enter_message.text.toString(),
                                    "authorId" to user_id,
                                    "timestamp" to FieldValue.serverTimestamp(),
                                    "date" to Calendar.getInstance().time
                                )
                                val newMessage =
                                    curChatRoom.collection("messages").document()

                                //add textmessage to messageList( temporary, until we can have an update listener)
                                newMessage.set(textMessage)
                                listOfMessages.add(
                                    TextMessage(
                                        enter_message.text.toString(),
                                        user_id,
                                        Calendar.getInstance().time
                                    )
                                )
                                myAdapter.notifyDataSetChanged()
                                enter_message.setText("")

                            }
                        }


                    }
                }
            }//end of query task
        }
    }//end of onCreate

}

