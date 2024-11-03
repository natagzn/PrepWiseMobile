package com.example.prepwise.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prepwise.DialogUtils
import com.example.prepwise.R
import com.example.prepwise.SetListProvider
import com.example.prepwise.SpaceItemDecoration
import com.example.prepwise.adapters.AdapterSet
import com.example.prepwise.models.Set


class SetsFragment : Fragment() {

    private lateinit var setList: ArrayList<Set>

    companion object {
        private const val ARG_SET_LIST = "set_list"

        fun newInstance(setList: ArrayList<Set>): SetsFragment {
            val fragment = SetsFragment()
            val args = Bundle()
            args.putSerializable(ARG_SET_LIST, setList)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            @Suppress("UNCHECKED_CAST")
            setList = it.getSerializable(ARG_SET_LIST) as? ArrayList<Set> ?: arrayListOf()
        }
    }

    private var adapterSet: AdapterSet? = null
    private lateinit var recyclerViewSet: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sets, container, false)

        val contentLayout:LinearLayout = view.findViewById(R.id.content)
        val emptyListTxt: TextView = view.findViewById(R.id.empty)

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

        }

        return view
    }
}

