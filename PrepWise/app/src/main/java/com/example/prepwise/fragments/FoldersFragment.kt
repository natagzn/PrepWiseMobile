package com.example.prepwise.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prepwise.objects.DialogUtils
import com.example.prepwise.R
import com.example.prepwise.SpaceItemDecoration
import com.example.prepwise.adapters.AdapterFolder
import com.example.prepwise.models.Folder


class FoldersFragment : Fragment() {

    private lateinit var folderList: ArrayList<Folder>

    companion object {
        private const val ARG_FOLDER_LIST = "folder_list"

        fun newInstance(folderList: ArrayList<Folder>): FoldersFragment {
            val fragment = FoldersFragment()
            val args = Bundle()
            args.putSerializable(ARG_FOLDER_LIST, folderList)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            @Suppress("UNCHECKED_CAST")
            folderList = it.getSerializable(ARG_FOLDER_LIST) as? ArrayList<Folder> ?: arrayListOf()
        }
    }

    private var adapterFolder: AdapterFolder? = null
    private lateinit var recyclerViewFolder: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_folders, container, false)

        val contentLayout: LinearLayout = view.findViewById(R.id.content)
        val emptyListTxt: TextView = view.findViewById(R.id.empty)

        if (folderList.isEmpty()) {
            emptyListTxt.visibility = View.VISIBLE
            contentLayout.visibility = View.GONE
        } else {
            emptyListTxt.visibility = View.GONE
            contentLayout.visibility = View.VISIBLE

            recyclerViewFolder = view.findViewById(R.id.folder_list)
            recyclerViewFolder.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapterFolder = AdapterFolder(folderList, requireContext(), parentFragmentManager)
            recyclerViewFolder.adapter = adapterFolder

            val spacingInDp = 8
            val scale = requireContext().resources.displayMetrics.density
            val spacingInPx = (spacingInDp * scale).toInt()
            recyclerViewFolder.addItemDecoration(SpaceItemDecoration(spacingInPx))

            val sortButton: LinearLayout = view.findViewById(R.id.sort_btn)

            sortButton.setOnClickListener {
                val adapter = adapterFolder
                if (adapter != null) {
                    DialogUtils.showSortPopupMenu(
                        requireContext(),
                        anchorView = sortButton,
                        list = folderList,
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
