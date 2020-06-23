package com.example.gydealpha.model

import java.io.Serializable

data class MeetingModel(
    val meetingMentorId:String = "",
    val meetingMenteeId:String = "",
    val meetingDate:String="",
    val meetingNotes: String ="",
    val meetingMentorName: String= "",
    val meetingMenteeName:String = ""

) : Serializable

