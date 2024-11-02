package com.example.prepwise.fragments

import android.app.Activity
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
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prepwise.DialogUtils
import com.example.prepwise.DialogUtils.showReportDialog
import com.example.prepwise.R
import com.example.prepwise.SpaceItemDecoration
import com.example.prepwise.activities.LoginActivity
import com.example.prepwise.activities.MainActivity
import com.example.prepwise.activities.MainActivity.Companion.dpToPx
import com.example.prepwise.activities.NewSetActivity
import com.example.prepwise.activities.PremiumActivity
import com.example.prepwise.activities.StudyFlascardActivity
import com.example.prepwise.activities.ViewFlashcardActivity
import com.example.prepwise.adapters.AccessAdapter
import com.example.prepwise.adapters.AdapterQuestion
import com.example.prepwise.models.Folder
import com.example.prepwise.models.People
import com.example.prepwise.models.Question
import com.example.prepwise.models.Set

class ViewSetFragment : Fragment() {

    private lateinit var questionList: ArrayList<Question>
    private var setId: Int? = null
    private var set: Set? = null

    private lateinit var setName: TextView
    private lateinit var setLevel: TextView
    private lateinit var setUsername: TextView
    private lateinit var setNumberOfQuestions: TextView
    private lateinit var setAccessType: TextView
    private lateinit var setAccessImg: ImageView
    private lateinit var categoriesContainer: LinearLayout
    private lateinit var setLike: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var setProgressPersent: TextView
    private lateinit var setKnow: TextView
    private lateinit var setStillLearning: TextView

    companion object {
        private const val ARG_SET_ID = "set_id"

        fun newInstance(setId: Int): ViewSetFragment {
            val fragment = ViewSetFragment()
            val args = Bundle()
            args.putInt(ARG_SET_ID, setId)
            fragment.arguments = args
            return fragment
        }
        private const val STUDY_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            setId = it.getInt(ARG_SET_ID)
        }

