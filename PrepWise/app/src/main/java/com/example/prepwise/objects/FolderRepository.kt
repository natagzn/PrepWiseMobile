package com.example.prepwise.objects

import android.util.Log
import com.example.prepwise.activities.MainActivity
import com.example.prepwise.models.Category
import com.example.prepwise.models.Folder
import com.example.prepwise.models.Level
import com.example.prepwise.models.Question
import com.example.prepwise.models.Set
import com.example.prepwise.models.User
import retrofit2.HttpException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object FolderRepository {

    var currentUser: User = MainActivity.currentUser

    // Функція для отримання папки за її ID
    suspend fun getFolderById(folderId: Int): Folder? {
        val folderResponse = RetrofitInstance.api().getFolderById(folderId)
        return if (folderResponse.isSuccessful && folderResponse.body() != null) {
            val folderData = folderResponse.body()!!
            Folder(
                id = folderId,
                name = folderData.name,
                sets = folderData.sets.mapNotNull { setId ->
                    SetRepository.getSetById(setId)  // Отримуємо повні дані сета за його ID
                } as ArrayList,
                date = LocalDateTime.parse(folderData.date, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toLocalDate(),
                isLiked = folderData.isFavourite
            )
        } else {
            Log.e("FolderRepository", "Error fetching folder details: ${folderResponse.message()}")
            null
        }
    }

    // Функція для завантаження всіх папок
    suspend fun fetchAllFolders() {
        try {
            val response = RetrofitInstance.api().getAllFolders()
            if (response.isSuccessful && response.body() != null) {
                val apiResponse = response.body()!!
                val apiFolders = apiResponse.data
                currentUser.folders.clear()

                // Завантажуємо повні дані для кожної папки
                val folders = mutableListOf<Folder>()
                for (apiFolder in apiFolders) {
                    val folder = getFolderById(apiFolder.folder_id)
                    folder?.let { folders.add(it) }
                }

                // Додаємо завантажені папки до currentUser
                currentUser.folders.addAll(folders)
            } else {
                Log.e("FolderRepository", "Error fetching folders: ${response.message()}")
            }
        } catch (e: HttpException) {
            Log.e("FolderRepository", "HttpException: ${e.message}")
        } catch (e: Exception) {
            Log.e("FolderRepository", "Exception: ${e.message}")
        }
    }
}