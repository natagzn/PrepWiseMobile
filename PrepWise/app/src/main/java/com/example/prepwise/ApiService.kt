package com.example.prepwise

import com.example.prepwise.dataClass.CategoryResponse
import com.example.prepwise.dataClass.LevelData
import com.example.prepwise.dataClass.LoginRequest
import com.example.prepwise.dataClass.LoginResponse
import com.example.prepwise.dataClass.SignUpRequest
import com.example.prepwise.dataClass.SignUpResponse
import com.example.prepwise.models.Level
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

// Інтерфейс для API
interface ApiService {
    @POST("/api/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("/api/auth/register")
    suspend fun signUp(@Body request: SignUpRequest): SignUpResponse

    @GET("/api/categories")
    suspend fun getCategories(): Response<CategoryResponse>

    @GET("/api/levels")
    suspend fun getLevels(): Response<List<Level>>

}
