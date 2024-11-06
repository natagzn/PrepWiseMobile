package com.example.prepwise.models

import java.io.Serializable
import java.time.LocalDate
import java.util.ArrayList

open class Set(
    val id: Int,
    var name: String,
    var level: Level,
    var categories: ArrayList<Category>,
    var access: String,
    var date: LocalDate,
    var questions: ArrayList<Question>,
    var username: String,
    var isLiked: Boolean): Serializable {

    fun calculateProgress(): Int {
        val totalQuestions = questions.size
        if (totalQuestions == 0) return 0

        val learnedQuestions = questions.count { it.learned }
        val progress = (learnedQuestions * 100) / totalQuestions

        return progress
    }

    fun getCountLearned(): Int{
        return questions.count { it.learned }
    }
}