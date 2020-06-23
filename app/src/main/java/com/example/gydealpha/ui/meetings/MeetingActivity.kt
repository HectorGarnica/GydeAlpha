package com.example.gydealpha.ui.meetings

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.gydealpha.R
import com.example.gydealpha.model.MeetingModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_meeting.*
import java.util.*

class MeetingActivity : AppCompatActivity() {

    var mentorName = ""
    var menteeName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meeting)

        back_to_meeting_button.setOnClickListener {
            onBackPressed()
        }

        val mentorId = intent.getStringExtra("extra_mentorId")
        val menteeId = intent.getStringExtra("extra_menteeId")
        val loadingPrevious = intent.getBooleanExtra("extra_loadingAMeeting",false)




        if(loadingPrevious){//load a previous

            newMeetingToolbarTextView.text= "Meeting"

            val meetingExtra = intent.getSerializableExtra("extra_meeting") as? MeetingModel

            if (meetingExtra != null) {
                MeetingMentorName.text = meetingExtra.meetingMentorName
                MeetingMenteeName.text = meetingExtra.meetingMenteeName
                meetingDateText.text = meetingExtra.meetingDate
                meetingNotesView.setText(meetingExtra.meetingNotes)
            }
            meeting_save.setText("Finish Meeting")





        }else{

            val c = Calendar.getInstance()
            val year =c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            pickDateButton.setOnClickListener {

                val dpd = DatePickerDialog(this,DatePickerDialog.OnDateSetListener{ view, mYear,mMonth,mDay ->
                meetingDateText.setText(""+ mDay +"/"+ mMonth+ "/"+ mYear)
                },year,month,day)
                dpd.show()

            }
            val db = Firebase.firestore
            val meetingsCollectionRef = db.collection("meetings")
            val mentorRef = db.collection("users").document("/$mentorId")
            val menteeRef = db.collection("users").document("/$menteeId")

            mentorRef.get().addOnSuccessListener {

                mentorName  = it.get("fname").toString() + it.get("lname").toString()

                menteeRef.get().addOnSuccessListener {menteeDoc ->
                    menteeName = menteeDoc.get("fname").toString() + menteeDoc.get("lname").toString()
                    MeetingMentorName.text = mentorName
                    MeetingMenteeName.text = menteeName

                    meeting_save.setOnClickListener {

                        if(meetingDateText.text == "No Date Set"){
                            Toast.makeText(this, "Please enter Date field",Toast.LENGTH_LONG).show()
                        }else {

                            //create new meeting doc
                            meetingsCollectionRef.get().addOnSuccessListener {

                                //creates chatroom document when mentee sends first message
                                val meetingsObj = hashMapOf<String, Any>(
                                    "meetingMentorId" to mentorId,
                                    "meetingMenteeId" to menteeId,
                                    "meetingDate" to meetingDateText.text,
                                    "meetingNotes" to meetingNotesView.text.toString(),
                                    "meetingMentorName" to mentorName,
                                    "meetingMenteeName" to menteeName
                                )

                                val newMeeting = meetingsCollectionRef.document()

                                newMeeting.set(meetingsObj)
                                    .addOnSuccessListener {
                                        println("meeting document created")
                                        onBackPressed()
                                    }
                            }
                        }

                    }


                }
            }


        }




    }


}
