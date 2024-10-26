package com.example.prepwise.activities

import android.content.Intent
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prepwise.R
import com.example.prepwise.SpaceItemDecoration
import com.example.prepwise.adapters.AdapterAddQuestion

class NewSetActivity : AppCompatActivity() {
    private lateinit var categoryListContainer: LinearLayout
    private var availableCategories = listOf("Category 1", "Category 2", "Category 3", "Category 4", "Category 5")
    private val levels = arrayOf("Trainee", "Junior", "Middle", "Senior", "Team lead")
    private val visibility = arrayOf("private", "public")
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterAddQuestion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_set)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        categoryListContainer = findViewById(R.id.category_list_container)
        addCategoryTextView("+")

        // Закриття сторінки
        val close: TextView = findViewById(R.id.cancel)
        close.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("openFragment", "HomeFragment")
            startActivity(intent)
            finish()
        }

        val levelLayout: LinearLayout = findViewById(R.id.level)
        levelLayout.setOnClickListener {
            showSelectionDialog(
                title = getString(R.string.select_level),
                items = levels,
                selectedItemTextViewId = R.id.level_type,
                dialogLayoutId = R.layout.dialog_level_selection,
                itemLayoutId = R.layout.dialog_item
            )
        }

        val visibilityLayout: LinearLayout = findViewById(R.id.visisibility)
        visibilityLayout.setOnClickListener {
            showSelectionDialog(
                title = getString(R.string.select_visibility),
                items = visibility,
                selectedItemTextViewId = R.id.visible_type,
                dialogLayoutId = R.layout.dialog_level_selection,
                itemLayoutId = R.layout.dialog_item
            )
        }

        categoryListContainer.setOnClickListener {
            showCategoryDialog()
        }

        recyclerView = findViewById(R.id.question_list)
        adapter = AdapterAddQuestion(mutableListOf(), this)

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
    }

    fun showSelectionDialog(
        title: String,
        items: Array<String>,
        selectedItemTextViewId: Int,
        dialogLayoutId: Int,
        itemLayoutId: Int
    ) {
        // Інфлейт кастомного макету діалогу
        val dialogView = layoutInflater.inflate(dialogLayoutId, null)
        val listView: ListView = dialogView.findViewById(R.id.levels_list)
        val dialogTitle: TextView = dialogView.findViewById(R.id.dialog_title)
        dialogTitle.text = title

        // Створюємо кастомний адаптер для елементів
        val adapter = object : ArrayAdapter<String>(this, itemLayoutId, items) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = convertView ?: layoutInflater.inflate(itemLayoutId, parent, false)
                val textView: TextView = view.findViewById(R.id.level_item)

                // Задаємо текст для кожного пункту
                textView.text = getItem(position)

                return view
            }
        }
        listView.adapter = adapter

        // Створюємо AlertDialog з кастомним макетом
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
            .setView(dialogView)

        val dialog = builder.create()

        // Обробка вибору елемента
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = items[position]
            val selectedTextView: TextView = findViewById(selectedItemTextViewId)
            selectedTextView.text = selectedItem
            dialog.dismiss()
        }

        dialog.show()
    }

    // Діалог вибору категорій
    fun showCategoryDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_category_selection, null)
        val categoryLayout: LinearLayout = dialogView.findViewById(R.id.category_layout)

        val checkBoxes = mutableListOf<CheckBox>()
        for (category in availableCategories) {
            val checkBox = CheckBox(ContextThemeWrapper(this, R.style.CustomCheckBoxStyle)).apply {
                text = category
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
                val selectedCategories = mutableListOf<String>()
                for (checkBox in checkBoxes) {
                    if (checkBox.isChecked) {
                        selectedCategories.add(checkBox.text.toString())
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
    fun updateCategoryList(categories: List<String>) {
        categoryListContainer.removeAllViews()

        for (category in categories) {
            addCategoryTextView(category)
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