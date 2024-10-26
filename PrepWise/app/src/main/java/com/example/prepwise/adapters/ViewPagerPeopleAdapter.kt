package com.example.prepwise.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.prepwise.fragments.FriendFragment
import com.example.prepwise.models.People

class ViewPagerPeopleAdapter(
    private val followingList: ArrayList<People>,
    private val followersList: ArrayList<People>,
    private val friendsList: ArrayList<People>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FriendFragment.newInstance(followingList)
            1 -> FriendFragment.newInstance(followersList)
            2 -> FriendFragment.newInstance(friendsList)
            else -> FriendFragment.newInstance(followingList)
        }
    }
}