        setId?.let {
            set = MainActivity.getSetById(it)
            questionList = set?.questions ?: arrayListOf()
        }
    }

    private var adapterQuestion: AdapterQuestion? = null
    private lateinit var recyclerViewQuestion: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_view_set, container, false)

        recyclerViewQuestion = view.findViewById(R.id.recyclerView)
        recyclerViewQuestion.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapterQuestion = AdapterQuestion(questionList, requireContext(), childFragmentManager)
        recyclerViewQuestion.adapter = adapterQuestion

        val spacingInDp = 10
        val scale = requireContext().resources.displayMetrics.density
        val spacingInPx = (spacingInDp * scale).toInt()
        recyclerViewQuestion.addItemDecoration(SpaceItemDecoration(spacingInPx))

        setName = view.findViewById(R.id.set_name)
        setLevel = view.findViewById(R.id.level)
        setUsername = view.findViewById(R.id.username)
        setNumberOfQuestions = view.findViewById(R.id.number_of_questions)
        setAccessType = view.findViewById(R.id.access_type)
        setAccessImg = view.findViewById(R.id.access_img)
        categoriesContainer = view.findViewById(R.id.categories_container)
        setLike = view.findViewById(R.id.like)
        progressBar = view.findViewById(R.id.progressBar)
        setProgressPersent = view.findViewById(R.id.progress_persent)
        setKnow = view.findViewById(R.id.number_of_know)
        setStillLearning = view.findViewById(R.id.number_of_still_learning)

        set?.let {
            setName.text = it.name
            setLevel.text = it.level
            setUsername.text = it.username
            setNumberOfQuestions.text = it.questions.size.toString()
            setAccessType.text = it.access

            if (it.access == "private") {
                setAccessImg.setImageResource(R.drawable.resource_private)
            } else if (it.access == "public") {
                setAccessImg.setImageResource(R.drawable.resource_public)
            }

            // Очищуємо контейнер категорій перед додаванням нових категорій
            categoriesContainer.removeAllViews()

            // Додаємо категорії в контейнер
            for (category in it.categories) {
                val categoryTextView = TextView(context)
                categoryTextView.text = category
                categoryTextView.setBackgroundResource(R.drawable.blue_rounded_background)
                categoryTextView.setPadding(
                    dpToPx(10, requireContext()), dpToPx(2, requireContext()),
                    dpToPx(10, requireContext()), dpToPx(2, requireContext())
                )
                categoryTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                categoryTextView.setTextAppearance(R.style.medium_11)

                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.setMargins(dpToPx(10, requireContext()), 0, 0, 0)
                categoryTextView.layoutParams = layoutParams

                categoriesContainer.addView(categoryTextView)
            }

            if (it.isLiked) setLike.setImageResource(R.drawable.save)
            else setLike.setImageResource(R.drawable.not_save)

            // Прогрес
            val progress = it.calculateProgress()
            progressBar.progress = progress
            setProgressPersent.text = progress.toString() +"%"
            setKnow.text = it.getCountLearned().toString()
            var stillLearning: Int = it.questions.size - it.getCountLearned()
            setStillLearning.text = stillLearning.toString()
        }

        val backButton: ImageView = view.findViewById(R.id.back)
        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        setLike.setOnClickListener {
            set?.let { s ->
                s.isLiked = !s.isLiked
                setLike.setImageResource(if (s.isLiked) R.drawable.save else R.drawable.not_save)
            }
        }

        // Меню роботи з сетом
        val setMenu: ImageView = view.findViewById(R.id.more)
        setMenu.setOnClickListener{
            showBottomDialog()
        }

        // Відкриття сторінки вчивчення сета
        val studyFlashcards: LinearLayout = view.findViewById(R.id.study_flashcards)
        studyFlashcards.setOnClickListener {
            if (set != null && set!!.questions.any { !it.learned }) {
                val intent = Intent(requireActivity(), StudyFlascardActivity::class.java)
                intent.putExtra("setId", setId)
                startActivityForResult(intent, STUDY_REQUEST_CODE)
            } else {
                val dialog = AlertDialog.Builder(requireContext())
                    .setTitle("Увага")
                    .setMessage("Всі питання в цьому сеті вже вивчені!")
                    .setPositiveButton("ОК") { dialog, _ -> dialog.dismiss() }
                    .create()

                dialog.show()
            }
        }

        // Відкриття сторінки перегляду питань сета
        val viewFlashcards: LinearLayout = view.findViewById(R.id.view_flashcards)
        viewFlashcards.setOnClickListener{
            val intent = Intent(requireActivity(), ViewFlashcardActivity::class.java)
            intent.putExtra("setId", setId)
            startActivity(intent)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == STUDY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            updateUI()
        }
    }

    private fun updateUI() {
        set?.let {
            val progress = it.calculateProgress()
            progressBar.progress = progress
            setProgressPersent.text = "$progress%"
            setKnow.text = it.getCountLearned().toString()
            val stillLearning = it.questions.size - it.getCountLearned()
            setStillLearning.text = stillLearning.toString()

            adapterQuestion?.notifyDataSetChanged()
        }
    }

    private fun showBottomDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_sheet_set)

        val edit = dialog.findViewById<LinearLayout>(R.id.edit)
        val resetProgress = dialog.findViewById<LinearLayout>(R.id.reset_progress)
        val addToFolder = dialog.findViewById<LinearLayout>(R.id.add_to_folder)
        val exportToPdf = dialog.findViewById<LinearLayout>(R.id.export_to_pdf)
        val share = dialog.findViewById<LinearLayout>(R.id.share)
        val report = dialog.findViewById<LinearLayout>(R.id.report)
        val delete = dialog.findViewById<LinearLayout>(R.id.delete)
        val close: ImageView = dialog.findViewById(R.id.close)

        edit.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(requireActivity(), NewSetActivity::class.java)
            intent.putExtra("mode", "edit")
            intent.putExtra("setId", setId)
            startActivity(intent)
        }

        resetProgress.setOnClickListener {
            dialog.dismiss()
            DialogUtils.showConfirmationDialog(
                context = requireContext(),
                message = getString(R.string.are_you_sure_you_want_to_reset_progress),
                positiveButtonText = getString(R.string.Reset_progress),
                negativeButtonText = getString(R.string.cancel)
            ) { confirmed ->
                set?.let { s ->
                    for(question in s.questions){
                        question.learned=false
                    }
                    updateUI()
                }
            }
        }

        report.setOnClickListener{
            dialog.dismiss()
            DialogUtils.showReportDialog(requireContext(), getString(R.string.report_this_set)) { selectedReason ->
                Toast.makeText(requireContext(), getString(R.string.report_sent_successfully_thank_you_for_your_help), Toast.LENGTH_SHORT).show()
            }
        }

        addToFolder.setOnClickListener {
            dialog.dismiss()
            showSelectionDialog(getString(R.string.select_folder), MainActivity.folderList, dialogLayoutId = R.layout.dialog_level_selection, itemLayoutId = R.layout.dialog_item)
        }

        delete.setOnClickListener{
            dialog.dismiss()
            DialogUtils.showConfirmationDialog(
                context = requireContext(),
                message = getString(R.string.are_you_sure_you_want_to_delete_this_set),
                positiveButtonText = getString(R.string.Delete),
                negativeButtonText = getString(R.string.cancel)
            ) { confirmed ->
                if (confirmed) {

                } else {

                }
            }

        }

        share.setOnClickListener{
            showAccessDialog()
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

    private fun showAccessDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_share_access, null)
        val recyclerViewUsers: RecyclerView = dialogView.findViewById(R.id.recycler_view_users)
        val confirmButton: TextView = dialogView.findViewById(R.id.confirm_button)
        val cancelButton: TextView = dialogView.findViewById(R.id.cancel_button)

        val users = arrayListOf(
            People(
                userImg = "img_anna",
                username = "Anna",
                status = "active",
                numberOfFollowing = 150,
                numberOfFollowers = 300,
                description = "Loves teaching math",
                email = "anna@example.com",
                location = "Kyiv, Ukraine",
                sets = arrayListOf(),
                resouces = arrayListOf()
            ),
            People(
                userImg = "img_john",
                username = "John",
                status = "active",
                numberOfFollowing = 200,
                numberOfFollowers = 500,
                description = "History enthusiast",
                email = "john@example.com",
                location = "Lviv, Ukraine",
                sets = arrayListOf(),
                resouces = arrayListOf()
            ),
            People(
                userImg = "img_nina",
                username = "Nina",
                status = "active",
                numberOfFollowing = 180,
                numberOfFollowers = 320,
                description = "Biology lover",
                email = "nina@example.com",
                location = "Odesa, Ukraine",
                sets = arrayListOf(),
                resouces = arrayListOf()
            )
        )

        val accessMap = mutableMapOf<String, String>()

        val accessAdapter = AccessAdapter(users, requireContext()) { user, access ->
            accessMap[user] = access
        }
        recyclerViewUsers.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewUsers.adapter = accessAdapter

        val spacingInDp = 10
        val scale = requireContext().resources.displayMetrics.density
        val spacingInPx = (spacingInDp * scale).toInt()
        recyclerViewUsers.addItemDecoration(SpaceItemDecoration(spacingInPx))

        val dialog = AlertDialog.Builder(requireContext(), R.style.RoundedDialogTheme)
            .setView(dialogView)
            .create()

        confirmButton.setOnClickListener {

            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


    fun showSelectionDialog(
        title: String,
        items: List<Folder>,
        dialogLayoutId: Int,
        itemLayoutId: Int
    ) {
        val dialogView = layoutInflater.inflate(dialogLayoutId, null)
        val listView: ListView = dialogView.findViewById(R.id.levels_list)
        val dialogTitle: TextView = dialogView.findViewById(R.id.dialog_title)
        dialogTitle.text = title

        // Створюємо кастомний адаптер для елементів
        val adapter = object : ArrayAdapter<Folder>(requireContext(), itemLayoutId, items) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = convertView ?: layoutInflater.inflate(itemLayoutId, parent, false)
                val textView: TextView = view.findViewById(R.id.level_item)
                textView.text = getItem(position)?.name  // Відображаємо назву папки
                return view
            }
        }
        listView.adapter = adapter

        // Створюємо AlertDialog з кастомним макетом
        val builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
            .setView(dialogView)

        val dialog = builder.create()

        // Обробка вибору елемента
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedFolder = items[position]
            val selectedFolderId = selectedFolder.id
            saveSelectedFolderId(selectedFolderId)

            dialog.dismiss()
        }

        dialog.show()
    }

    // Функція для збереження ID вибраної папки
    private fun saveSelectedFolderId(folderId: Int) {

    }

}

