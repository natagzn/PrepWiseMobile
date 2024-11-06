package com.example.prepwise.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prepwise.R
import com.example.prepwise.SpaceItemDecoration
import com.example.prepwise.activities.MainActivity
import com.example.prepwise.adapters.AdapterFriendHelp
import com.example.prepwise.models.People

class FriendHelpDialog : DialogFragment() {

    val peopleList = MainActivity.currentUser!!.friends

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_friend_help, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_friends)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = AdapterFriendHelp(peopleList, requireContext())

        val spacingInDp = 10
        val scale = requireContext().resources.displayMetrics.density
        val spacingInPx = (spacingInDp * scale).toInt()
        recyclerView.addItemDecoration(SpaceItemDecoration(spacingInPx))

        val close: ImageView = view.findViewById(R.id.close_button)
        close.setOnClickListener{dismiss()}

        return view
    }

    override fun onStart() {
        super.onStart()

        val width = (resources.displayMetrics.widthPixels * 0.9).toInt()
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}