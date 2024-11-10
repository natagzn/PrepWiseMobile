package com.example.prepwise.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.prepwise.R
import com.example.prepwise.activities.MainActivity
import com.example.prepwise.adapters.ViewPagerLibratyAdapter
import com.example.prepwise.repositories.FolderRepository
import com.example.prepwise.repositories.ResourceRepository
import com.example.prepwise.repositories.SetRepository
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class LibraryFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var progressBarLoading: ProgressBar
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_library, container, false)

        progressBarLoading = view.findViewById(R.id.progressBarLoading)
        viewPager = view.findViewById(R.id.viewPager)

        viewLifecycleOwner.lifecycleScope.launch{
            progressBarLoading.visibility = View.VISIBLE
            viewPager.visibility = View.GONE

            SetRepository.fetchAllSets()
            ResourceRepository.fetchAllResources()
            FolderRepository.fetchAllFolders()

            progressBarLoading.visibility = View.GONE
            viewPager.visibility = View.VISIBLE
        }

        val foldersList = MainActivity.currentUser!!.folders
        val setsList = MainActivity.currentUser!!.sets
        val sharedList = MainActivity.currentUser!!.sharedSets
        val resourcesList = MainActivity.currentUser!!.resources

        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)

        val adapter = ViewPagerLibratyAdapter(foldersList, setsList, sharedList, resourcesList, requireActivity())
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.sets)
                1 -> tab.text = getString(R.string.folders)
                2 -> tab.text = getString(R.string.shared)
                3 -> tab.text = getString(R.string.resources)
            }
        }.attach()

        return view
    }
}