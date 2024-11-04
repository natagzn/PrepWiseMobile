package com.example.prepwise.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.prepwise.fragments.AllResultFragment
import com.example.prepwise.fragments.SetsFragment
import com.example.prepwise.fragments.ResourcesFragment
import com.example.prepwise.fragments.UsersFragment
import com.example.prepwise.models.People
import com.example.prepwise.models.Resourse
import com.example.prepwise.models.Set

class ViewPagerSearchAdapter(
    private val setsList: ArrayList<Set>,
    private val usersList: ArrayList<People>,
    private val resourcesList: ArrayList<Resourse>,
    activity: FragmentActivity
) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AllResultFragment.newInstance(ArrayList(usersList.take(3)), ArrayList(setsList.take(3)), ArrayList(resourcesList.take(3)))
            1 -> SetsFragment.newInstance(setsList, "Search")
            2 -> ResourcesFragment.newInstance(resourcesList)
            3 -> UsersFragment.newInstance(usersList)
            else -> AllResultFragment.newInstance(ArrayList(usersList.take(3)), ArrayList(setsList.take(3)), ArrayList(resourcesList.take(3)))
        }
    }
}
