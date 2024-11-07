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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prepwise.objects.DialogUtils
import com.example.prepwise.R
import com.example.prepwise.SpaceItemDecoration
import com.example.prepwise.activities.MainActivity
import com.example.prepwise.activities.NewFolderActivity
import com.example.prepwise.adapters.AdapterSetInFolder
import com.example.prepwise.models.Folder
import com.example.prepwise.models.Set
import java.time.format.DateTimeFormatter

class ViewFolderFragment : Fragment() {

    private lateinit var setList: ArrayList<Set>
    private var folderId: Int? = null
    private var folder: Folder? = null

    private lateinit var setFolderName: TextView
    private lateinit var setDate: TextView
    private lateinit var setNumberOfSet: TextView
    private lateinit var setNumberOfQuestion: TextView
    private lateinit var setLike: ImageView

    private var adapterSet: AdapterSetInFolder? = null
    private lateinit var recyclerViewSet: RecyclerView

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

        recyclerViewSet = view.findViewById(R.id.recyclerView)
        recyclerViewSet.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapterSet = AdapterSetInFolder(setList, requireContext(), parentFragmentManager)
        recyclerViewSet.adapter = adapterSet

        val spacingInDp = 15
        val scale = requireContext().resources.displayMetrics.density
        val spacingInPx = (spacingInDp * scale).toInt()
        recyclerViewSet.addItemDecoration(SpaceItemDecoration(spacingInPx))

        setFolderName = view.findViewById(R.id.folder_name)
        setNumberOfSet = view.findViewById(R.id.number_of_sets)
        setDate = view.findViewById(R.id.date)
        setNumberOfQuestion = view.findViewById(R.id.number_of_questions)
        setLike = view.findViewById(R.id.like)

        folder?.let {
            setFolderName.text = it.name
            setNumberOfSet.text = it.sets.size.toString()

            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            setDate.text = it.date.format(formatter)

            val totalQuestions = it.sets.sumOf { set -> set.questions.size }
            setNumberOfQuestion.text = totalQuestions.toString()

            if (it.isLiked) setLike.setImageResource(R.drawable.save)
            else setLike.setImageResource(R.drawable.not_save)
        }

        setLike.setOnClickListener {
            folder?.let { s ->
                s.isLiked = !s.isLiked
                setLike.setImageResource(if (s.isLiked) R.drawable.save else R.drawable.not_save)
            }
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

        return view
    }

    private fun showBottomDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_sheet_folder)

        val edit = dialog.findViewById<LinearLayout>(R.id.edit)
        val delete = dialog.findViewById<LinearLayout>(R.id.delete)
        val close: ImageView = dialog.findViewById(R.id.close)

        delete.setOnClickListener {
            dialog.dismiss()
            DialogUtils.showConfirmationDialog(
                context = requireContext(),
                message = getString(R.string.are_you_sure_you_want_to_delete_this_folder),
                positiveButtonText = getString(R.string.Delete),
                negativeButtonText = getString(R.string.cancel)
            ) { confirmed ->
                if (confirmed) {

                } else {

                }
            }

        }

        edit.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(requireActivity(), NewFolderActivity::class.java)
            intent.putExtra("mode", "edit")
            intent.putExtra("folderId", folderId)
            startActivity(intent)
        }

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