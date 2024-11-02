package com.example.prepwise.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import androidx.viewpager2.widget.ViewPager2
import com.example.prepwise.R
import com.example.prepwise.activities.MainActivity
import com.example.prepwise.adapters.ViewPagerLibratyAdapter
import com.example.prepwise.adapters.ViewPagerSearchAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class SearchFragment : Fragment() {
    private lateinit var searchQuery: String
    private lateinit var searchInput: EditText
    private var initialTabIndex: Int = 0

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

        val setsList = MainActivity.setList
        val userList = MainActivity.userList
        val resourcesList = MainActivity.resourceList

        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)

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
            getActivity()?.getSupportFragmentManager()?.popBackStack();
        }

        return view
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

}