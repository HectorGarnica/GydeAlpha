package com.example.gydealpha.model

import java.io.Serializable
import java.util.*

data class NoteModel (

    val noteId: String ="",
    val noteTitle: String = "",
    val noteDescription : String ="",
    val noteDate: Date? = null

    ) : Serializable

