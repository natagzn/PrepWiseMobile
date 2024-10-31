package com.example.prepwise.models

import java.io.Serializable
import java.time.LocalDateTime

class HelpAnswer(val id: Int, var content: String, var username: String, var dateTime: LocalDateTime):
    Serializable {
}