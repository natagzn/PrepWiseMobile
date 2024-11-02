package com.example.prepwise.models

import java.io.Serializable

class People(val id: Int, var userImg:String, var username: String, var status: String,
             var numberOfFollowing: Int, var numberOfFollowers: Int, var description: String,
             var email: String, var location: String, var sets: ArrayList<Set>, var resouces: ArrayList<Resourse>): Serializable {
}