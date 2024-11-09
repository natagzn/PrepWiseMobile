package com.example.prepwise.dataClass

data class FolderResponse(
    val message: String,
    val folder: FolderDetails
)

data class FolderDetails(
    val folderId: Int
)
