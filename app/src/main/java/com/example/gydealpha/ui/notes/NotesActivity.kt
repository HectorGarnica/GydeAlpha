package com.example.gydealpha.ui.notes

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.gydealpha.R
import com.example.gydealpha.model.NoteModel
import com.example.gydealpha.model.TextMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_notes.*
import java.util.*

class NotesActivity : AppCompatActivity() {



    val auth = FirebaseAuth.getInstance()
    val user_id = auth.currentUser?.uid
    val db = Firebase.firestore
    val docRef = db.collection("users").document("/$user_id")




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)

        val isInDatabase = intent.getBooleanExtra("extra_note_exists",true)


        if(isInDatabase) {  //if note is already in database

            delete_note_button.visibility= View.VISIBLE
            newNoteToolbarTextView.text = "Edit Note"
            //load note from database
            val note = intent.getSerializableExtra("extra_note") as? NoteModel
            if (note != null) {
                println(note.noteTitle)
            }
            //set edit text from database and load this into a variable too
            val loadedTitle:String = note!!.noteTitle
            val loadedDescription:String = note.noteDescription
            edit_text_notes_title.setText(loadedTitle)
            editTextNotes.setText(loadedDescription)

            val noteId = note.noteId

            delete_note_button.setOnClickListener {
                docRef.get().addOnSuccessListener {
                    val loadedNote = docRef.collection("notes").whereEqualTo("noteId",noteId)
                    loadedNote.get().addOnSuccessListener { documents ->
                        for(document in documents){
                            document.reference.delete().addOnSuccessListener {

                                onBackPressed()
                            }
                        }
                    }
                }
            }


            back_to_notes_button.setOnClickListener { //note exist in database back button behavior
                //if edit text remains unchanged
                if((loadedTitle==edit_text_notes_title.text.toString()) || (loadedDescription==editTextNotes.text.toString()))
                    onBackPressed()
                else{
                    docRef.get().addOnSuccessListener {
                        val loadedNote = docRef.collection("notes").whereEqualTo("noteId",noteId)
                        loadedNote.get().addOnSuccessListener { documents ->
                            for (document in documents){
                                document.reference.update("noteTitle",edit_text_notes_title.text.toString())
                                document.reference.update("noteDescription", editTextNotes.text.toString())
                                onBackPressed()
                            }
                        }
                    }
                }
            }


        }
        else{   //note is not in database

            //focusAndShowKeyboard
            edit_text_notes_title.requestFocus()
            showKeyboard(this)

            //do not load database
            back_to_notes_button.setOnClickListener { //note does not exist in database

                //check if Edittext is empty
                val editTextDescription = editTextNotes.text.toString()
                val editTextStringTitle = edit_text_notes_title.text.toString()

                if(editTextDescription.trim().length>0){ //editText has context



                    docRef.get().addOnSuccessListener {

                        //creates chatroom document when mentee sends first message
                        val noteObject = hashMapOf<String, Any>(
                            "noteId" to UUID.randomUUID().toString(),
                            "noteTitle" to editTextStringTitle,
                            "noteDescription" to editTextDescription,
                            "noteDate" to Calendar.getInstance().time,
                            "noteTimeStamp" to FieldValue.serverTimestamp()
                        )

                        val newNote = docRef.collection("notes").document()

                        newNote.set(noteObject)
                            .addOnSuccessListener {
                                hideKeyboard(this,editTextNotes.windowToken)
                                onBackPressed()
                                println("Note document created")
                            }.addOnFailureListener {
                                println("Failed to create chat room document")
                            }
                    }

                }//else Edit text empty - > on backpressed->no note is created
                else{
                    hideKeyboard(this,editTextNotes.windowToken)
                    onBackPressed()
                }

            }

        }//end of (not in database)




    }


    fun showKeyboard(activity: Activity?) {
        if (activity == null) return
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    // private methods
    private fun hideKeyboard(activity: Activity?, windowToken: IBinder?) {
        if (activity == null) return
        val inputManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }






}
