package com.example.prepwise.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prepwise.R
import com.example.prepwise.SpaceItemDecoration
import com.example.prepwise.adapters.AdapterFriendHelp
import com.example.prepwise.models.People

class FriendHelpDialog : DialogFragment() {

    val peopleList = arrayListOf(
        People(
            userImg = "img_anna",
            username = "Anna",
            status = "friend",
            numberOfFollowing = 150,
            numberOfFollowers = 300,
            description = "Loves teaching math",
            email = "anna@example.com",
            location = "Kyiv, Ukraine",
            sets = arrayListOf(),
            resouces = arrayListOf()
        ),
        People(
            userImg = "img_john",
            username = "John",
            status = "friend",
            numberOfFollowing = 200,
            numberOfFollowers = 500,
            description = "History enthusiast",
            email = "john@example.com",
            location = "Lviv, Ukraine",
            sets = arrayListOf(),
            resouces = arrayListOf()
        ),
        People(
            userImg = "img_nina",
            username = "Nina",
            status = "friend",
            numberOfFollowing = 180,
            numberOfFollowers = 320,
            description = "Biology lover",
            email = "nina@example.com",
            location = "Odesa, Ukraine",
            sets = arrayListOf(),
            resouces = arrayListOf()
        ),
        People(
            userImg = "img_paul",
            username = "Paul",
            status = "friend",
            numberOfFollowing = 220,
            numberOfFollowers = 430,
            description = "Physics enthusiast",
            email = "paul@example.com",
            location = "Kharkiv, Ukraine",
            sets = arrayListOf(),
            resouces = arrayListOf()
        ),
        People(
            userImg = "img_sara",
            username = "Sara",
            status = "friend",
            numberOfFollowing = 170,
            numberOfFollowers = 290,
            description = "Chemistry teacher",
            email = "sara@example.com",
            location = "Dnipro, Ukraine",
            sets = arrayListOf(),
            resouces = arrayListOf()
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_friend_help, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_friends)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = AdapterFriendHelp(peopleList, requireContext())

        val spacingInDp = 10
        val scale = requireContext().resources.displayMetrics.density
        val spacingInPx = (spacingInDp * scale).toInt()
        recyclerView.addItemDecoration(SpaceItemDecoration(spacingInPx))

        val close: ImageView = view.findViewById(R.id.close_button)
        close.setOnClickListener{dismiss()}

        return view
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}