package com.example.prepwise.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.prepwise.fragments.SetsFragment
import com.example.prepwise.fragments.ResourcesFragment
import com.example.prepwise.models.Resourse
import com.example.prepwise.models.Set

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
            0 -> SetsFragment.newInstance(setsList, "Library")
            1 -> ResourcesFragment.newInstance(resourcesList)
            else -> SetsFragment.newInstance(setsList, "Library")
        }
    }
}
