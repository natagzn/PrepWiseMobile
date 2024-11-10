package com.example.prepwise.objects

import android.util.Log
import com.example.prepwise.models.Folder
import com.example.prepwise.models.Resource
import com.example.prepwise.models.Set

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
}
