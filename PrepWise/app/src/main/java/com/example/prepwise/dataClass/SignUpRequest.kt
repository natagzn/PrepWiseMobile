package com.example.prepwise.dataClass

import com.example.prepwise.activities.ForgotPasswordActivity

data class SignUpRequest(
    val username: String,
    val email: String,
    val password: String,
    val password_confirmation: String
)

