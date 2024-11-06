package com.example.prepwise.activities

import android.app.Activity
import android.content.Context
import android.os.Bundle
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
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prepwise.DialogUtils
import com.example.prepwise.DialogUtils.showSelectionPopup
import com.example.prepwise.R
import com.example.prepwise.SpaceItemDecoration
import com.example.prepwise.adapters.AdapterAddQuestion
import com.example.prepwise.models.Category
import com.example.prepwise.models.Set
import java.time.LocalDate

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_set)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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
            loadDataForEditing(setId)
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
                val selectedLevelId = selectedLevel.id
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
            //saveNewSet()
        }
    }

    /*private fun saveNewSet() {
        // Отримати назву
        val title = titleTxt.text.toString()

        // Отримати рівень
        val level = levels.find { it.name == levelTxt.text.toString() }

        // Отримати доступ
        val access = accessTxt.text.toString()

        // Отримати список питань
        val questions = adapter.getQuestions()

        // Перевірка, що всі обов'язкові поля заповнені
        if (title.isBlank() || level == null || access.isBlank()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Створення нового об'єкта Set
        val newSet = Set(
            id = 0,
            name = title,
            level = level,
            categories = ArrayList(selectedCategories),
            access = access,
            date = LocalDate.now(),
            questions = ArrayList(questions),
            username = MainActivity.currentUser?.username ?: "Unknown",
            isLiked = false
        )

        // Додавання нового сету до списку користувача або іншої колекції
        MainActivity.currentUser?.sets?.add(newSet)

        // Повернення до попереднього екрану
        Toast.makeText(this, "Set saved successfully", Toast.LENGTH_SHORT).show()
        finish()
    }*/

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

    private fun loadDataForEditing(setId: Int) {
        val setData = MainActivity.getSetById(setId)

        if (setData != null) {
            titleTxt.text = setData.name
            levelTxt.text = setData.level.name
            accessTxt.text = setData.access
            selectedCategories = setData.categories
            updateCategoryList(setData.categories)

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

        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
            .setCustomTitle(titleView)
            .setView(dialogView)
            .setPositiveButton(getString(R.string.apply)) { dialog, _ ->
                selectedCategories.clear()
                for ((index, checkBox) in checkBoxes.withIndex()) {
                    if (checkBox.isChecked) {
                        selectedCategories.add(availableCategories[index])
                    }
                }

                if (selectedCategories.size <= 3) {
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