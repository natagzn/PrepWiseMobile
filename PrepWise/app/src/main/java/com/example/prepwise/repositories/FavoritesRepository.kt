package com.example.prepwise.repositories

import android.util.Log
import com.example.prepwise.models.Folder
import com.example.prepwise.models.Resource
import com.example.prepwise.models.Set
import com.example.prepwise.utils.RetrofitInstance

object FavoritesRepository {

    suspend fun getUserFavorites(): Triple<List<Folder>, List<Set>, List<Resource>> {
        val foldersList = mutableListOf<Folder>()
        val setsList = mutableListOf<Set>()
        val resourcesList = mutableListOf<Resource>()

        val favoritesResponse = RetrofitInstance.api().getFavorites()
        if (favoritesResponse.isSuccessful && favoritesResponse.body() != null) {
            val favoritesData = favoritesResponse.body()!!.favourites

            for (folder in favoritesData.folders) {
                val folderDetails = FolderRepository.getFolderById(folder.folder_id)
                folderDetails?.let { foldersList.add(it) }
            }

            for (set in favoritesData.sets) {
                val setDetails = SetRepository.getSetById(set.question_set_id)
                setDetails?.let { setsList.add(it) }
            }

            for (resource in favoritesData.resources) {
                val resourceDetails = ResourceRepository.getResourceById(resource.resource_id)
                resourceDetails?.let { resourcesList.add(it) }
            }
        } else {
            Log.e("FavoritesRepository", "Error fetching user favorites: ${favoritesResponse.message()}")
        }

        return Triple(foldersList, setsList, resourcesList)
    }

    suspend fun addSetToFavorites(questionListId: Int): Boolean {
        return try {
            val requestBody = mapOf("questionListId" to questionListId)
            val response = RetrofitInstance.api().addSetToFavorites(requestBody)

            if (response.isSuccessful && response.body() != null) {
                Log.d("FavoritesRepository", response.body()!!.message)
                true
            } else {
                Log.e("FavoritesRepository", "Failed to add set to favorites: ${response.message()}")
                false
            }
        } catch (e: Exception) {
            Log.e("FavoritesRepository", "Exception: ${e.message}")
            false
        }
    }

    suspend fun deleteSetFromFavorites(questionListId: Int): Boolean {
        return try {
            val response = RetrofitInstance.api().deleteSetFromFavorite(questionListId)

            if (response.isSuccessful && response.body() != null) {
                Log.d("FavoritesRepository", response.body()!!.message)
                true
            } else {
                Log.e("FavoritesRepository", "Failed to delete set from favorites: ${response.message()}")
                false
            }
        } catch (e: Exception) {
            Log.e("FavoritesRepository", "Exception: ${e.message}")
            false
        }
    }

    suspend fun addFolderToFavorites(folderId: Int): Boolean {
        return try {
            val requestBody = mapOf("folderId" to folderId)
            val response = RetrofitInstance.api().addFolderToFavorites(requestBody)

            if (response.isSuccessful && response.body() != null) {
                Log.d("FavoritesRepository", response.body()!!.message)
                true
            } else {
                Log.e("FavoritesRepository", "Failed to add folder to favorites: ${response.message()}")
                false
            }
        } catch (e: Exception) {
            Log.e("FavoritesRepository", "Exception: ${e.message}")
            false
        }
    }

    suspend fun deleteFolderFromFavorites(folderId: Int): Boolean {
        return try {
            val response = RetrofitInstance.api().deleteFolderFromFavorite(folderId)

            if (response.isSuccessful && response.body() != null) {
                Log.d("FavoritesRepository", response.body()!!.message)
                true
            } else {
                Log.e("FavoritesRepository", "Failed to delete folder from favorites: ${response.message()}")
                false
            }
        } catch (e: Exception) {
            Log.e("FavoritesRepository", "Exception: ${e.message}")
            false
        }
    }
}
