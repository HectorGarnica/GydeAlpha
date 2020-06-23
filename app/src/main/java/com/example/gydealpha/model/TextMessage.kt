package com.example.gydealpha.model

import java.io.Serializable
import java.util.*

data class TextMessage(
    val textText:String = "",
    val authorId: String ="",
    val date: Date? = null
) : Serializable
