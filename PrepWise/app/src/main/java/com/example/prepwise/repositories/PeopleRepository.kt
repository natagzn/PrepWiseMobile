package com.example.prepwise.repositories

import android.util.Log
import com.example.prepwise.models.Category
import com.example.prepwise.models.Level
import com.example.prepwise.models.People
import com.example.prepwise.models.Question
import com.example.prepwise.models.Resource
import com.example.prepwise.models.Set
import com.example.prepwise.utils.RetrofitInstance

object PeopleRepository {

    suspend fun getPeopleById(peopleId: Int): People? {
        val setResponse = RetrofitInstance.api().getPeopleById(peopleId)
        return if (setResponse.isSuccessful && setResponse.body() != null) {
            val peopleData = setResponse.body()!!

            val setsList = ArrayList<Set>()
            val resourcesList = ArrayList<Resource>()

            // Завантажуємо публічні сети
            for (setId in peopleData.publicSetIds) {
                val setDetails = SetRepository.getSetById(setId)
                setDetails?.let { setsList.add(it) }
            }

            // Завантажуємо ресурси
            for (resourceId in peopleData.resourceIds) {
                val resourceDetails = ResourceRepository.getResourceById(resourceId)
                resourceDetails?.let { resourcesList.add(it) }
            }

            // Перетворення статусу відношення
            val formattedStatus = when (peopleData.relationshipStatus) {
                "friend" -> "friends"
                "subscriber" -> "follower"
                "subscription" -> "following"
                else -> "none"
            }

            // Повертаємо об'єкт People
            People(
                id = peopleId,
                userImg = "",  // Додайте логіку для завантаження зображення, якщо необхідно
                username = peopleData.username,
                numberOfFollowing = peopleData.subscriptionCount.toIntOrNull() ?: 0,
                numberOfFollowers = peopleData.subscriberCount.toIntOrNull() ?: 0,
                description = peopleData.description,
                location = peopleData.location,
                status = formattedStatus,
                sets = setsList,
                resouces = resourcesList
            )
        } else {
            Log.e("PeopleRepository", "Error fetching people details: ${setResponse.message()}")
            null
        }
    }
}