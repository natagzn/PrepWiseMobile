package com.example.prepwise.models

import java.io.Serializable

class User(val id: Int, var userImg:String, var username: String, var description: String, var email: String,
           var location: String, var sets: ArrayList<Set>, var sharedSets: ArrayList<SharedSet>,
           var resouces: ArrayList<Resourse>, var folders: ArrayList<Folder>,
           var friends: ArrayList<People>, var followers: ArrayList<People>,
           var following: ArrayList<People>, var premium: Boolean): Serializable {
}