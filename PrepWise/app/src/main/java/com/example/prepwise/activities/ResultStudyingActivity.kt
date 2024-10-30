package com.example.prepwise.activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
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

        // Відображаємо їх на екрані
        knowCountTextView.text = learnedCount.toString()
        stillLearningCountTextView.text = learningCount.toString()
        leftCountTxt.text = rightCount.toString()
        rightCountTxt.text = rightCount.toString()

        findViewById<ImageView>(R.id.close).setOnClickListener{
            finish()
        }

        findViewById<TextView>(R.id.back_to_set).setOnClickListener{
            finish()
        }

        findViewById<TextView>(R.id.restart).setOnClickListener{
            finish()
        }
    }
}