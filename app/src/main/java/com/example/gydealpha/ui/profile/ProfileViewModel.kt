package com.example.gydealpha.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is profile Fragment"
    }
    val text: LiveData<String> = _text

    val auth = FirebaseAuth.getInstance()
    val user_id = auth.currentUser?.uid
    val storage = Firebase.storage
    val db = Firebase.firestore
    val docRef = db.collection("users").document("/$user_id")
    val source = Source.CACHE
    private val _firstname = MutableLiveData<String>().apply {

        value = "Hectoroni"
        //value = docRef.get(source).result?.getString("fname")
    }

    val fname:LiveData<String> = _firstname



}