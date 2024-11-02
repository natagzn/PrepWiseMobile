package com.example.prepwise.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.prepwise.fragments.AllResultFragment
import com.example.prepwise.fragments.FoldersFragment
import com.example.prepwise.fragments.SetsFragment
import com.example.prepwise.fragments.ResourcesFragment
import com.example.prepwise.fragments.SharedFragment
import com.example.prepwise.fragments.UsersFragment
import com.example.prepwise.models.Folder
import com.example.prepwise.models.People
import com.example.prepwise.models.Resourse
import com.example.prepwise.models.Set
import com.example.prepwise.models.SharedSet

class ViewPagerUserAdapter(
    private val setsList: ArrayList<Set>,
    private val resourcesList: ArrayList<Resourse>,
    activity: FragmentActivity
) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SetsFragment.newInstance(setsList)
            1 -> ResourcesFragment.newInstance(resourcesList)
            else -> SetsFragment.newInstance(setsList)
        }
    }
}
