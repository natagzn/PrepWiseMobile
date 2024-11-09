package com.example.prepwise.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.prepwise.fragments.FoldersFragment
import com.example.prepwise.fragments.SetsFragment
import com.example.prepwise.fragments.ResourcesFragment
import com.example.prepwise.fragments.SharedFragment
import com.example.prepwise.models.Folder
import com.example.prepwise.models.Resource
import com.example.prepwise.models.Set
import com.example.prepwise.models.SharedSet

class ViewPagerLibratyAdapter(
    private var foldersList: ArrayList<Folder>,
    private var setsList: ArrayList<Set>,
    private var sharedList: ArrayList<SharedSet>,
    private var resourcesList: ArrayList<Resource>,
    activity: FragmentActivity
) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SetsFragment.newInstance(setsList, "Library")
            1 -> FoldersFragment.newInstance(foldersList)
            2 -> SharedFragment.newInstance(sharedList, "Library")
            3 -> ResourcesFragment.newInstance(resourcesList)
            else -> SetsFragment.newInstance(setsList, "Library")
        }
    }
}
