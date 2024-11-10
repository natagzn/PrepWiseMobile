package com.example.prepwise.dataClass

import com.example.prepwise.models.Folder
import com.example.prepwise.models.People
import com.example.prepwise.models.Resource
import com.example.prepwise.models.SharedSet
import com.example.prepwise.models.Set

data class UserResponse (
    var user_id: Int = 0,
    var userImg: String = "",
    var username: String = "",
    var bio: String = "",
    var email: String = "",
    var location: String = "",
    var sets: ArrayList<Set> = arrayListOf(),
    var sharedSets: ArrayList<SharedSet> = arrayListOf(),
    var resources: ArrayList<Resource> = arrayListOf(),
    var folders: ArrayList<Folder> = arrayListOf(),
    var friends: ArrayList<People> = arrayListOf(),
    var followers: ArrayList<People> = arrayListOf(),
    var following: ArrayList<People> = arrayListOf(),
    var premium: Boolean = false
)