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
import com.example.prepwise.adapters.ViewPagerLikedAdapter
import com.example.prepwise.models.Folder
import com.example.prepwise.models.Resource
import com.example.prepwise.models.Set
import com.example.prepwise.objects.FavoritesRepository
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class LikedFragment : Fragment() {

    private lateinit var foldersList: ArrayList<Folder>
    private lateinit var setsList: ArrayList<Set>
    private lateinit var resourcesList: ArrayList<Resource>
    private lateinit var adapter: ViewPagerLikedAdapter
    private lateinit var progressBarLoading: ProgressBar
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_liked, container, false)

        progressBarLoading = view.findViewById(R.id.progressBarLoading)
        viewPager = view.findViewById(R.id.viewPager)

        // Ініціалізуємо порожні списки
        foldersList = arrayListOf()
        setsList = arrayListOf()
        resourcesList = arrayListOf()

        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)

        // Створюємо адаптер і прив'язуємо його до ViewPager
        adapter = ViewPagerLikedAdapter(foldersList, setsList, resourcesList, requireActivity())
        viewPager.adapter = adapter

        // Налаштовуємо TabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.sets)
                1 -> tab.text = getString(R.string.folders)
                2 -> tab.text = getString(R.string.resources)
            }
        }.attach()

        // Завантажуємо вподобані елементи
        viewLifecycleOwner.lifecycleScope.launch {
            // Показуємо індикатор завантаження (якщо потрібен)
             progressBarLoading.visibility = View.VISIBLE
             viewPager.visibility = View.GONE

            // Отримуємо вподобані елементи
            val (folders, sets, resources) = FavoritesRepository.getUserFavorites()

            // Додаємо отримані дані до списків
            foldersList.addAll(folders)
            setsList.addAll(sets)
            resourcesList.addAll(resources)

            // Оновлюємо адаптер після завантаження даних
            adapter.notifyDataSetChanged()

            // Приховуємо індикатор завантаження (якщо потрібен)
             progressBarLoading.visibility = View.GONE
             viewPager.visibility = View.VISIBLE
        }

        return view
    }
}
