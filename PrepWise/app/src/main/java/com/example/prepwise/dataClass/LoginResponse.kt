package com.example.prepwise.dataClass

import com.google.gson.annotations.SerializedName

import com.google.gson.JsonElement

data class LoginResponse(
    val status: Int,
    val message: String,
    @SerializedName("token") val tokenData: TokenData
)

data class TokenData(
    val type: String,
    val token: String
)

