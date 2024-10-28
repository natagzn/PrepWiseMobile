package com.example.prepwise.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.prepwise.FolderListProvider
import com.example.prepwise.R
import com.example.prepwise.ResourceListProvider
import com.example.prepwise.SetListProvider
import com.example.prepwise.SharedSetListProvider
import com.example.prepwise.activities.MainActivity
import com.example.prepwise.adapters.AdapterResource
import com.example.prepwise.adapters.ViewPagerLibratyAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.MainScope
import android.view.View as View1

class LibraryFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View1? {
        val view = inflater.inflate(R.layout.fragment_library, container, false)

        val foldersList = MainActivity.folderList
        val setsList = MainActivity.setList
        val sharedList = MainActivity.sharedSetList
        val resourcesList = MainActivity.resourceList

        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)

        val adapter = ViewPagerLibratyAdapter(foldersList, setsList, sharedList, resourcesList, requireActivity())
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.question_sets)
                1 -> tab.text = getString(R.string.folders)
                2 -> tab.text = getString(R.string.shared)
                3 -> tab.text = getString(R.string.resources)
            }
        }.attach()

        return view
    }
}