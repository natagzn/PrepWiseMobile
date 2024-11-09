package com.example.prepwise.dataClass

data class SetResponse(
    val message: String,
    val set: SetDetails
)

data class SetDetails(
    val question_set_id: Int
)
