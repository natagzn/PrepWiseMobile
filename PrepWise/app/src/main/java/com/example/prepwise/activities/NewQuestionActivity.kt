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
import com.example.prepwise.R

class NewQuestionActivity : AppCompatActivity() {
    private val setNames = arrayOf("Name1", "Name2", "Name3")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_question)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Закриття сторінки
        val close: TextView = findViewById(R.id.cancel)
        close.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("openFragment", "HomeFragment")
            startActivity(intent)
            finish()
        }

        val visibilityLayout: LinearLayout = findViewById(R.id.set)
        visibilityLayout.setOnClickListener {
            // Інфлейт кастомного макету діалогу
            val dialogView = layoutInflater.inflate(R.layout.dialog_level_selection, null)
            val listView: ListView = dialogView.findViewById(R.id.levels_list)
            val dialogTitle: TextView = dialogView.findViewById(R.id.dialog_title)
            dialogTitle.text = getString(R.string.select_set)

            // Створюємо кастомний адаптер для рівнів
            val adapter = object : ArrayAdapter<String>(this, R.layout.dialog_item, setNames) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = convertView ?: layoutInflater.inflate(R.layout.dialog_item, parent, false)
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

            // Обробка вибору рівня
            listView.setOnItemClickListener { _, _, position, _ ->
                val selectedLevel = setNames[position]
                val levelType: TextView = findViewById(R.id.set_name)
                levelType.text = selectedLevel
                dialog.dismiss()
            }

            dialog.show()
        }
    }
}