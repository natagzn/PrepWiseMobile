package com.example.prepwise.models

class People(val userImg:String, var username: String, var status: String,
             var numberOfFollowing: Int, var numberOfFollowers: Int, var description: String,
             var email: String, var location: String, var sets: ArrayList<Set>, var resouces: ArrayList<Resourse>) {
}