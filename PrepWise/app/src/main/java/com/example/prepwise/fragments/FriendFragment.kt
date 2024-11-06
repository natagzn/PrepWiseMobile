package com.example.prepwise.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prepwise.R
import com.example.prepwise.SpaceItemDecoration
import com.example.prepwise.adapters.AdapterFolder
import com.example.prepwise.adapters.AdapterFriend
import com.example.prepwise.models.People

class FriendFragment : Fragment() {

    private lateinit var friendList: ArrayList<People>

    companion object {
        private const val ARG_LIST = "friend_list"

        fun newInstance(list: ArrayList<People>): FriendFragment {
            val fragment = FriendFragment()
            val args = Bundle()
            args.putSerializable(ARG_LIST, list)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            @Suppress("UNCHECKED_CAST")
            friendList = it.getSerializable(ARG_LIST) as? ArrayList<People> ?: arrayListOf()
        }
    }

    private var adapterFriend: AdapterFriend? = null
    private lateinit var recyclerViewFriend: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_friend, container, false)

        recyclerViewFriend = view.findViewById(R.id.people_list)
        recyclerViewFriend.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapterFriend = AdapterFriend(friendList, requireContext(), requireActivity().supportFragmentManager)
        recyclerViewFriend.adapter = adapterFriend

        val spacingInDp = 8
        val scale = requireContext().resources.displayMetrics.density
        val spacingInPx = (spacingInDp * scale).toInt()
        recyclerViewFriend.addItemDecoration(SpaceItemDecoration(spacingInPx))

        return view
    }
}
