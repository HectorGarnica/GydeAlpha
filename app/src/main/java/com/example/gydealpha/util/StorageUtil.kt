package com.example.gydealpha.util

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.lang.NullPointerException
import java.util.*

object StorageUtil {
    private val storageInstance: FirebaseStorage by lazy { FirebaseStorage.getInstance()}

    private val currentUserRef: StorageReference
        get() = storageInstance.reference
            .child(FirebaseAuth.getInstance().uid ?: throw NullPointerException("UID is null"))

    fun uploadProfilePicture(selectedPhotoUri: Uri?, onSuccess: (imagePath:String)-> Unit){

        val ref = currentUserRef.child("profilePictures/${UUID.randomUUID()}")
        if (selectedPhotoUri != null) {
            ref.putFile(selectedPhotoUri).addOnSuccessListener {
                onSuccess(ref.path)
            }
        }

    }

    fun pathToReference(path:String) = storageInstance.getReference(path)




}