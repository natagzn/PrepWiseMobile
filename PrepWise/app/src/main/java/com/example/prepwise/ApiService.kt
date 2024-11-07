package com.example.prepwise

import com.example.prepwise.dataClass.CategoryResponse
import com.example.prepwise.dataClass.LoginRequest
import com.example.prepwise.dataClass.LoginResponse
import com.example.prepwise.dataClass.AllSetIdResponse
import com.example.prepwise.dataClass.SetDetailsResponse
import com.example.prepwise.dataClass.SignUpRequest
import com.example.prepwise.dataClass.SignUpResponse
import com.example.prepwise.models.Level
import com.example.prepwise.models.Set
import com.example.prepwise.models.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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

    @GET("/api/profile")
    suspend fun getUserProfile(): Response<User>

    @GET("/api/setsAll")
    suspend fun getAllSets(): Response<List<AllSetIdResponse>>

    @GET("/api/sets/{id}")
    suspend fun getSetById(@Path("id") id: Int): Response<SetDetailsResponse>

}
