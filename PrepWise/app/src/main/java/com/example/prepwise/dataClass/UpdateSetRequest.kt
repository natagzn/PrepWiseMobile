package com.example.prepwise.dataClass

data class UpdateSetRequest(
    val name: String? = null,
    val access: String? = null,
    val level_id: Int? = null,
    val categories: List<Int>? = null
)
