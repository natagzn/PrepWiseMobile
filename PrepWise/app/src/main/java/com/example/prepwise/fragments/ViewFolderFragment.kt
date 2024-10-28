package com.example.prepwise.fragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.prepwise.DialogUtils
import com.example.prepwise.R
import com.example.prepwise.activities.MainActivity
import com.example.prepwise.activities.MainActivity.Companion.dpToPx
import com.example.prepwise.activities.NewFolderActivity
import com.example.prepwise.models.Folder
import com.example.prepwise.models.Set

class ViewFolderFragment : Fragment() {

    private lateinit var setList: ArrayList<Set>
    private var folderId: Int? = null
    private var folder: Folder? = null

    private lateinit var setFolderName: TextView
    private lateinit var setNumberOfSet: TextView
    private lateinit var setLike: ImageView

    companion object {
        private const val ARG_FOLDER_ID = "folder_id"

        fun newInstance(folderId: Int): ViewFolderFragment {
            val fragment = ViewFolderFragment()
            val args = Bundle()
            args.putInt(ARG_FOLDER_ID, folderId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            folderId = it.getInt(ARG_FOLDER_ID)
        }

        folderId?.let {
            folder = MainActivity.getFolderById(it)
            setList = folder?.sets ?: arrayListOf()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_view_folder, container, false)

        setFolderName = view.findViewById(R.id.folder_name)
        setNumberOfSet = view.findViewById(R.id.number_of_sets)
        setLike = view.findViewById(R.id.like)

        folder?.let {
            setFolderName.text = it.name
            setNumberOfSet.text = it.sets.size.toString()
        }

        // Меню роботи з сетом
        val setMenu: ImageView = view.findViewById(R.id.more)
        setMenu.setOnClickListener{
            showBottomDialog()
        }

        // назад
        val backButton: ImageView = view.findViewById(R.id.back)
        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }

    private fun showBottomDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_sheet_folder)

        val edit = dialog.findViewById<LinearLayout>(R.id.edit)
        val delete = dialog.findViewById<LinearLayout>(R.id.delete)
        val close: ImageView = dialog.findViewById(R.id.close)



        close.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
    }
}