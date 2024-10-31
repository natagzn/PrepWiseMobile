package com.example.prepwise.models

import java.io.Serializable
import java.time.LocalDate

class Folder(val id: Int, var name: String, var sets: ArrayList<Set>, var date: LocalDate, var isLiked: Boolean):Serializable {
}