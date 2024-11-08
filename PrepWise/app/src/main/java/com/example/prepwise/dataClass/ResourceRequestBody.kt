package com.example.prepwise.dataClass

data class ResourceRequestBody (
    val title: String,
    val description: String,
    val levelId: Int,
    val categoryId: Int
)