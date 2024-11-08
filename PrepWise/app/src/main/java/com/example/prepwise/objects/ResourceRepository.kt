package com.example.prepwise.objects

import android.util.Log
import com.example.prepwise.activities.MainActivity
import com.example.prepwise.dataClass.FavoriteRequestBody
import com.example.prepwise.models.Category
import com.example.prepwise.models.Level
import com.example.prepwise.models.Resource
import com.example.prepwise.models.User
import retrofit2.HttpException
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object ResourceRepository {

    var currentUser: User = MainActivity.currentUser

    suspend fun getResourceById(resourceId: Int): Resource? {
        val setResponse = RetrofitInstance.api().getResourceById(resourceId)
        return if (setResponse.isSuccessful && setResponse.body() != null) {
            val setData = setResponse.body()!!
            Log.d("API Data", "${setData.title}, ${setData.author.username}, ${setData.userReaction}, ${setData.likes}, ${setData.dislikes},${setData.created_at}")
            Resource(
                id = resourceId,
                articleBook = setData.title,
                description = setData.description,
                level = Level(setData.level.id, setData.level.name),
                category = Category(setData.category.id, setData.category.name),
                date = LocalDateTime.parse(setData.created_at, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toLocalDate(),
                username = setData.author.username,
                isLiked = setData.userReaction == "liked",
                isDisLiked = setData.userReaction == "disliked",
                numberOfLikes = setData.likes,
                numberOfDislikes = setData.dislikes,
                isAuthor = setData.isAuthor
            )

        } else {
            Log.e("ResourceRepository", "Error fetching resource details: ${setResponse.message()}")
            null
        }
    }

    // Функція для завантаження всіх сетів
    suspend fun fetchAllResources() {
        try {
            val response = RetrofitInstance.api().getAllResource()
            if (response.isSuccessful && response.body() != null) {
                val apiResources = response.body()!!
                currentUser.resources.clear()
                Log.d("SIZE", "${apiResources.size}")

                // Завантажуємо повні дані для кожного сета
                val resources = mutableListOf<Resource>()
                for (apiResource in apiResources) {
                    val resource = getResourceById(apiResource.resource_id)
                    Log.d("IIIDDD", "${apiResource.resource_id}")
                    resource?.let { resources.add(it) }
                }

                // Додаємо завантажені сети до currentUser
                currentUser.resources.addAll(resources)
            } else {
                Log.e("ResourceRepository", "Error fetching resources: ${response.message()}")
            }
        } catch (e: HttpException) {
            Log.e("ResourceRepository", "HttpException: ${e.message}")
        } catch (e: Exception) {
            Log.e("ResourceRepository", "Exception: ${e.message}")
        }
    }

    suspend fun addFavoriteResource(resourceId: Int, like: Boolean): Response<Unit> {
        return try {
            // Створюємо об'єкт FavoriteRequestBody з resourceId та like
            val requestBody = FavoriteRequestBody(resourceId, like)
            val response = RetrofitInstance.api().addFavoriteResource(requestBody)
            if (response.isSuccessful) {
                Log.d("ResourceRepository", "Лайк успішно додано")
            } else {
                Log.e("ResourceRepository", "Помилка при додаванні лайка: ${response.message()}")
            }
            response
        } catch (e: Exception) {
            Log.e("ResourceRepository", "Exception: ${e.message}")
            throw e
        }
    }


    suspend fun removeFavoriteResource(resourceId: Int): Response<Unit> {
        return try {
            val response = RetrofitInstance.api().removeFavoriteResource(resourceId)
            if (response.isSuccessful) {
                Log.d("ResourceRepository", "Лайк успішно видалено")
            } else {
                Log.e("ResourceRepository", "Помилка при видаленні лайка: ${response.message()}")
            }
            response
        } catch (e: Exception) {
            Log.e("ResourceRepository", "Exception: ${e.message}")
            throw e
        }
    }
}
