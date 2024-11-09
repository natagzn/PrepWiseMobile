package com.example.prepwise

import com.example.prepwise.dataClass.AllResourceIdResponse
import com.example.prepwise.dataClass.CategoryResponse
import com.example.prepwise.dataClass.LoginRequest
import com.example.prepwise.dataClass.LoginResponse
import com.example.prepwise.dataClass.AllSetIdResponse
import com.example.prepwise.dataClass.DateOfVisitResponse
import com.example.prepwise.dataClass.FavoriteRequestBody
import com.example.prepwise.dataClass.LevelResponse1
import com.example.prepwise.dataClass.QuestionRequestBody
import com.example.prepwise.dataClass.ResourceDetailsResponse
import com.example.prepwise.dataClass.ResourceRequestBody
import com.example.prepwise.dataClass.SetDetailsResponse
import com.example.prepwise.dataClass.SetRequestBody
import com.example.prepwise.dataClass.SetResponse
import com.example.prepwise.dataClass.SignUpRequest
import com.example.prepwise.dataClass.SignUpResponse
import com.example.prepwise.dataClass.UpdateProfileRequest
import com.example.prepwise.dataClass.UpdateSetRequest
import com.example.prepwise.models.Set
import com.example.prepwise.models.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
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
    suspend fun getLevels(): Response<List<LevelResponse1>>

    @GET("/api/profile")
    suspend fun getUserProfile(): Response<User>

    @GET("/api/setsAll")
    suspend fun getAllSets(): Response<List<AllSetIdResponse>>

    @GET("/api/sets/{id}")
    suspend fun getSetById(@Path("id") id: Int): Response<SetDetailsResponse>

    @GET("/api/resources")
    suspend fun getAllResource(): Response<List<AllResourceIdResponse>>

    @GET("/api/resources/{id}")
    suspend fun getResourceById(@Path("id") id: Int): Response<ResourceDetailsResponse>

    @POST("/api/resources-likes")
    suspend fun addFavoriteResource(
        @Body requestBody: FavoriteRequestBody
    ): Response<Unit>

    @DELETE("/api/resources-likes/{resourceId}")
    suspend fun removeFavoriteResource(
        @Path("resourceId") resourceId: Int
    ): Response<Unit>

    @POST("/api/resources")
    suspend fun createResource(
        @Body requestBody: ResourceRequestBody
    ): Response<Unit>

    @DELETE("/api/resources/{id}")
    suspend fun deleteResource(
        @Path("resourceId") resourceId: Int
    ): Response<Unit>

    @POST("/api/sets")
    suspend fun createSet(
        @Body requestBody: SetRequestBody
    ): Response<SetResponse>

    @POST("/api/questions")
    suspend fun createQuestion(
        @Body requestBody: QuestionRequestBody
    ): Response<Unit>

    @PUT("/api/sets/{id}")
    suspend fun updateSet(
        @Path("id") id: Int,
        @Body body: UpdateSetRequest
    ): Response<Void>

    @PUT("/api/questions/{id}")
    suspend fun updateQuestion(
        @Path("id") id: Int,
        @Body body: QuestionRequestBody
    ): Response<Void>

    @DELETE("/api/questions/{id}")
    suspend fun deleteQuestion(
        @Path("id") id: Int
    ): Response<Void>

    @DELETE("/api/sets/{setId}/categories/{categoryId}")
    suspend fun deleteCategoryFromSet(
        @Path("setId") setId: Int,
        @Path("categoryId") categoryId: Int
    ): Response<Void>

    @POST("/api/sets/{setId}/categories/{categoryId}")
    suspend fun addCategoryToSet(
        @Path("setId") setId: Int,
        @Path("categoryId") categoryId: Int
    ): Response<Void>

    @POST("/api/update")
    suspend fun updateProfile(
        @Body body: UpdateProfileRequest
    ): Response<Void>

    @GET("/api/date-of-visits/days")
    suspend fun getDayIfVisit(): Response<DateOfVisitResponse>
}
