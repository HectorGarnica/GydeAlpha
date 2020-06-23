package com.example.gydealpha.model

import java.io.Serializable


data class ChatRoom(
    val menteeId:String = "",
    val mentorName:String = "",
    val menteeName:String="",
    val menteeImageUrl: String ="",
    val mentorId: String= "",
    val mentorImageUrl:String = "",
    val recentMessage: String= "",
    val seenByRecipient:Boolean=false,
    val sentByMentor: Boolean = false

) : Serializable