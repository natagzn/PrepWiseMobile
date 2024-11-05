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
import com.example.prepwise.ResourceListProvider
import com.example.prepwise.SpaceItemDecoration
import com.example.prepwise.adapters.AdapterResource
import com.example.prepwise.adapters.AdapterSet
import com.example.prepwise.models.Folder
import com.example.prepwise.models.Resourse

class ResourcesFragment : Fragment() {

    private lateinit var resourceList: ArrayList<Resourse>
    private var selectedCategories = mutableListOf<String>()

    private lateinit var contentLayout: LinearLayout
    private lateinit var emptyListTxt: TextView
    private lateinit var emptyFilteredListTxt: TextView

    companion object {
        private const val ARG_PESOURCE_LIST = "resource_list"

        fun newInstance(resourceList: ArrayList<Resourse>): ResourcesFragment {
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
            resourceList = it.getSerializable(ARG_PESOURCE_LIST) as? ArrayList<Resourse> ?: arrayListOf()
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
                DialogUtils.showFilterResourcePopup(
                    context = requireContext(),
                    anchorView = filterBtn,
                    onApplyFilters = { selectedCategories ->
                        this.selectedCategories = selectedCategories.toMutableList()
                        applyFilters(selectedCategories)
                    },
                    currentCategories = selectedCategories
                )
            }
        }

        return view
    }

    private fun applyFilters(
        selectedCategories: List<String>
    ) {
        val filteredList = resourceList.filter { resource -> selectedCategories.isEmpty() || resource.category in selectedCategories }
        adapterResource?.updateData(filteredList)

        if (filteredList.isEmpty()) {
            emptyFilteredListTxt.visibility = View.VISIBLE
            recyclerViewResoure.visibility = View.GONE
        } else {
            emptyFilteredListTxt.visibility = View.GONE
            recyclerViewResoure.visibility = View.VISIBLE
        }
    }
}