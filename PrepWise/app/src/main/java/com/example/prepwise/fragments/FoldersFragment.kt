package com.example.prepwise.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prepwise.utils.DialogUtils
import com.example.prepwise.R
import com.example.prepwise.SpaceItemDecoration
import com.example.prepwise.adapters.AdapterFolder
import com.example.prepwise.models.Folder
import com.example.prepwise.utils.KeyboardUtils.hideKeyboard


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

    private lateinit var emptyFilteredListTxt: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_folders, container, false)

        val contentLayout: LinearLayout = view.findViewById(R.id.content)
        val emptyListTxt: TextView = view.findViewById(R.id.empty)
        emptyFilteredListTxt = view.findViewById(R.id.empty_filter)

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

            val searchInput: EditText = view.findViewById(R.id.input_folder_name)
            searchInput.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val query = s.toString().trim()
                    filterFolder(query)
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

    private fun filterFolder(query: String) {
        val filteredList = if (query.isEmpty()) {
            folderList  // Якщо поле пошуку порожнє, показуємо весь список
        } else {
            folderList.filter { set ->
                set.name.contains(query, ignoreCase = true)  // Фільтруємо за назвою
            }
        }

        adapterFolder?.updateData(filteredList)  // Оновлюємо дані в адаптері

        // Відображення або приховування тексту для порожнього списку
        if (filteredList.isEmpty()) {
            emptyFilteredListTxt.visibility = View.VISIBLE
            recyclerViewFolder.visibility = View.GONE
        } else {
            emptyFilteredListTxt.visibility = View.GONE
            recyclerViewFolder.visibility = View.VISIBLE
        }
    }
}
