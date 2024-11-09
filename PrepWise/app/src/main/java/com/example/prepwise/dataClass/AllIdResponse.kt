package com.example.prepwise.dataClass

data class AllSetIdResponse (
    val question_set_id: Int
)

data class AllResourceIdResponse (
    val resource_id: Int
)

data class AllFoldersResponse(
    val message: String,
    val data: List<FolderData>
)

data class FolderData(
    val folder_id: Int,
    val user_id: Int,
    val name: String,
    val date: String,
    val created_at: String,
    val updated_at: String,
    val setsInFolder: List<SetInFolder>
)

data class SetInFolder(
    val id: Int,
    val set_id: Int,
    val folder_id: Int,
    val created_at: String,
    val updated_at: String,
    val set: SetDetails
)
