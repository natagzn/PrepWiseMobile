package com.example.prepwise.models

import java.io.Serializable

class User(
    var id: Int = 0,
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
    var premium: Boolean = true
) : Serializable
