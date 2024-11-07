package com.example.prepwise.dataClass

import com.example.prepwise.models.Question

data class SetDetailsResponse(
    val name: String,
    val level: LevelResponse,
    val categories: List<CategoryResponse1>,
    val author: AuthorResponse,
    val createdAt: String,
    val questions: List<Question>,
    val access: String,
    val isFavourite: Boolean
)

data class LevelResponse(
    val levelId: Int,
    val name: String
)

data class CategoryResponse1(
    val id: Int,
    val name: String
)

data class AuthorResponse(
    val username: String
)

data class QuestionResponse(
    val questionId: Int,
    val content: String
)
