package com.example.prepwise.objects

import android.util.Log
import com.example.prepwise.activities.MainActivity
import com.example.prepwise.models.Category
import com.example.prepwise.models.Level
import com.example.prepwise.models.Question
import com.example.prepwise.models.Set
import com.example.prepwise.models.User
import retrofit2.HttpException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object SetRepository {

    var currentUser: User = MainActivity.currentUser

    // Функція для отримання сета за його ID
    suspend fun getSetById(setId: Int): Set? {
        val setResponse = RetrofitInstance.api().getSetById(setId)
        return if (setResponse.isSuccessful && setResponse.body() != null) {
            val setData = setResponse.body()!!
            Set(
                id = setId,
                name = setData.name,
                level = Level(setData.level.levelId, setData.level.name),
                categories = ArrayList(setData.categories.map { Category(it.id, it.name) }),
                access = setData.access,
                date = LocalDateTime.parse(setData.createdAt, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toLocalDate(),
                questions = ArrayList(setData.questions.map { Question(it.id, it.content, it.answer, it.learned) }),
                username = setData.author.username,
                isLiked = setData.isFavourite
            )
        } else {
            Log.e("SetRepository", "Error fetching set details: ${setResponse.message()}")
            null
        }
    }

    // Функція для завантаження всіх сетів
    suspend fun fetchAllSets() {
        try {
            val response = RetrofitInstance.api().getAllSets()
            if (response.isSuccessful && response.body() != null) {
                val apiSets = response.body()!!
                currentUser.sets.clear()

                // Завантажуємо повні дані для кожного сета
                val sets = mutableListOf<Set>()
                for (apiSet in apiSets) {
                    val set = getSetById(apiSet.question_set_id)
                    set?.let { sets.add(it) }
                }

                // Додаємо завантажені сети до currentUser
                currentUser.sets.addAll(sets)
            } else {
                Log.e("SetRepository", "Error fetching sets: ${response.message()}")
            }
        } catch (e: HttpException) {
            Log.e("SetRepository", "HttpException: ${e.message}")
        } catch (e: Exception) {
            Log.e("SetRepository", "Exception: ${e.message}")
        }
    }
}
