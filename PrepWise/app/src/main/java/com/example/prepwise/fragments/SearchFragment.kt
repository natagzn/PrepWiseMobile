package com.example.prepwise.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.HomeFragment
import com.example.prepwise.R
import com.example.prepwise.activities.MainActivity
import com.example.prepwise.adapters.ViewPagerLibratyAdapter
import com.example.prepwise.adapters.ViewPagerSearchAdapter
import com.example.prepwise.models.People
import com.example.prepwise.models.Resource
import com.example.prepwise.models.Set
import com.example.prepwise.repositories.PeopleRepository
import com.example.prepwise.repositories.PeopleRepository.getPeopleById
import com.example.prepwise.repositories.ResourceRepository
import com.example.prepwise.repositories.ResourceRepository.getResourceById
import com.example.prepwise.repositories.SetRepository
import com.example.prepwise.repositories.SetRepository.getSetById
import com.example.prepwise.utils.RetrofitInstance
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private lateinit var searchQuery: String
    private lateinit var searchInput: EditText
    private var initialTabIndex: Int = 0

    private lateinit var progressBarLoading: ProgressBar
    private lateinit var viewPager: ViewPager2

    private var setsList = ArrayList<Set>()
    private var userList = ArrayList<People>()
    private var resourcesList = ArrayList<Resource>()

    companion object {
        private const val ARG_SEARCH_QUERY = "search_query"
        private const val ARG_INITIAL_TAB_INDEX = "initial_tab_index"

        fun newInstance(query: String): SearchFragment {
            val fragment = SearchFragment()
            val args = Bundle()
            args.putString(ARG_SEARCH_QUERY, query)
            fragment.arguments = args
            return fragment
        }

        fun newInstanceWithTab(tabIndex: Int): SearchFragment {
            val fragment = SearchFragment()
            val args = Bundle()
            args.putInt(ARG_INITIAL_TAB_INDEX, tabIndex)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            searchQuery = it.getString(ARG_SEARCH_QUERY, "")
            initialTabIndex = it.getInt(ARG_INITIAL_TAB_INDEX, 0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
        progressBarLoading = view.findViewById(R.id.progressBarLoading)
        viewPager = view.findViewById(R.id.viewPager)

        val adapter = ViewPagerSearchAdapter(setsList, userList, resourcesList, requireActivity())
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.all_results)
                1 -> tab.text = getString(R.string.Sets)
                2 -> tab.text = getString(R.string.resources)
                3 -> tab.text = getString(R.string.users)
            }
        }.attach()

        // Завантажуємо пошукові результати
        viewLifecycleOwner.lifecycleScope.launch {
            progressBarLoading.visibility = View.VISIBLE
            viewPager.visibility = View.GONE

            val (sets, users, resources) = getSearchResults(searchQuery)

            setsList.addAll(sets)
            userList.addAll(users)
            resourcesList.addAll(resources)

            adapter.notifyDataSetChanged()

            progressBarLoading.visibility = View.GONE
            viewPager.visibility = View.VISIBLE
        }

        // Перемикання на вкладку, передану з AllResultFragment
        viewPager.currentItem = initialTabIndex

        searchInput = view.findViewById(R.id.input_search)
        searchInput.setText(searchQuery)

        // обробник для клавіші "Пошук" на клавіатурі
        searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                openSearchFragment(searchInput.text.toString())
                true
            } else {
                false
            }
        }

        val close: ImageView = view.findViewById(R.id.close)
        close.setOnClickListener{
            val fragment = HomeFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    suspend fun getSearchResults(query: String): Triple<List<Set>, List<People>, List<Resource>> {
        val setsList = mutableListOf<Set>()
        val usersList = mutableListOf<People>()
        val resourcesList = mutableListOf<Resource>()

        val searchResponse = RetrofitInstance.api().getSearchResult(query)
        if (searchResponse.isSuccessful && searchResponse.body() != null) {
            val searchData = searchResponse.body()!!

            for (set in searchData.sets) {
                val setDetails = SetRepository.getSetById(set.question_set_id)
                setDetails?.let { setsList.add(it) }
            }

            for (user in searchData.users) {
                val userDetails = PeopleRepository.getPeopleById(user.user_id)
                userDetails?.let { usersList.add(it) }
            }

            for (resource in searchData.resources) {
                val resourceDetails = ResourceRepository.getResourceById(resource.resource_id)
                resourceDetails?.let { resourcesList.add(it) }
            }
        } else {
            Log.e("SearchRepository", "Error fetching search results: ${searchResponse.message()}")
        }

        return Triple(setsList, usersList, resourcesList)
    }

    private fun openSearchFragment(query: String) {
        val existingFragment = parentFragmentManager.findFragmentByTag("SearchFragment") as? SearchFragment

        if (existingFragment != null) {
            // Якщо фрагмент вже існує, оновлюємо пошуковий запит і виконуємо новий пошук
            existingFragment.updateSearchQuery(query)
        } else {
            // Якщо фрагмента немає, створюємо новий
            val searchFragment = SearchFragment.newInstance(query)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, searchFragment, "SearchFragment")
                .addToBackStack(null)
                .commit()
        }
    }

    fun updateSearchQuery(newQuery: String) {
        searchQuery = newQuery
        searchInput.setText(newQuery)
    }

    private fun fetchSearchResults() {

    }
}