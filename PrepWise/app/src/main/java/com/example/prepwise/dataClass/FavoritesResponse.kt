package com.example.prepwise.dataClass

data class FavoritesResponse(
    val message: String,
    val favourites: Favourites
)

data class Favourites(
    val folders: List<Folder>,
    val sets: List<Set>,
    val resources: List<Resource>
)

data class Folder(
    val folder_id: Int
)

data class Set(
    val question_set_id: Int
)

data class Resource(
    val resource_id: Int
)
