package com.example.prepwise.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prepwise.objects.DialogUtils
import com.example.prepwise.R
import com.example.prepwise.SpaceItemDecoration
import com.example.prepwise.adapters.AdapterSharedSet
import com.example.prepwise.models.Category
import com.example.prepwise.models.Level
import com.example.prepwise.models.SharedSet
import com.example.prepwise.objects.KeyboardUtils.hideKeyboard

class SharedFragment : Fragment() {

    private lateinit var sharedList: ArrayList<SharedSet>
    private var selectedCategories = mutableListOf<Category>()
    private var selectedLevels = mutableListOf<Level>()
    private var selectedAccesses = mutableListOf<String>()
    private var paramPage: String = "Library"

    private lateinit var contentLayout: LinearLayout
    private lateinit var emptyListTxt: TextView
    private lateinit var emptyFilteredListTxt: TextView

    companion object {
        private const val ARG_SHARED_LIST = "shared_list"
        private const val ARG_PARAM_PAGE = "param_page"

        fun newInstance(sharedList: ArrayList<SharedSet>, paramPage: String): SharedFragment {
            val fragment = SharedFragment()
            val args = Bundle()
            args.putSerializable(ARG_SHARED_LIST, sharedList)
            args.putString(ARG_PARAM_PAGE, paramPage)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            @Suppress("UNCHECKED_CAST")
            sharedList = it.getSerializable(ARG_SHARED_LIST) as? ArrayList<SharedSet> ?: arrayListOf()
            paramPage = it.getSerializable(ARG_PARAM_PAGE) as? String ?: "Library"
        }
    }

    private var adapterSharedSet: AdapterSharedSet? = null
    private lateinit var recyclerViewSharedSet: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_shared, container, false)

        contentLayout = view.findViewById(R.id.content)
        emptyListTxt = view.findViewById(R.id.empty)
        emptyFilteredListTxt = view.findViewById(R.id.empty_filter)

        if (sharedList.isEmpty()) {
            emptyListTxt.visibility = View.VISIBLE
            contentLayout.visibility = View.GONE
        } else {
            emptyListTxt.visibility = View.GONE
            contentLayout.visibility = View.VISIBLE

            recyclerViewSharedSet = view.findViewById(R.id.shared_set_list)
            recyclerViewSharedSet.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapterSharedSet = AdapterSharedSet(sharedList, requireContext())
            recyclerViewSharedSet.adapter = adapterSharedSet

            val spacingInDp = 10
            val scale = requireContext().resources.displayMetrics.density
            val spacingInPx = (spacingInDp * scale).toInt()
            recyclerViewSharedSet.addItemDecoration(SpaceItemDecoration(spacingInPx))

            val sortButton: LinearLayout = view.findViewById(R.id.sort_btn)

            sortButton.setOnClickListener {
                val adapter = adapterSharedSet
                if (adapter != null) {
                    DialogUtils.showSortPopupMenu(
                        requireContext(),
                        anchorView = sortButton,
                        list = sharedList,
                        adapter = adapter,
                        getDate = { it.date },
                        getName = { it.name }
                    )
                }
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
                accessOptions = listOf("sharing", "view", "edit"),
                currentCategories = selectedCategories,
                currentLevels = selectedLevels,
                currentAccesses = selectedAccesses,
                showAccess
            )
        }

        val searchInput: EditText = view.findViewById(R.id.input_shared_set_name)
        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                filterSets(query)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard(requireContext(), view)
                true
            } else {
                false
            }
        }

        return view
    }

    private fun filterSets(query: String) {
        val filteredList = if (query.isEmpty()) {
            sharedList  // Якщо поле пошуку порожнє, показуємо весь список
        } else {
            sharedList.filter { set ->
                set.name.contains(query, ignoreCase = true)  // Фільтруємо за назвою
            }
        }

        adapterSharedSet?.updateData(filteredList)  // Оновлюємо дані в адаптері

        // Відображення або приховування тексту для порожнього списку
        if (filteredList.isEmpty()) {
            emptyFilteredListTxt.visibility = View.VISIBLE
            recyclerViewSharedSet.visibility = View.GONE
        } else {
            emptyFilteredListTxt.visibility = View.GONE
            recyclerViewSharedSet.visibility = View.VISIBLE
        }
    }

    // Функція для застосування фільтрів
    private fun applyFilters(
        selectedCategories: List<Category>,
        selectedLevels: List<Level>,
        selectedAccesses: List<String>
    ) {
        val filteredList = sharedList.filter { set ->
            (selectedCategories.isEmpty() || set.categories.any { category ->
                selectedCategories.any { it.id == category.id }
            }) &&
                    (selectedLevels.isEmpty() || selectedLevels.any { it.id == set.level.id }) &&
                    (selectedAccesses.isEmpty() || set.type in selectedAccesses)
        }

        adapterSharedSet?.updateData(filteredList)

        if (filteredList.isEmpty()) {
            emptyFilteredListTxt.visibility = View.VISIBLE
            recyclerViewSharedSet.visibility = View.GONE
        } else {
            emptyFilteredListTxt.visibility = View.GONE
            recyclerViewSharedSet.visibility = View.VISIBLE
        }
    }
}