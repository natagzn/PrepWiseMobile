package com.example.prepwise.dataClass

import com.example.prepwise.models.Category
import com.google.gson.annotations.SerializedName

import com.google.gson.JsonElement
import java.util.Date

data class CategoryResponse(
    val message: String,
    val categories: List<Category>
)

