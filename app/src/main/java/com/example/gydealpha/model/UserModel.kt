package com.example.gydealpha.model

import java.io.Serializable

class UserModel(
    val userId:String = "",
    val fname: String ="",
    val lname: String= "",
    val institution:String = "",
    val major: String= "",
    val bio:String?=null, //could be null
    val seeking: ArrayList<String> = arrayListOf(),
    val expertise: ArrayList<String>?= null,
    val isAMentor: Boolean? = false,
    val userImageUrl: String?=null
) : Serializable
