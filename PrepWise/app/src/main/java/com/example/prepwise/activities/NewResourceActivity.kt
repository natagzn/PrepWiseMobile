package com.example.prepwise.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.prepwise.objects.DialogUtils.showSelectionPopup
import com.example.prepwise.R
import com.example.prepwise.dataClass.ResourceRequestBody
import com.example.prepwise.models.Category
import com.example.prepwise.models.Level
import com.example.prepwise.objects.KeyboardUtils.hideKeyboard
import com.example.prepwise.objects.LocaleHelper.setLocale
import com.example.prepwise.objects.RetrofitInstance
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import retrofit2.HttpException

class NewResourceActivity : AppCompatActivity() {

    private var categories = MainActivity.categories
    private val levels = MainActivity.levels
    private var category: Category? = null
    private var level: Level? = null

    private lateinit var titleTxt: TextInputEditText
    private lateinit var descriptionTxt: TextInputEditText

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
        setContentView(R.layout.activity_new_resource)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadLocale(this)

        titleTxt = findViewById(R.id.name_article_book)
        descriptionTxt = findViewById(R.id.description)

        titleTxt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard(this, currentFocus ?: View(this))
                true
            } else {
                false
            }
        }

        descriptionTxt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard(this, currentFocus ?: View(this))
                true
            } else {
                false
            }
        }

        val close: TextView = findViewById(R.id.cancel)
        close.setOnClickListener {
            finish()
        }

        val save: TextView = findViewById(R.id.save)
        save.setOnClickListener {
            val title = titleTxt.text.toString().trim()
            val description = descriptionTxt.text.toString().trim()

            if (title.isEmpty()) {
                titleTxt.error = getString(R.string.please_enter_a_name_article_book)
                return@setOnClickListener
            }
            if (description.isEmpty()) {
                descriptionTxt.error = getString(R.string.please_enter_a_description)
                return@setOnClickListener
            }
            if (level == null) {
                Toast.makeText(this, getString(R.string.please_select_a_level), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (category == null) {
                Toast.makeText(this, getString(R.string.please_select_a_category), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val requestBody = ResourceRequestBody(title, description, level!!.id, category!!.id)
                    val response = RetrofitInstance.api().createResource(requestBody)
                    if (response.isSuccessful && response.body() != null) {
                        // Handle successful response if needed
                    } else {
                        Log.e("NewResourceActivity", "Error: ${response.message()}")
                    }
                } catch (e: HttpException) {
                    Log.e("NewResourceActivity", "HttpException: ${e.message}")
                } catch (e: Exception) {
                    Log.e("NewResourceActivity", "Exception: ${e.message}")
                }
            }

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
                level = selectedLevel
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
                category = selectedCategory
            }
        }

    }
}

