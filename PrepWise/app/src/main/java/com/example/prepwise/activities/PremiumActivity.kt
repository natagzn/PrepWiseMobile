package com.example.prepwise.activities

import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.prepwise.R
import com.example.prepwise.utils.LocaleHelper.setLocale

class PremiumActivity : AppCompatActivity() {
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
        setContentView(R.layout.activity_premium)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadLocale(this)

        // Закриття сторінки
        val close: ImageView = findViewById(R.id.close)
        close.setOnClickListener {
            finish()
        }

        val monthlySubscription: LinearLayout = findViewById(R.id.monthly_subscription)
        val annualSubscription: LinearLayout = findViewById(R.id.annual_subscription)
        val monthlyCheckImage: ImageView = findViewById(R.id.monthly_check_img)
        val yearlyCheckImage: ImageView = findViewById(R.id.yearly_check_img)

        // Клік для місячної підписки
        monthlySubscription.setOnClickListener {
            monthlySubscription.setBackgroundResource(R.drawable.white_green_rounded_background)
            annualSubscription.setBackgroundResource(R.drawable.white_rounded_background)
            monthlyCheckImage.setImageResource(R.drawable.check_circle)
            yearlyCheckImage.setImageResource(R.drawable.circle)
        }

        // Клік для річної підписки
        annualSubscription.setOnClickListener {
            annualSubscription.setBackgroundResource(R.drawable.white_green_rounded_background)
            monthlySubscription.setBackgroundResource(R.drawable.white_rounded_background)

            yearlyCheckImage.setImageResource(R.drawable.check_circle)
            monthlyCheckImage.setImageResource(R.drawable.circle)
        }

    }
}