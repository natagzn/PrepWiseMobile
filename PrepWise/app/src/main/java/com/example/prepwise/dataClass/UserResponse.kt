package com.example.prepwise.dataClass

import com.example.prepwise.models.Folder
import com.example.prepwise.models.People
import com.example.prepwise.models.Resource
import com.example.prepwise.models.SharedSet
import kotlin.collections.Set

class UserResponse (
    var user_id: Int = 0,
    var avatar_url: String = "",
    var username: String = "",
    var bio: String = "",
    var email: String = "",
    var location: String = "",
    val subscription_type: String =""
)