package com.example.prepwise.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prepwise.R
import com.example.prepwise.SpaceItemDecoration
import com.example.prepwise.adapters.AdapterNewFriend
import com.example.prepwise.models.People
import com.example.prepwise.models.Resourse
import com.example.prepwise.models.Set

class NewFollowersFragment : DialogFragment() {

    val emptySetList = arrayListOf<Set>()
    val emptyResourcesList = arrayListOf<Resourse>()

    val newFriendList = arrayListOf(
        People(
            id = 1,
            userImg = "img1.png",
            username = "User1",
            status = "Follower",
            numberOfFollowing = 100,
            numberOfFollowers = 200,
            description = "User1 description",
            email = "user1@example.com",
            location = "City1",
            sets = emptySetList,
            resouces = emptyResourcesList
        ),
        People(
            id = 2,
            userImg = "img2.png",
            username = "User2",
            status = "Follower",
            numberOfFollowing = 150,
            numberOfFollowers = 180,
            description = "User2 description",
            email = "user2@example.com",
            location = "City2",
            sets = emptySetList,
            resouces = emptyResourcesList
        ),
        People(
            id = 3,
            userImg = "img6.png",
            username = "User6",
            status = "Friends",
            numberOfFollowing = 220,
            numberOfFollowers = 280,
            description = "User6 description",
            email = "user6@example.com",
            location = "City6",
            sets = emptySetList,
            resouces = emptyResourcesList
        )
    )

    private var adapterFriend: AdapterNewFriend? = null
    private lateinit var recyclerViewFriend: RecyclerView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_new_followers, container, false)

        setStyle(STYLE_NO_TITLE, R.style.TransparentDialogTheme)

        val close: ImageView = view.findViewById(R.id.close)
        close.setOnClickListener{
            dismiss()
        }

        recyclerViewFriend = view.findViewById(R.id.recyclerView)
        recyclerViewFriend.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapterFriend = AdapterNewFriend(newFriendList, requireContext())
        recyclerViewFriend.adapter = adapterFriend

        val spacingInDp = 8
        val scale = requireContext().resources.displayMetrics.density
        val spacingInPx = (spacingInDp * scale).toInt()
        recyclerViewFriend.addItemDecoration(SpaceItemDecoration(spacingInPx))

        return view
    }

    override fun onStart() {
        super.onStart()

        val width = (resources.displayMetrics.widthPixels * 0.9).toInt()
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

}
