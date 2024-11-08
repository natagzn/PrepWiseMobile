package com.example.prepwise.models

import java.io.Serializable
import java.time.LocalDate

class Resource (var id: Int, var articleBook: String, var description: String,
                var level: Level, var category: Category, var date: LocalDate, var username: String,
                var isLiked: Boolean, var isDisLiked: Boolean,
                var numberOfLikes: Int = 0,
                var numberOfDislikes: Int = 0, var isAuthor: Boolean): Serializable {
}