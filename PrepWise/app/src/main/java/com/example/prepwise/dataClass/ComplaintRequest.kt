package com.example.prepwise.dataClass

data class ComplaintRequest(
    val userIdCompl: Int?,
    val resourcesId: Int?,
    val setId: Int?,
    val context: String
)
