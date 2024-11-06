package com.example.prepwise.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prepwise.DialogUtils
import com.example.prepwise.R
import com.example.prepwise.SpaceItemDecoration
import com.example.prepwise.adapters.AdapterSet
import com.example.prepwise.models.Category
import com.example.prepwise.models.Level
import com.example.prepwise.models.Set


class SetsFragment : Fragment() {

    private lateinit var setList: ArrayList<Set>
    private var selectedCategories = mutableListOf<Category>()
    private var selectedLevels = mutableListOf<Level>()
    private var selectedAccesses = mutableListOf<String>()
    private var paramPage: String = "Library"

    private lateinit var contentLayout: LinearLayout
    private lateinit var emptyListTxt: TextView
    private lateinit var emptyFilteredListTxt: TextView

    companion object {
        private const val ARG_SET_LIST = "set_list"
        private const val ARG_PARAM_PAGE = "param_page"

        fun newInstance(setList: ArrayList<Set>, paramPage: String): SetsFragment {
            val fragment = SetsFragment()
            val args = Bundle()
            args.putSerializable(ARG_SET_LIST, setList)
            args.putString(ARG_PARAM_PAGE, paramPage)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            @Suppress("UNCHECKED_CAST")
            setList = it.getSerializable(ARG_SET_LIST) as? ArrayList<Set> ?: arrayListOf()
            paramPage = it.getSerializable(ARG_PARAM_PAGE) as? String ?: "Library"
        }
    }

    private var adapterSet: AdapterSet? = null
    private lateinit var recyclerViewSet: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sets, container, false)

        contentLayout = view.findViewById(R.id.content)
        emptyListTxt = view.findViewById(R.id.empty)
        emptyFilteredListTxt = view.findViewById(R.id.empty_filter)

        if (setList.isEmpty()) {
            emptyListTxt.visibility = View.VISIBLE
            contentLayout.visibility = View.GONE
        } else {
            emptyListTxt.visibility = View.GONE
            contentLayout.visibility = View.VISIBLE

            recyclerViewSet = view.findViewById(R.id.set_list)
            recyclerViewSet.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapterSet = AdapterSet(setList, requireContext(), parentFragmentManager,"with access")
            recyclerViewSet.adapter = adapterSet

            val spacingInDp = 10
            val scale = requireContext().resources.displayMetrics.density
            val spacingInPx = (spacingInDp * scale).toInt()
            recyclerViewSet.addItemDecoration(SpaceItemDecoration(spacingInPx))

            val sortButton: LinearLayout = view.findViewById(R.id.sort_btn)
            sortButton.setOnClickListener {
                val adapter = adapterSet
                if (adapter != null) {
                    DialogUtils.showSortPopupMenu(
                        requireContext(),
                        anchorView = sortButton,
                        list = setList,
                        adapter = adapter,
                        getDate = { it.date },
                        getName = { it.name }
                    )
                }
            }

            val filterBtn: LinearLayout = view.findViewById(R.id.filter_btn)
            var showAccess = false
            if(paramPage == "Library" || paramPage == "Liked") showAccess = true
            filterBtn.setOnClickListener{
                DialogUtils.showFilterPopup(
                    context = requireContext(),
                    anchorView = filterBtn,
                    onApplyFilters = { selectedCategories, selectedLevels, selectedAccesses ->
                        this.selectedCategories = selectedCategories.toMutableList()
                        this.selectedLevels = selectedLevels.toMutableList()
                        this.selectedAccesses = selectedAccesses.toMutableList()
                        applyFilters(selectedCategories, selectedLevels, selectedAccesses)
                    },
                    accessOptions = listOf("public", "private"),
                    currentCategories = selectedCategories,
                    currentLevels = selectedLevels,
                    currentAccesses = selectedAccesses,
                    showAccess
                )
            }
        }

        return view
    }

    private fun applyFilters(
        selectedCategories: List<Category>,
        selectedLevels: List<Level>,
        selectedAccesses: List<String>
    ) {
        val filteredList = setList.filter { set ->
            (selectedCategories.isEmpty() || set.categories.any { category ->
                selectedCategories.any { it.id == category.id }
            }) &&
                    (selectedLevels.isEmpty() || selectedLevels.any { it.id == set.level.id }) &&
                    (selectedAccesses.isEmpty() || set.access in selectedAccesses)
        }

        // Оновлення адаптера з фільтрованим списком
        adapterSet?.updateData(filteredList)

        // Відображення або приховування тексту для порожнього списку
        if (filteredList.isEmpty()) {
            emptyFilteredListTxt.visibility = View.VISIBLE
            recyclerViewSet.visibility = View.GONE
        } else {
            emptyFilteredListTxt.visibility = View.GONE
            recyclerViewSet.visibility = View.VISIBLE
        }
    }
}

