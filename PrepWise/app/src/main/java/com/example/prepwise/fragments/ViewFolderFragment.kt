package com.example.prepwise.fragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prepwise.utils.DialogUtils
import com.example.prepwise.R
import com.example.prepwise.SpaceItemDecoration
import com.example.prepwise.activities.MainActivity
import com.example.prepwise.activities.NewFolderActivity
import com.example.prepwise.adapters.AdapterSetInFolder
import com.example.prepwise.models.Folder
import com.example.prepwise.models.Set
import com.example.prepwise.repositories.FolderRepository
import com.example.prepwise.utils.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.time.format.DateTimeFormatter

class ViewFolderFragment : Fragment()
{
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

    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var contentPage: LinearLayout

    companion object {
        private const val ARG_FOLDER_ID = "folder_id"

        fun newInstance(folderId: Int): ViewFolderFragment {
            val fragment = ViewFolderFragment()
            val args = Bundle()
            args.putInt(ARG_FOLDER_ID, folderId)
            fragment.arguments = args
            return fragment
        }

        private const val EDIT_FOLDER_REQUEST_CODE = 100
    }

    private fun loadFolderData(setId: Int) {
        lifecycleScope.launch {
            folder = FolderRepository.getFolderById(setId)

            folder?.let { loadedFolder ->
                setFolderName.text = loadedFolder.name
                setNumberOfSet.text = loadedFolder.sets.size.toString()
                val totalQuestions = loadedFolder.sets.sumOf { set -> set.questions.size }
                setNumberOfQuestion.text = totalQuestions.toString()

                // Оновлення списку даних
                setList.clear()
                setList.addAll(loadedFolder.sets)

                adapterSet?.notifyDataSetChanged()
            }
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

        loadingProgressBar =
            view.findViewById(R.id.loadingProgressBar)
        contentPage = view.findViewById(R.id.content)

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
                    val customScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
                    customScope.launch {
                        try {
                            val response = RetrofitInstance.api().deleteFolder(folderId!!)
                            if (response.isSuccessful && response.body() != null) {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.folder_deleted),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            parentFragmentManager.popBackStack()
                        } catch (e: HttpException) {
                            Log.e("NewSetActivity", "HttpException: ${e.message}")
                        } catch (e: Exception) {
                            Log.e("NewSetActivity", "Exception: ${e.message}")
                        }
                    }
                }
            }
        }

        edit.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(requireActivity(), NewFolderActivity::class.java)
            intent.putExtra("mode", "edit")
            intent.putExtra("folderId", folderId)
            startActivityForResult(intent, EDIT_FOLDER_REQUEST_CODE)

        }

        close.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_FOLDER_REQUEST_CODE ) {
            folderId?.let {
                loadFolderData(it)  // Повторне завантаження оновлених даних
                adapterSet?.notifyDataSetChanged() // Оновлення відображення списку
            }
        }
    }
}