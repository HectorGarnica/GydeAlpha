package com.example.gydealpha.ui.profile

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.gydealpha.MainActivity
import com.example.gydealpha.R
import com.example.gydealpha.glide.GlideApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.IOException
import java.net.URL


class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel

    val auth = FirebaseAuth.getInstance()
    val user_id = auth.currentUser?.uid
    val storage = Firebase.storage
    val db = Firebase.firestore
    val docRef = db.collection("users").document("/$user_id")
    val source = Source.CACHE
    var seekingSkillArray = arrayListOf<String>()
    var expertiseSkillArray = arrayListOf<String>()
    lateinit var bio :String



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileViewModel =
            ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()


        docRef.get().addOnSuccessListener {


            val path : String? = it.getString("userImageUrl")
            Picasso.get().load(path).fit().centerCrop().into(profileFragment_profileImage)

            var value = it.getString("fname")+ " " +it.getString("lname")
            profileFragment_name_TextView.text= value

            bio = it.getString("bio").toString() // null= did not get anything
            println(bio)
            if(bio!="null"){ // did not get anything
                profile_bio_Textview.text = bio

            }else
                profile_bio_Textview.text = "Needs to update bio"



            seekingSkillArray = it.get("seeking") as ArrayList<String> //will crash if does not exist

            var stringResult:String =""
            for(item:String in seekingSkillArray){
                stringResult += item +"\n"
            }
            profile_seeking_list_textView.text=stringResult


            val isAMentor :Boolean= it.get("isAMentor") as Boolean
            if (isAMentor){
                //set constrainlayout to visible
                profile_mentor_expertise_constraintLayout.visibility= View.VISIBLE
                expertiseSkillArray = it.get("expertise") as ArrayList<String>

                var expertiseStringResult =""
                for(item:String in expertiseSkillArray){
                    expertiseStringResult += item +"\n"
                }
                profile_expertise_list_textView.text=expertiseStringResult

            }

            profile_edit_imageView.setOnClickListener {
                profile_bio_EditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
                profile_bio_EditText.setRawInputType(InputType.TYPE_CLASS_TEXT);
                profile_bio_EditText.visibility = View.VISIBLE
                profile_bio_Textview.visibility = View.INVISIBLE
                profile_save_button.visibility = View.VISIBLE
                profile_edit_imageView.visibility = View.INVISIBLE
                if(bio!="null"){
                    profile_bio_EditText.setText(bio)
                }

            }
            profile_save_button.setOnClickListener {


                profile_bio_EditText.visibility = View.INVISIBLE
                profile_save_button.visibility= View.INVISIBLE
                profile_bio_Textview.visibility = View.VISIBLE
                profile_edit_imageView.visibility =View.VISIBLE

                val oldBio = bio
                val editTextBio = profile_bio_EditText.text.toString()
                if(oldBio!= editTextBio){
                    val newbio= hashMapOf<String,Any>("bio" to editTextBio)
                    docRef.set(newbio, SetOptions.merge())
                    profile_bio_Textview.text =editTextBio

                }
                closeKeyboard()



            }



        }

        }

    private fun closeKeyboard() {

        val activity = activity as MainActivity
        val view = activity.currentFocus

        if (view!=null){
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken,0)
        }
    }

}