package com.example.prepwise.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.prepwise.LikedFolderListProvider
import com.example.prepwise.LikedResourceListProvider
import com.example.prepwise.LikedSetListProvider
import com.example.prepwise.R
import com.example.prepwise.adapters.ViewPagerLibratyAdapter
import com.example.prepwise.adapters.ViewPagerLikedAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class LikedFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_liked, container, false)

        val foldersList = LikedFolderListProvider.folderList
        val setsList = LikedSetListProvider.setList
        val resourcesList = LikedResourceListProvider.resourceList

        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)

        val adapter = ViewPagerLikedAdapter(foldersList, setsList, resourcesList, requireActivity())
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.question_sets)
                1 -> tab.text = getString(R.string.folders)
                2 -> tab.text = getString(R.string.resources)
            }
        }.attach()

        return view
    }


}