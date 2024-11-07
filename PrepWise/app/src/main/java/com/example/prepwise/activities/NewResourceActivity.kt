package com.example.prepwise.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.prepwise.objects.DialogUtils.showSelectionPopup
import com.example.prepwise.R

class NewResourceActivity : AppCompatActivity() {

    private var categories = MainActivity.categories
    private val levels = MainActivity.levels

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_resource)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val close: TextView = findViewById(R.id.cancel)
        close.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("openFragment", "HomeFragment")
            startActivity(intent)
            finish()
        }

        val levelLayout: LinearLayout = findViewById(R.id.level)
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
                // Використати обраний елемент
                val selectedLevelId = selectedLevel.id
                // Далі обробляти обраний ID або name
            }
        }


        val categoryLayout: LinearLayout = findViewById(R.id.category)
        categoryLayout.setOnClickListener {
            showSelectionPopup(
                context = this,
                anchorView = levelLayout,
                title = getString(R.string.select_category),
                items = categories,
                selectedItemTextViewId = R.id.category_type,
                dialogLayoutId = R.layout.dialog_select_selection,
                itemLayoutId = R.layout.dialog_item
            ) { selectedCategory ->
                val selectedCategoryId = selectedCategory.id
            }
        }

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
}

