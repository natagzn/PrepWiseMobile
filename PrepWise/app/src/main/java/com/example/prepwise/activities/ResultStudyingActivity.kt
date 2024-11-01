package com.example.prepwise.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.prepwise.R

class ResultStudyingActivity : AppCompatActivity() {
    private lateinit var knowCountTextView: TextView
    private lateinit var stillLearningCountTextView: TextView
    private lateinit var rightCountTxt: TextView
    private lateinit var leftCountTxt: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_studying)

        knowCountTextView = findViewById(R.id.number_of_know)
        stillLearningCountTextView = findViewById(R.id.number_of_still_learning)
        leftCountTxt = findViewById(R.id.left)
        rightCountTxt = findViewById(R.id.right)

        // Отримуємо передані дані
        val learnedCount = intent.getIntExtra("learnedCount", 0)
        val learningCount = intent.getIntExtra("learningCount", 0)
        val rightCount = intent.getIntExtra("rightCount", 0)
        val setId = intent.getIntExtra("setId",-1)
        val set = MainActivity.getSetById(setId)

        // Відображаємо їх на екрані
        knowCountTextView.text = learnedCount.toString()
        stillLearningCountTextView.text = learningCount.toString()
        leftCountTxt.text = rightCount.toString()
        rightCountTxt.text = rightCount.toString()

        // закриття сторінки
        findViewById<ImageView>(R.id.close).setOnClickListener{
            finish()
        }

        // Повернення до сторінки про сет
        findViewById<TextView>(R.id.back_to_set).setOnClickListener{
            finish()
        }

        // перезапуск сторінки вивчення карток
        findViewById<TextView>(R.id.restart).setOnClickListener {
            if (set != null && set!!.questions.any { !it.learned }) {
                val intent = Intent(this, StudyFlascardActivity::class.java)
                intent.putExtra("setId", setId)
                startActivity(intent)
            } else {
                val dialog = AlertDialog.Builder(this)
                    .setTitle(getString(R.string.attention))
                    .setMessage(getString(R.string.all_questions_in_this_set_have_already_been_studied))
                    .setPositiveButton("ОК") { dialog, _ ->
                        dialog.dismiss()
                        finish()
                    }
                    .create()
                dialog.show()
            }
            finish()
        }

        // встановлення результатів вивчення у вигляді відсотків
        val percentage: Int = if (rightCount != 0) (learnedCount * 100) / rightCount else 0
        findViewById<ProgressBar>(R.id.progress_bar).progress = percentage
        findViewById<TextView>(R.id.progress_persent).text = "$percentage%"

        // встановлення мотиваціної фрази
        val motivationTextView = findViewById<TextView>(R.id.motivation_text)
        motivationTextView.text = getRandomComment(percentage)
    }

    // Отримання рандомної мотиваційної фраази
    private fun getRandomComment(percentage: Int): String {
        val phrasesArray = when {
            percentage < 30 -> resources.getStringArray(R.array.low_phrases)
            percentage < 80 -> resources.getStringArray(R.array.medium_phrases)
            else -> resources.getStringArray(R.array.high_phrases)
        }
        return phrasesArray.random()
    }
}