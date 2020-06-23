package com.example.gydealpha.profileBuilder

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.core.net.toFile
import androidx.lifecycle.lifecycleScope
import androidx.fragment.app.Fragment
import com.example.gydealpha.MainActivity
import com.example.gydealpha.R
import com.example.gydealpha.util.StorageUtil
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import id.zelory.compressor.Compressor
import id.zelory.compressor.loadBitmap
import kotlinx.android.synthetic.main.fragment_profile_picture_prompt.*
import kotlinx.coroutines.launch
import org.jetbrains.anko.support.v4.startActivity
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class ProfilePicturePrompt : Fragment() {

    lateinit var fname: String
    lateinit var lname: String
    lateinit var institution: String
    lateinit var major : String
    lateinit var mentorRequirements : String
    lateinit var personalExpertise: String
    var isAMentor by Delegates.notNull<Boolean>()

    private var actualImage: File? = null
    var compressedImage: File? = null
    var selectedPhotoUri: Uri? = null
    var mentorSkillArray = arrayListOf<String>()
    var personalSkillArray = arrayListOf<String>()


    //inflates view
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_picture_prompt, container, false)
    }

    //sets button listeners
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profile_picture_name.text = fname +" "+lname

        button6.setOnClickListener {
            createUser()
        }
        profile_builder_profile_imageView.setOnClickListener {
                chooseImage()
        }

    }


    /////////////////////////////////////////
    //Setting the image view from gallery///
    ///////////////////////////////////////
    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }
    private fun chooseImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data == null) {
                showError("Failed to open picture!")
                return
            }

                selectedPhotoUri = data.data

                actualImage = FileUtil.from(requireActivity(), data.data)?.also {
                    profile_builder_profile_imageView.setImageBitmap(loadBitmap(it))
                    //val bitmap : Bitmap = loadBitmap(it)
                    compressImage()
                }


        }
    }
    private fun compressImage() {
        actualImage?.let { imageFile ->
            lifecycleScope.launch {
                // Default compression
                compressedImage = Compressor.compress(requireActivity(), imageFile)
                setCompressedImage()
            }
        } ?: showError("Please choose an image!")
    }

    private fun setCompressedImage() {
        compressedImage?.let {
            profile_builder_profile_imageView.setImageBitmap(BitmapFactory.decodeFile(it.absolutePath))
            Log.i("goku", it.toString())
        }

    }

    private fun showError(errorMessage: String) {
        Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT).show()
    }




    //////////////////////
    //create firestore user//
    ////////////////////////
    fun createUser() {
        val auth = FirebaseAuth.getInstance()
        val user_id = auth.currentUser?.uid
        val storage = Firebase.storage
        val storageRef = storage.reference
        val db = Firebase.firestore


        if (selectedPhotoUri!= null&& user_id!=null){

            val imageStorage = storageRef.child("users/$user_id")
            val uploadTask = imageStorage!!.putFile(selectedPhotoUri!!)
            val task = uploadTask.continueWithTask{
                task ->
                if (!task.isSuccessful){
                    Toast.makeText(requireActivity(),"Failed",Toast.LENGTH_LONG).show()
                }
                imageStorage!!.downloadUrl
            }.addOnCompleteListener {task ->
                if (task.isSuccessful){
                    val downloadUri = task.result
                    val url= downloadUri!!.toString().substring(0,downloadUri.toString().indexOf("&token"))
                    println("url equals"+ url)
                    Log.d("Direct",url)

                    //creates user document with userId as document name
                    val user = hashMapOf<String,Any>(
                        "userId" to user_id,
                        "fname" to fname,
                        "lname" to lname,
                        "institution" to institution,
                        "major" to major,
                        "bio" to "",
                        "seeking" to mentorSkillArray,
                        "expertise" to personalSkillArray,  //might not be neccecary
                        "userImageUrl" to url,
                        "isAMentor" to isAMentor,
                        "lastActive" to FieldValue.serverTimestamp()
                    )

                    db.collection("users").document(user_id).set(user)
                        .addOnSuccessListener {
                            println("document created")
                            Log.i("goku","Document created" )
                            startActivity<MainActivity>()
                            activity?.finish()
                        }

                        .addOnFailureListener {
                            println("Failed to create document")
                            Log.i("goku", "Document NOT created")
                        }




                }
            }








        }

    }






    //intantiates global variables from bundle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fname = arguments!!.getString("firstNameArg").toString()
        lname = arguments!!.getString("lastNameArg").toString()
        institution = arguments!!.getString("institutionArg").toString()
        major = arguments!!.getString("major1Arg").toString()
        mentorRequirements =arguments!!.getString("mentorSkillArg").toString()
        personalExpertise =arguments!!.getString("personalSkillArg").toString()
        mentorSkillArray = mentorRequirements.split(",") as ArrayList<String>
        mentorSkillArray.removeAt(mentorSkillArray.size - 1)
        if(personalExpertise !="") {
            personalSkillArray = personalExpertise.split(",") as ArrayList<String>
            personalSkillArray.removeAt(personalSkillArray.size - 1)
            isAMentor = true
        }
        else {
            personalSkillArray = arrayListOf<String>("None")
            isAMentor = false
        }
    }



}

