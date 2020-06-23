package com.example.gydealpha.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

object FirestoreUtil {
    private val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance()}
        private val currentUserDocRef: DocumentReference
            get()= firestoreInstance.document("users/${FirebaseAuth.getInstance().uid
                ?: throw NullPointerException("UID is null.")}")




        fun updateCurrentUser(
            fname: String = "",
            lname: String = "",
            institution: String ="",
            major: String = "",
            bio: String? = null,
            seeking:ArrayList<String> = arrayListOf<String>(),
            personalSkills: ArrayList<String>? = null,
            isAMentor: Boolean? = null,
            profilePicturePath: String? =null){

            val userFieldMap = mutableMapOf<String,Any>()
            if (fname.isNotBlank()) userFieldMap["fname"]  = fname
            if (lname.isNotBlank()) userFieldMap["lname"]  = lname
            if (institution.isNotBlank()) userFieldMap["institution"]  = institution
            if (major.isNotBlank()) userFieldMap["major"] = major
            if (bio!=null) userFieldMap["bio"]  = bio
            if (seeking.isNotEmpty()) userFieldMap["seeking"] = seeking
            if (personalSkills!=null) userFieldMap["expertise"] = personalSkills
            if(isAMentor!=null) userFieldMap["isAMentor"] = isAMentor
            if (profilePicturePath!=null) userFieldMap["userImageUrl"] = profilePicturePath

            currentUserDocRef.update(userFieldMap)

        }
    fun getCurrentUser()= currentUserDocRef
    //getcurrentuser 9:06
    }




