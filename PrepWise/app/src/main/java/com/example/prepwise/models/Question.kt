package com.example.prepwise.models

import java.io.Serializable

class Question(var content: String, var answer: String, var  learned: Boolean): Serializable {
}