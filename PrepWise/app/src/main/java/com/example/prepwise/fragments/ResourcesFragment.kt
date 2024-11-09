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
import com.example.prepwise.adapters.AdapterResource
import com.example.prepwise.models.Category
import com.example.prepwise.models.Level
import com.example.prepwise.models.Resource
import com.example.prepwise.objects.KeyboardUtils.hideKeyboard

class ResourcesFragment : Fragment() {

    private lateinit var resourceList: ArrayList<Resource>
    private var selectedCategories = mutableListOf<Category>()
    private var selectedLevels = mutableListOf<Level>()

    private lateinit var contentLayout: LinearLayout
    private lateinit var emptyListTxt: TextView
    private lateinit var emptyFilteredListTxt: TextView

    companion object {
        private const val ARG_PESOURCE_LIST = "resource_list"

        fun newInstance(resourceList: ArrayList<Resource>): ResourcesFragment {
            val fragment = ResourcesFragment()
            val args = Bundle()
            args.putSerializable(ARG_PESOURCE_LIST, resourceList)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            @Suppress("UNCHECKED_CAST")
            resourceList = it.getSerializable(ARG_PESOURCE_LIST) as? ArrayList<Resource> ?: arrayListOf()
        }
    }

    private var adapterResource: AdapterResource? = null
    private lateinit var recyclerViewResoure: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_resources, container, false)

        contentLayout = view.findViewById(R.id.content)
        emptyListTxt = view.findViewById(R.id.empty)
        emptyFilteredListTxt = view.findViewById(R.id.empty_filter)

        if (resourceList.isEmpty()) {
            emptyListTxt.visibility = View.VISIBLE
            contentLayout.visibility = View.GONE
        } else {
            emptyListTxt.visibility = View.GONE
            contentLayout.visibility = View.VISIBLE

            recyclerViewResoure = view.findViewById(R.id.resource_list)
            recyclerViewResoure.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapterResource = AdapterResource(resourceList, requireContext())
            recyclerViewResoure.adapter = adapterResource

            val spacingInDp = 10
            val scale = requireContext().resources.displayMetrics.density
            val spacingInPx = (spacingInDp * scale).toInt()
            recyclerViewResoure.addItemDecoration(SpaceItemDecoration(spacingInPx))

            val sortButton: LinearLayout = view.findViewById(R.id.sort_btn)
            sortButton.setOnClickListener {
                val adapter = adapterResource
                if (adapter != null) {
                    DialogUtils.showSortPopupMenu(
                        requireContext(),
                        anchorView = sortButton,
                        list = resourceList,
                        adapter = adapter,
                        getDate = { it.date },
                        getName = { it.articleBook }
                    )
                }
            }

            val filterBtn: LinearLayout = view.findViewById(R.id.filter_btn)
            filterBtn.setOnClickListener{
                var showAccess = false
                DialogUtils.showFilterPopup(
                    context = requireContext(),
                    anchorView = filterBtn,
                    onApplyFilters = { selectedCategories, selectedLevels, selectedAccesses ->
                        this.selectedCategories = selectedCategories.toMutableList()
                        this.selectedLevels = selectedLevels.toMutableList()
                        applyFilters(selectedCategories, selectedLevels)
                    },
                    accessOptions = listOf("public", "private"),
                    currentCategories = selectedCategories,
                    currentLevels = selectedLevels,
                    currentAccesses = mutableListOf<String>(),
                    showAccess
                )
            }

            val searchInput: EditText = view.findViewById(R.id.input_resource)
            searchInput.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val query = s.toString().trim()
                    filterResource(query)
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
        }

        return view
    }

    private fun filterResource(query: String) {
        val filteredList = if (query.isEmpty()) {
            resourceList  // Якщо поле пошуку порожнє, показуємо весь список
        } else {
            resourceList.filter { resource ->
                resource.articleBook.contains(query, ignoreCase = true)  // Фільтруємо за назвою
            }
        }

        adapterResource?.updateData(filteredList)  // Оновлюємо дані в адаптері

        // Відображення або приховування тексту для порожнього списку
        if (filteredList.isEmpty()) {
            emptyFilteredListTxt.visibility = View.VISIBLE
            recyclerViewResoure.visibility = View.GONE
        } else {
            emptyFilteredListTxt.visibility = View.GONE
            recyclerViewResoure.visibility = View.VISIBLE
        }
    }

    private fun applyFilters(
        selectedCategories: List<Category>, // Обрані категорії з id та name
        selectedLevels: List<Level>         // Обрані рівні
    ) {
        val filteredList = resourceList.filter { resource ->
            // Перевірка на категорії
            (selectedCategories.isEmpty() || selectedCategories.any { it.id == resource.category.id }) &&
                    // Перевірка на рівні
                    (selectedLevels.isEmpty() || selectedLevels.any { it.id == resource.level.id })
        }

        // Оновлення адаптера з фільтрованим списком
        adapterResource?.updateData(filteredList)

        // Відображення або приховування тексту для порожнього списку
        if (filteredList.isEmpty()) {
            emptyFilteredListTxt.visibility = View.VISIBLE
            recyclerViewResoure.visibility = View.GONE
        } else {
            emptyFilteredListTxt.visibility = View.GONE
            recyclerViewResoure.visibility = View.VISIBLE
        }
    }

}