package com.example.prepwise.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prepwise.objects.DialogUtils.showSelectionPopup
import com.example.prepwise.R
import com.example.prepwise.SpaceItemDecoration
import com.example.prepwise.adapters.AdapterAddQuestion
import com.example.prepwise.dataClass.QuestionRequestBody
import com.example.prepwise.dataClass.SetRequestBody
import com.example.prepwise.dataClass.UpdateSetRequest
import com.example.prepwise.models.Category
import com.example.prepwise.models.Level
import com.example.prepwise.models.Question
import com.example.prepwise.objects.LocaleHelper.setLocale
import com.example.prepwise.objects.RetrofitInstance
import com.example.prepwise.objects.SetRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import retrofit2.HttpException

class NewSetActivity : AppCompatActivity() {
    private lateinit var categoryListContainer: LinearLayout
    private var availableCategories = MainActivity.categories
    private var selectedCategories = mutableListOf<Category>()
    private val levels = MainActivity.levels
    private val visibility = arrayOf("private", "public")
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterAddQuestion
    private lateinit var titleTxt: TextView
    private lateinit var accessLayout: LinearLayout
    private lateinit var accessTxt: TextView
    private lateinit var levelLayout: LinearLayout
    private lateinit var levelTxt: TextView

    private var level: Level? = null

    private lateinit var originalTitle:String
    private var originalAccess:Boolean = false
    private lateinit var originalLevel:Level
    private var originalCategories = mutableListOf<Category>()
    var originalQuestions = listOf<Question>()

