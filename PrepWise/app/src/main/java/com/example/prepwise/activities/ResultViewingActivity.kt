package com.example.prepwise.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.prepwise.R

class ResultViewingActivity : AppCompatActivity() {
    private lateinit var knowCountTextView: TextView
    private lateinit var rightCountTxt: TextView
    private lateinit var leftCountTxt: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_viewing)

        knowCountTextView = findViewById(R.id.number_of_know)
        leftCountTxt = findViewById(R.id.left)
        rightCountTxt = findViewById(R.id.right)

        // Отримуємо передані дані
        val count = intent.getIntExtra("count", 0)
        val setId = intent.getIntExtra("setId",-1)

        // Відображаємо їх на екрані
        knowCountTextView.text = count.toString()
        leftCountTxt.text = count.toString()
        rightCountTxt.text = count.toString()

        // Закриття сторінки
        findViewById<ImageView>(R.id.close).setOnClickListener{
            finish()
        }

        // Повернення до сторніки перегляду інфомації про сет
        findViewById<TextView>(R.id.back_to_set).setOnClickListener{
            finish()
        }

        // Перезапускаємо сторінку перегляду
        findViewById<TextView>(R.id.restart).setOnClickListener {
            val intent = Intent(this, ViewFlashcardActivity::class.java)
            intent.putExtra("setId", setId)
            startActivity(intent)
            finish()
        }

        // Встановлюємо мотиваційну фразу
        val motivationTextView = findViewById<TextView>(R.id.motivation)
        motivationTextView.text = getRandomComment()
    }

    // Отримання мотиваційної фрази
    private fun getRandomComment(): String {
        val phrasesArray = resources.getStringArray(R.array.random_phrases)
        return phrasesArray.random()
    }


}