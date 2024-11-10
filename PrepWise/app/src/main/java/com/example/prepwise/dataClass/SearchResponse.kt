package com.example.prepwise.dataClass

data class SearchResponse(
    val sets: List<SetSummary>,
    val resources: List<ResourceSummary>,
    val users: List<UserSummary>
)

data class SetSummary(val question_set_id: Int, val name: String)
data class ResourceSummary(val resource_id: Int, val title: String, val description: String)
data class UserSummary(val user_id: Int, val username: String)
