package com.example.prepwise.dataClass

import com.example.prepwise.models.Question

data class FolderDetailsResponse(
    val name: String,
    val sets: List<Int>,
    val date: String,
    val isFavourite: Boolean
)