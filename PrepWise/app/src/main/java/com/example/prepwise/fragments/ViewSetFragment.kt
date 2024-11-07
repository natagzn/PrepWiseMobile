package com.example.prepwise.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Context
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
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.PopupWindow
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prepwise.objects.DialogUtils
import com.example.prepwise.R
import com.example.prepwise.SpaceItemDecoration
import com.example.prepwise.activities.MainActivity
import com.example.prepwise.activities.MainActivity.Companion.dpToPx
import com.example.prepwise.activities.NewSetActivity
import com.example.prepwise.activities.StudyFlascardActivity
import com.example.prepwise.activities.ViewFlashcardActivity
import com.example.prepwise.adapters.AccessAdapter
import com.example.prepwise.adapters.AdapterQuestion
import com.example.prepwise.models.Folder
import com.example.prepwise.models.Question
import com.example.prepwise.models.Set
import com.example.prepwise.objects.SetRepository
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

class ViewSetFragment : Fragment() {

    private lateinit var questionList: ArrayList<Question>
    private lateinit var originalQuestionList: List<Question> // Початковий порядок
    private var setId: Int? = null
    private var set: Set? = null

    private lateinit var setName: TextView
    private lateinit var setLevel: TextView
    private lateinit var setUsername: TextView
    private lateinit var setDate: TextView
    private lateinit var setNumberOfQuestions: TextView
    private lateinit var setAccessType: TextView
    private lateinit var setAccessImg: ImageView
    private lateinit var categoriesContainer: LinearLayout
    private lateinit var setLike: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var setProgressPersent: TextView
    private lateinit var setKnow: TextView
    private lateinit var setStillLearning: TextView

    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var contentPage: LinearLayout

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
    }

    private var adapterQuestion: AdapterQuestion? = null
    private lateinit var recyclerViewQuestion: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_view_set, container, false)

        // Ініціалізація елементів макета
        loadingProgressBar = view.findViewById(R.id.loadingProgressBar) // Ініціалізація прогрес-бару
        contentPage = view.findViewById(R.id.content)

        // Робимо прогрес-бар видимим перед завантаженням
        loadingProgressBar.visibility = View.VISIBLE
        contentPage.visibility = View.GONE

        questionList = arrayListOf()
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
        setDate = view.findViewById(R.id.date)
        setStillLearning = view.findViewById(R.id.number_of_still_learning)

        setId?.let { id ->
            lifecycleScope.launch {
                // Виконуємо запит getSetById асинхронно
                set = SetRepository.getSetById(id)

                // Після отримання даних приховуємо прогрес-бар
                loadingProgressBar.visibility = View.GONE
                contentPage.visibility = View.VISIBLE

                // Після того, як запит завершився, перевіряємо, чи значення set не null
                set?.let { loadedSet ->
                    questionList.clear()
                    questionList.addAll(loadedSet.questions) // Додаємо питання до списку
                    originalQuestionList = ArrayList(questionList)

                    adapterQuestion?.notifyDataSetChanged()

                    setName.text = loadedSet.name
                    setLevel.text = loadedSet.level.name
                    setUsername.text = loadedSet.username
                    setNumberOfQuestions.text = loadedSet.questions.size.toString()
                    setAccessType.text = loadedSet.access

                    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
                    setDate.text = loadedSet.date.format(formatter)

                    if (loadedSet.access == "private") {
                        setAccessImg.setImageResource(R.drawable.resource_private)
                    } else if (loadedSet.access == "public") {
                        setAccessImg.setImageResource(R.drawable.resource_public)
                    }

                    // Очищуємо контейнер категорій перед додаванням нових категорій
                    categoriesContainer.removeAllViews()

                    // Додаємо категорії в контейнер
                    for (category in loadedSet.categories) {
                        val categoryTextView = TextView(context)
                        categoryTextView.text = category.name
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

                    if (loadedSet.isLiked) setLike.setImageResource(R.drawable.save)
                    else setLike.setImageResource(R.drawable.not_save)

                    // Прогрес
                    val progress = loadedSet.calculateProgress()
                    progressBar.progress = progress
                    setProgressPersent.text = progress.toString() +"%"
                    setKnow.text = loadedSet.getCountLearned().toString()
                    var stillLearning: Int = loadedSet.questions.size - loadedSet.getCountLearned()
                    setStillLearning.text = stillLearning.toString()

                } ?: run {
                    Log.e("ViewSetFragment", "Set not found with id: $id")
                }
            }
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

        //сортування питань
        view.findViewById<LinearLayout>(R.id.sort).setOnClickListener {
            showSortPopupMenu(requireContext(), it) { sortType ->
                sortQuestions(sortType)
            }
        }

        return view
    }

    enum class SortType {
        DEFAULT, ALPHABETICAL, BY_STATUS
    }

    fun showSortPopupMenu(
        context: Context,
        anchorView: View,
        sortCallback: (SortType) -> Unit
    ) {
        // Інфлюємо макет для PopupWindow
        val popupView = LayoutInflater.from(context).inflate(R.layout.dialog_sort_question_menu, null)
        val popupWindow = PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true)

        // Застосовуємо стиль для фону та тіні
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.white_green_rounded_background))
        popupWindow.elevation = 8f

        // Отримуємо посилання на елементи сортування
        val sortDefault = popupView.findViewById<TextView>(R.id.sort_default)
        val sortAlphabetical = popupView.findViewById<TextView>(R.id.sort_alphabetical)
        val sortStatus = popupView.findViewById<TextView>(R.id.sort_status)

        // Додаємо обробники кліків для кожного елементу сортування
        sortDefault.setOnClickListener {
            sortCallback(SortType.DEFAULT)
            popupWindow.dismiss()
        }

        sortAlphabetical.setOnClickListener {
            sortCallback(SortType.ALPHABETICAL)
            popupWindow.dismiss()
        }

        sortStatus.setOnClickListener {
            sortCallback(SortType.BY_STATUS)
            popupWindow.dismiss()
        }

        // Показуємо PopupWindow під кнопкою
        popupWindow.showAsDropDown(anchorView, 0, 10)
    }

    private fun sortQuestions(sortType: SortType) {
        when (sortType) {
            SortType.DEFAULT -> {
                // Відновлюємо початковий порядок
                questionList.clear()
                questionList.addAll(originalQuestionList)
            }
            SortType.ALPHABETICAL -> {
                // Сортування за абеткою
                questionList.sortBy { it.content }
            }
            SortType.BY_STATUS -> {
                // Розділення на два списки за статусом "Still learning" та "Already know"
                val stillLearning = questionList.filter { !it.learned }
                val alreadyKnow = questionList.filter { it.learned }
                questionList.clear()
                questionList.addAll(stillLearning + alreadyKnow)
            }
        }
        adapterQuestion?.notifyDataSetChanged() // Оновлення адаптера після сортування
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
            showSelectionDialog(getString(R.string.select_folder), MainActivity.currentUser!!.folders, dialogLayoutId = R.layout.dialog_select_selection, itemLayoutId = R.layout.dialog_item)
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
            if(MainActivity.currentUser!!.premium) showAccessDialog()
            else DialogUtils.showPremiumDialog(requireContext())
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

        val users = MainActivity.currentUser!!.friends

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

