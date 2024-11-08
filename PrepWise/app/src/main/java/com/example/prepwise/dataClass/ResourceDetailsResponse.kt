package com.example.prepwise.dataClass

import com.example.prepwise.models.Category
import com.example.prepwise.models.Level
import com.example.prepwise.models.Question

data class ResourceDetailsResponse(
    val author: Author,
    val category: Category,
    val level: Level,
    val title: String,
    val description: String,
    val created_at: String,
    val likes: Int,
    val dislikes: Int,
    val userReaction: String,
    val isAuthor: Boolean
)

data class Author(
    val id: Int,
    val username: String
)

//data class Category(
//    val id: Int,
//    val name: String
//)
//
//data class Level(
//    val id: Int,
//    val name: String
//)