    fun loadLocale(context: Context) {
        val sharedPref = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val language = sharedPref.getString("My_lang", "")
        if (!language.isNullOrEmpty()) {
            setLocale(language, context)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_set)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadLocale(this)

        titleTxt = findViewById(R.id.title_set)
        accessLayout = findViewById(R.id.access)
        accessTxt = findViewById(R.id.access_type)
        levelLayout = findViewById(R.id.level)
        levelTxt = findViewById(R.id.level_type)
        categoryListContainer = findViewById(R.id.category_list_container)
        recyclerView = findViewById(R.id.question_list)
        adapter = AdapterAddQuestion(mutableListOf(), this)

        val mode = intent.getStringExtra("mode") ?: "create"
        val setId = intent.getIntExtra("setId", -1)

        if (mode == "edit" && setId != -1) {
            val customScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
            customScope.launch {
                loadDataForEditing(setId)
            }
            findViewById<TextView>(R.id.mode).text = getString(R.string.edit_set)
        }else addCategoryTextView("+")

        // Закриття сторінки
        val close: TextView = findViewById(R.id.cancel)
        close.setOnClickListener {
            finish()
        }

        levelLayout.setOnClickListener {
            showSelectionPopup(
                context = this,
                anchorView = levelLayout,
                title = getString(R.string.select_level),
                items = levels,
                selectedItemTextViewId = R.id.level_type,
                dialogLayoutId = R.layout.dialog_select_selection,
                itemLayoutId = R.layout.dialog_item
            ) { selectedLevel ->
                level = selectedLevel
            }
        }

        accessLayout.setOnClickListener {
            showSelectionAccessPopup(
                context = this,
                anchorView = accessLayout,
                title = getString(R.string.select_visibility),
                items = visibility,
                selectedItemTextViewId = R.id.access_type,
                dialogLayoutId = R.layout.dialog_select_selection,
                itemLayoutId = R.layout.dialog_item
            )
        }

        categoryListContainer.setOnClickListener {
            showCategoryDialog()
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        adapter.addQuestion()

        findViewById<ImageView>(R.id.add_btn).setOnClickListener {
            adapter.addQuestion()
        }

        val spacingInDp = 10
        val scale = this.resources.displayMetrics.density
        val spacingInPx = (spacingInDp * scale).toInt()
        recyclerView.addItemDecoration(SpaceItemDecoration(spacingInPx))

        // Обробка натискання кнопки "Зберегти"
        findViewById<TextView>(R.id.save).setOnClickListener {
            val title = titleTxt.text.toString()
            val accessStr = accessTxt.text.toString()
            val access = accessStr == "public"
            val categoriesId = selectedCategories.map { it.id }

            // сворення нового сета
            if (mode == "create") {
                // Перевірка заповнення полів
                if (title.isEmpty()) {
                    titleTxt.error = getString(R.string.please_enter_a_title)
                    return@setOnClickListener
                }
                if (level == null) {
                    Toast.makeText(
                        this,
                        getString(R.string.please_select_a_level),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                if (categoriesId.isEmpty()) {
                    Toast.makeText(
                        this,
                        getString(R.string.please_select_at_least_one_category),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                if (accessStr.isEmpty()) {
                    Toast.makeText(
                        this,
                        getString(R.string.please_select_access_type),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                val customScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
                customScope.launch {
                    try {
                        val requestBody = SetRequestBody(title, access, level!!.id, categoriesId)
                        val response = RetrofitInstance.api().createSet(requestBody)
                        if (response.isSuccessful && response.body() != null) {
                            val setId = response.body()!!.set.question_set_id
                            Toast.makeText(
                                this@NewSetActivity,
                                "Set created successfully",
                                Toast.LENGTH_SHORT
                            ).show()

                            // Додаємо питання до новоствореного сету
                            for (question in adapter.questions) {
                                // Перевірка, щоб уникнути додавання пустих питань
                                if (question.content.isEmpty() && question.answer.isEmpty()) {
                                    continue // Пропустити це питання, якщо content і answer порожні
                                }

                                val questionRequestBody = QuestionRequestBody(
                                    list_id = setId,
                                    status = false.toString(),
                                    content = question.content,
                                    answer = question.answer
                                )

                                // Відправляємо запит для створення кожного питання
                                lifecycleScope.launch {
                                    try {
                                        val questionResponse = RetrofitInstance.api()
                                            .createQuestion(questionRequestBody)
                                        if (!questionResponse.isSuccessful) {
                                            Log.e(
                                                "NewSetActivity",
                                                "Error creating question: ${questionResponse.message()}"
                                            )
                                        }
                                    } catch (e: HttpException) {
                                        Log.e("NewSetActivity", "HttpException: ${e.message}")
                                    } catch (e: Exception) {
                                        Log.e("NewSetActivity", "Exception: ${e.message}")
                                    }
                                }
                            }
                            finish()
                        } else {
                            Log.e("NewSetActivity", "Error: ${response.message()}")
                        }
                    } catch (e: HttpException) {
                        Log.e("NewSetActivity", "HttpException: ${e.message}")
                    } catch (e: Exception) {
                        Log.e("NewSetActivity", "Exception: ${e.message}")
                    }
                }
            }
            else if (mode == "edit" && setId != -1) {
                val newTitle = titleTxt.text.toString()
                val newAccess = accessStr == "public"
                val newCategories = selectedCategories.map { it.id }
                val originalCategoryIds = originalCategories.map { it.id }

                val updateRequest = UpdateSetRequest(
                    name = if (newTitle != originalTitle) newTitle else null,
                    access = if (newAccess != originalAccess) newAccess.toString() else originalAccess.toString(),
                    level_id = if (level != null && level != originalLevel) level!!.id else null,
                    categories = null // Категорії будемо оновлювати окремо
                )

                val customScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
                customScope.launch {
                    try {
                        val response = RetrofitInstance.api().updateSet(setId, updateRequest)

                        if (response.isSuccessful) {

                            for (categoryId in originalCategoryIds) {
                                if (categoryId !in newCategories) {
                                    val deleteResponse = RetrofitInstance.api()
                                        .deleteCategoryFromSet(setId, categoryId)
                                    if (!deleteResponse.isSuccessful) {
                                        val errorBody = deleteResponse.errorBody()?.string()
                                        when (deleteResponse.code()) {
                                            404 -> Log.e(
                                                "NewSetActivity",
                                                "Category $categoryId not found. Error body: $errorBody"
                                            )

                                            403 -> Log.e(
                                                "NewSetActivity",
                                                "Access denied when deleting category $categoryId. Error body: $errorBody"
                                            )

                                            else -> Log.e(
                                                "NewSetActivity",
                                                "Failed to delete category $categoryId: ${deleteResponse.message()}, error body: $errorBody"
                                            )
                                        }
                                    }
                                }
                            }

                            for (categoryId in newCategories) {
                                if (categoryId !in originalCategoryIds) {
                                    val addResponse =
                                        RetrofitInstance.api().addCategoryToSet(setId, categoryId)
                                    if (!addResponse.isSuccessful) {
                                        Log.e(
                                            "NewSetActivity",
                                            "Failed to add category $categoryId: ${addResponse.message()}"
                                        )
                                    }
                                }
                            }

                            finish()
                        } else {
                            Log.e("NewSetActivity", "Error updating set: ${response.message()}")
                        }
                    } catch (e: Exception) {
                        Log.e("NewSetActivity", "Exception: ${e.message}")
                    }
                }

                val updatedQuestions = adapter.questions
                val questionsToCreate = updatedQuestions.filter { it.question_id == -1 && it.content.isNotEmpty() }
                val questionsToUpdate = adapter.questionsToUpdate
                val questionsToDelete = adapter.questionsToDelete

                // Ініціювання корутини для роботи з питаннями
                val customScopeQue = CoroutineScope(SupervisorJob() + Dispatchers.Main)
                customScopeQue.launch {
                    try {
                        // Видалення питань
                        questionsToDelete.forEach { question ->
                            val deleteResponse = RetrofitInstance.api().deleteQuestion(question.question_id)
                            if (!deleteResponse.isSuccessful) {
                                Log.e(
                                    "NewSetActivity",
                                    "Не вдалося видалити питання ${question.question_id}: ${deleteResponse.message()}"
                                )
                            }
                        }

                        // Оновлення питань
                        questionsToUpdate.forEach { question ->
                            val updateRequestBody = QuestionRequestBody(
                                list_id = setId,
                                status = question.learned.toString(),
                                content = question.content,
                                answer = question.answer
                            )
                            val updateResponse = RetrofitInstance.api()
                                .updateQuestion(question.question_id, updateRequestBody)
                            if (!updateResponse.isSuccessful) {
                                Log.e(
                                    "NewSetActivity",
                                    "Не вдалося оновити питання ${question.question_id}: ${updateResponse.message()}"
                                )
                            }
                        }

                        // Створення нових питань
                        questionsToCreate.forEach { question ->
                            val createRequestBody = QuestionRequestBody(
                                list_id = setId,
                                status = question.learned.toString(),
                                content = question.content,
                                answer = question.answer
                            )
                            val createResponse =
                                RetrofitInstance.api().createQuestion(createRequestBody)
                            if (!createResponse.isSuccessful) {
                                Log.e(
                                    "NewSetActivity",
                                    "Не вдалося створити питання: ${createResponse.message()}"
                                )
                            }
                        }

                        finish()

                    } catch (e: Exception) {
                        Log.e("NewSetActivity", "Виняток: ${e.message}")
                    }
                }
            }
        }
    }

    fun showSelectionAccessPopup(
        context: Context,
        anchorView: View,
        title: String,
        items: Array<String>,
        selectedItemTextViewId: Int,
        dialogLayoutId: Int,
        itemLayoutId: Int
    ) {
        // Інфлейт кастомного макету діалогу
        val popupView = LayoutInflater.from(context).inflate(dialogLayoutId, null)
        val listView: ListView = popupView.findViewById(R.id.levels_list)
        val dialogTitle: TextView = popupView.findViewById(R.id.dialog_title)
        dialogTitle.text = title

        // Створюємо адаптер для елементів
        val adapter = object : ArrayAdapter<String>(context, itemLayoutId, items) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = convertView ?: LayoutInflater.from(parent.context).inflate(itemLayoutId, parent, false)
                val textView: TextView = view.findViewById(R.id.level_item)
                textView.text = getItem(position)
                return view
            }
        }
        listView.adapter = adapter

        // Створюємо PopupWindow
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.white_green_rounded_background))
        popupWindow.elevation = 8f

        val dimBackground = (context as Activity).findViewById<View>(R.id.dim_background)
        dimBackground.visibility = View.VISIBLE

        popupWindow.setOnDismissListener {
            dimBackground.visibility = View.GONE
        }

        // Обробка вибору елемента
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = items[position]
            val selectedTextView: TextView = (context as Activity).findViewById(selectedItemTextViewId)
            selectedTextView.text = selectedItem
            popupWindow.dismiss()
        }

        // Відображення PopupWindow біля anchorView
        popupWindow.showAsDropDown(anchorView, 0, 10)
    }

    private suspend fun loadDataForEditing(setId: Int) {
        val setData = SetRepository.getSetById(setId)

        if (setData != null) {
            titleTxt.text = setData.name
            levelTxt.text = setData.level.name
            accessTxt.text = setData.access
            selectedCategories = setData.categories.toMutableList()
            updateCategoryList(setData.categories)

            originalTitle = setData.name
            originalLevel = setData.level
            originalAccess = setData.access == "public"
            originalCategories = setData.categories.toMutableList()
            originalQuestions = setData.questions.toMutableList()

            adapter.updateQuestions(setData.questions)
        }
    }

    // Діалог вибору категорій
    fun showCategoryDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_category_selection, null)
        val categoryLayout: LinearLayout = dialogView.findViewById(R.id.category_layout)

        val checkBoxes = mutableListOf<CheckBox>()
        for (category in availableCategories) {
            val checkBox = CheckBox(ContextThemeWrapper(this, R.style.CustomCheckBoxStyle)).apply {
                text = category.name
                isChecked = selectedCategories.contains(category)
            }
            categoryLayout.addView(checkBox)
            checkBoxes.add(checkBox)
        }

        val titleView = TextView(this).apply {
            text = getString(R.string.select_category)
            textSize = 18f
            setTextColor(ContextCompat.getColor(context, R.color.black))
            typeface = ResourcesCompat.getFont(context, R.font.regular)
            setPadding(40, 40, 0, 0)
        }

        var currCategories = mutableListOf<Category>()

        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
            .setCustomTitle(titleView)
            .setView(dialogView)
            .setPositiveButton(getString(R.string.apply)) { dialog, _ ->
                for ((index, checkBox) in checkBoxes.withIndex()) {
                    if (checkBox.isChecked) {
                        currCategories.add(availableCategories[index])
                    }
                }

                if (currCategories.size <= 3) {
                    selectedCategories.clear()
                    selectedCategories.addAll(currCategories)
                    updateCategoryList(selectedCategories)
                } else {
                    Toast.makeText(this, getString(R.string.select_up_to_3_categories), Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }

        val dialog = builder.create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.let { positiveButton ->
                positiveButton.setTextColor(ContextCompat.getColor(this, R.color.white))
                positiveButton.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
                positiveButton.typeface = ResourcesCompat.getFont(this, R.font.regular)
            }

            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.let { negativeButton ->
                negativeButton.setTextColor(ContextCompat.getColor(this, R.color.black))
                negativeButton.setBackgroundResource(R.drawable.white_green_rounded_background)
                negativeButton.typeface = ResourcesCompat.getFont(this, R.font.regular)
            }
        }

        dialog.show()
    }

    // Оновлення списку вибраних категорій
    fun updateCategoryList(categories: List<Category>) {
        categoryListContainer.removeAllViews()

        for (category in categories) {
            addCategoryTextView(category.name)
        }
        addCategoryTextView("+")
    }

    // Додавання категорії до списку обраних
    private fun addCategoryTextView(category: String) {
        val textView = TextView(this).apply {
            text = category
            setTextColor(ContextCompat.getColor(context, R.color.black))
            typeface = ResourcesCompat.getFont(context, R.font.semi_bold)
            textSize = 16f

            background = ContextCompat.getDrawable(context, R.drawable.gray_rounded_stroke)

            height = (28 * resources.displayMetrics.density).toInt()
            setPadding(
                (10 * resources.displayMetrics.density).toInt(),
                (5 * resources.displayMetrics.density).toInt(),
                (10 * resources.displayMetrics.density).toInt(),
                (5 * resources.displayMetrics.density).toInt()
            )
        }

        val marginInDp = 5
        val marginInPx = (marginInDp * resources.displayMetrics.density).toInt()

        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(0, 0, marginInPx, 0)
        textView.layoutParams = layoutParams

        categoryListContainer.addView(textView)
    }
}