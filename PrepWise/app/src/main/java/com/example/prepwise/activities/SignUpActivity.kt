package com.example.prepwise.activities

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.prepwise.LocaleHelper.loadLocale
import com.example.prepwise.R

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadLocale(this)

        // Перехід на сторінку входу
        val goToLogin: TextView = findViewById(R.id.go_to_login)
        goToLogin.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val goToConfirm: TextView = findViewById(R.id.sign_up_btn)
        goToConfirm.setOnClickListener{
            val intent = Intent(this, ConfirmEmailActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}