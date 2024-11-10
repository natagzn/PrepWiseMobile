package com.example.prepwise.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.prepwise.R
import com.example.prepwise.activities.MainActivity.Companion.currentUser
import com.example.prepwise.dataClass.QuestionRequestBody
import com.example.prepwise.models.Set
import com.example.prepwise.utils.KeyboardUtils.hideKeyboard
import com.example.prepwise.utils.LocaleHelper.setLocale
import com.example.prepwise.utils.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.HttpException

class NewQuestionActivity : AppCompatActivity() {
    private val sets = currentUser.sets
    private var setId: Int? = null
    private lateinit var questionTxt: EditText
    private lateinit var answerTxt: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_question)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        loadLocale(this)

        questionTxt = findViewById(R.id.question)
        answerTxt = findViewById(R.id.answer)

        questionTxt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard(this, currentFocus ?: View(this))
                true
            } else {
                false
            }
        }

        answerTxt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard(this, currentFocus ?: View(this))
                true
            } else {
                false
            }
        }

        // Закриття сторінки
        val close: TextView = findViewById(R.id.cancel)
        close.setOnClickListener {
            finish()
        }

        // Обробка натискання кнопки "Зберегти"
        findViewById<TextView>(R.id.save).setOnClickListener {
            val questionContent = questionTxt.text.toString().trim()
            if (questionContent.isEmpty()) {
                questionTxt.error = getString(R.string.please_enter_a_question)
                return@setOnClickListener
            }

            val questionRequestBody = QuestionRequestBody(
                list_id = setId!!,
                status = "false",
                content = questionContent,
                answer = answerTxt.text.toString().trim()
            )

            // Відправка запиту для створення питання
            lifecycleScope.launch {
                try {
                    val questionResponse = RetrofitInstance.api().createQuestion(questionRequestBody)
                    if (questionResponse.isSuccessful) {
                        // Успішне створення питання
                        finish()
                    } else {
                        Log.e("NewQuestionActivity", "Error creating question: ${questionResponse.message()}")
                    }
                } catch (e: HttpException) {
                    Log.e("NewQuestionActivity", "HttpException: ${e.message}")
                } catch (e: Exception) {
                    Log.e("NewQuestionActivity", "Exception: ${e.message}")
                }
            }
        }

        // Вибір сета
        setupSetSelectionDialog()
    }

    private fun setupSetSelectionDialog() {
        val setLayout: LinearLayout = findViewById(R.id.set)
        setLayout.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_select_selection, null)
            val listView: ListView = dialogView.findViewById(R.id.levels_list)
            val dialogTitle: TextView = dialogView.findViewById(R.id.dialog_title)
            dialogTitle.text = getString(R.string.select_set)

            val adapter = object : ArrayAdapter<Set>(this, R.layout.dialog_item, sets) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = convertView ?: layoutInflater.inflate(R.layout.dialog_item, parent, false)
                    val textView: TextView = view.findViewById(R.id.level_item)
                    textView.text = getItem(position)?.name
                    return view
                }
            }
            listView.adapter = adapter

            val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
                .setView(dialogView)
            val dialog = builder.create()

            listView.setOnItemClickListener { _, _, position, _ ->
                val selectedSet = sets[position]
                val levelType: TextView = findViewById(R.id.set_name)
                levelType.text = selectedSet.name
                dialog.dismiss()
                setId = selectedSet.id
            }
            dialog.show()
        }
    }

    fun loadLocale(context: Context) {
        val sharedPref = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val language = sharedPref.getString("My_lang", "")
        if (!language.isNullOrEmpty()) {
            setLocale(language, context)
        }
    }
}