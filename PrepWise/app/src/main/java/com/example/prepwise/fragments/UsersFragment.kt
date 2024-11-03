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
import com.example.prepwise.R
import com.example.prepwise.SpaceItemDecoration
import com.example.prepwise.adapters.AdapterPeople
import com.example.prepwise.adapters.AdapterResource
import com.example.prepwise.models.People

class UsersFragment : Fragment() {

    private lateinit var userList: ArrayList<People>

    companion object {
        private const val ARG_USER_LIST = "user_list"

        fun newInstance(userList: ArrayList<People>): UsersFragment {
            val fragment = UsersFragment()
            val args = Bundle()
            args.putSerializable(ARG_USER_LIST, userList)
            fragment.arguments = args
            return fragment
        }
    }

    private var adapterUser: AdapterPeople? = null
    private lateinit var recyclerViewUser: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            @Suppress("UNCHECKED_CAST")
            userList = it.getSerializable(UsersFragment.ARG_USER_LIST) as? ArrayList<People> ?: arrayListOf()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_users, container, false)
        val emptyListTxt: TextView = view.findViewById(R.id.empty)
        recyclerViewUser = view.findViewById(R.id.user_list)

        if (userList.isEmpty()) {
            emptyListTxt.visibility = View.VISIBLE
            recyclerViewUser.visibility = View.GONE
        } else {
            emptyListTxt.visibility = View.GONE
            recyclerViewUser.visibility = View.VISIBLE

            recyclerViewUser.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapterUser = AdapterPeople(userList, requireContext(), parentFragmentManager)
            recyclerViewUser.adapter = adapterUser

            val spacingInDp = 10
            val scale = requireContext().resources.displayMetrics.density
            val spacingInPx = (spacingInDp * scale).toInt()
            recyclerViewUser.addItemDecoration(SpaceItemDecoration(spacingInPx))
        }

        return view
    }
}