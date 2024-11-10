package com.example.prepwise.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prepwise.R
import com.example.prepwise.SpaceItemDecoration
import com.example.prepwise.adapters.AdapterResource
import com.example.prepwise.adapters.AdapterSet
import com.example.prepwise.adapters.AdapterPeople
import com.example.prepwise.models.People
import com.example.prepwise.models.Resource
import com.example.prepwise.models.Set

class AllResultFragment : Fragment() {

    private lateinit var userList: ArrayList<People>
    private lateinit var setList: ArrayList<Set>
    private lateinit var resourceList: ArrayList<Resource>

    companion object {
        private const val ARG_USER_LIST = "user_list"
        private const val ARG_SET_LIST = "set_list"
        private const val ARG_RESOURCE_LIST = "resource_list"

        fun newInstance(
            userList: ArrayList<People>,
            setList: ArrayList<Set>,
            resourceList: ArrayList<Resource>
        ): AllResultFragment {
            val fragment = AllResultFragment()
            val args = Bundle()
            args.putSerializable(ARG_USER_LIST, userList)
            args.putSerializable(ARG_SET_LIST, setList)
            args.putSerializable(ARG_RESOURCE_LIST, resourceList)
            fragment.arguments = args
            return fragment
        }
    }

    private var adapterUser: AdapterPeople? = null
    private lateinit var recyclerViewUser: RecyclerView
    private var adapterSet: AdapterSet? = null
    private lateinit var recyclerViewSet: RecyclerView
    private var adapterResource: AdapterResource? = null
    private lateinit var recyclerViewResource: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userList = it.getSerializable(ARG_USER_LIST) as? ArrayList<People> ?: arrayListOf()
            setList = it.getSerializable(ARG_SET_LIST) as? ArrayList<Set> ?: arrayListOf()
            resourceList = it.getSerializable(ARG_RESOURCE_LIST) as? ArrayList<Resource> ?: arrayListOf()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_all_result, container, false)

        if(setList.isEmpty()) view.findViewById<TextView>(R.id.empty_filter_set).visibility = View.VISIBLE
        if(resourceList.isEmpty()) view.findViewById<TextView>(R.id.empty_filter_resource).visibility = View.VISIBLE
        if(userList.isEmpty()) view.findViewById<TextView>(R.id.empty_filter_user).visibility = View.VISIBLE

        // Ініціалізація user_list RecyclerView
        recyclerViewUser = view.findViewById(R.id.user_list)
        recyclerViewUser.layoutManager = LinearLayoutManager(requireContext())
        adapterUser = AdapterPeople(userList, requireContext(), parentFragmentManager)
        recyclerViewUser.adapter = adapterUser
        recyclerViewUser.isNestedScrollingEnabled = false

        // Ініціалізація set_list RecyclerView
        recyclerViewSet = view.findViewById(R.id.set_list)
        recyclerViewSet.layoutManager = LinearLayoutManager(requireContext())
        adapterSet = AdapterSet(setList, requireContext(), parentFragmentManager, "without access")
        recyclerViewSet.adapter = adapterSet
        recyclerViewSet.isNestedScrollingEnabled = false

        // Ініціалізація resource_list RecyclerView
        recyclerViewResource = view.findViewById(R.id.resource_list)
        recyclerViewResource.layoutManager = LinearLayoutManager(requireContext())
        adapterResource = AdapterResource(resourceList, requireContext())
        recyclerViewResource.adapter = adapterResource
        recyclerViewResource.isNestedScrollingEnabled = false

        // Додамо відступи між елементами RecyclerView
        val spacingInDp = 10
        val scale = requireContext().resources.displayMetrics.density
        val spacingInPx = (spacingInDp * scale).toInt()

        recyclerViewUser.addItemDecoration(SpaceItemDecoration(spacingInPx))
        recyclerViewSet.addItemDecoration(SpaceItemDecoration(spacingInPx))
        recyclerViewResource.addItemDecoration(SpaceItemDecoration(spacingInPx))

        // Знаходьте View all елементи
        val viewAllSet = view.findViewById<TextView>(R.id.view_all_set)
        val viewAllResource = view.findViewById<TextView>(R.id.view_all_resource)
        val viewAllUsers = view.findViewById<TextView>(R.id.view_all_users)

        // Налаштування обробників натискання
        viewAllSet.setOnClickListener {
            switchToTab(1)
        }

        viewAllResource.setOnClickListener {
            switchToTab(2)
        }

        viewAllUsers.setOnClickListener {
            switchToTab(3)
        }

        return view
    }

    private fun switchToTab(tabIndex: Int) {
        val fragment = SearchFragment.newInstanceWithTab(tabIndex)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
