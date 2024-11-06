package com.example.prepwise.models

import androidx.core.util.Pools
import java.io.Serializable
import java.time.LocalDate

class SharedSet(
    id: Int,
    var type: String,
    var coAuthors: ArrayList<People>,
    name: String,
    level: Level,
    categories: ArrayList<Category>,
    access: String,
    date: LocalDate,
    questions: ArrayList<Question>,
    username: String,
    isLiked: Boolean
) : Set(id, name, level, categories, access, date, questions, username, isLiked), Serializable