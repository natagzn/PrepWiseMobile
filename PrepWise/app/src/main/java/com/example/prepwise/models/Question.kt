package com.example.prepwise.models

import java.io.Serializable

class Question(val question_id:Int, var content: String, var answer: String, var  learned: Boolean): Serializable {
}