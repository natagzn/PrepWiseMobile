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
import com.example.prepwise.adapters.AdapterResource
import com.example.prepwise.adapters.AdapterSet
import com.example.prepwise.adapters.AdapterSharedSet
import com.example.prepwise.models.Set
import com.example.prepwise.models.SharedSet

class SharedFragment : Fragment() {

    private lateinit var sharedList: ArrayList<SharedSet>
    companion object {
        private const val ARG_SHARED_LIST = "shared_list"

        fun newInstance(sharedList: ArrayList<SharedSet>): SharedFragment {
            val fragment = SharedFragment()
            val args = Bundle()
            args.putSerializable(ARG_SHARED_LIST, sharedList)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            @Suppress("UNCHECKED_CAST")
            sharedList = it.getSerializable(ARG_SHARED_LIST) as? ArrayList<SharedSet> ?: arrayListOf()
        }
    }

    private var adapterSharedSet: AdapterSharedSet? = null
    private lateinit var recyclerViewSharedSet: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_shared, container, false)

        val contentLayout: LinearLayout = view.findViewById(R.id.content)
        val emptyListTxt: TextView = view.findViewById(R.id.empty)

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

        return view
    }

}