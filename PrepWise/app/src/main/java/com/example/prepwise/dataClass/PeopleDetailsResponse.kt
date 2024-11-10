package com.example.prepwise.dataClass

class PeopleDetailsResponse(
    var id: Int,
    var username: String,
    var description: String,
    var location: String,
    var publicSetIds: List<Int>,
    var resourceIds: List<Int>,
    var subscriberCount: String,
    var subscriptionCount: String,
    var relationshipStatus: String?
)