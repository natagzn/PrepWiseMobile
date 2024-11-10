package com.example.prepwise.dataClass

data class AddFavoriteResponse(
    val message: String,
    val favourite: Favourite
)

data class Favourite(
    val user_id: Int,
   // val question_list_id: Int,
    val created_at: String,
    val updated_at: String,
    val favourite_id: Int
)

data class DeleteFavoriteResponse(
    val message: String
)

