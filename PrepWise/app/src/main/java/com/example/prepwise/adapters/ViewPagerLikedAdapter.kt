package com.example.prepwise.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.prepwise.fragments.FoldersFragment
import com.example.prepwise.fragments.SetsFragment
import com.example.prepwise.fragments.ResourcesFragment
import com.example.prepwise.models.Folder
import com.example.prepwise.models.Resourse
import com.example.prepwise.models.Set

class ViewPagerLikedAdapter(
    private val foldersList: ArrayList<Folder>,
    private val setsList: ArrayList<Set>,
    private val resourcesList: ArrayList<Resourse>,
    activity: FragmentActivity
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SetsFragment.newInstance(setsList)
            1 -> FoldersFragment.newInstance(foldersList)
            2 -> ResourcesFragment.newInstance(resourcesList)
            else -> SetsFragment.newInstance(setsList)
        }
    }
}
