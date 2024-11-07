package com.example.prepwise.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.prepwise.LocaleHelper.loadLocale
import com.example.prepwise.R
import com.example.prepwise.RetrofitInstance
import com.example.prepwise.dataClass.LoginRequest
import com.example.prepwise.dataClass.SignUpRequest
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import retrofit2.HttpException

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
        findViewById<TextView>(R.id.go_to_login).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Кнопка реєстрації
        findViewById<TextView>(R.id.sign_up_btn).setOnClickListener {
            val username = findViewById<TextInputEditText>(R.id.username).text.toString().trim()
            val email = findViewById<TextInputEditText>(R.id.email).text.toString().trim()
            val password = findViewById<TextInputEditText>(R.id.pass).text.toString().trim()
            val confirmPassword = findViewById<TextInputEditText>(R.id.confirmPass).text.toString().trim()

            if (validateInput(username, email, password, confirmPassword)) {
                performSignUp(username, email, password, confirmPassword)
            }
        }
    }

    private fun validateInput(username: String, email: String, password: String, confirmPassword: String): Boolean {
        // Перевірка на порожні поля
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, getString(R.string.all_fields_required), Toast.LENGTH_SHORT).show()
            return false
        }

        // Перевірка правильності email
        if (!isValidEmail(email)) {
            Toast.makeText(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT).show()
            return false
        }

        // Перевірка username на наявність пробілів та довжину
        if (containsWhitespace(username) || username.length < 3) {
            Toast.makeText(this, getString(R.string.username_invalid), Toast.LENGTH_SHORT).show()
            return false
        }

        // Перевірка пароля на наявність пробілів, мінімальну довжину, та наявність цифр і літер
        if (containsWhitespace(password) || password.length < 8) {
            Toast.makeText(this, getString(R.string.password_invalid), Toast.LENGTH_SHORT).show()
            return false
        }

        // Перевірка, чи співпадають пароль та підтвердження пароля
        if (password != confirmPassword) {
            Toast.makeText(this, getString(R.string.passwords_do_not_match), Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun containsWhitespace(text: String): Boolean {
        return text.contains("\\s".toRegex())
    }

    private fun performSignUp(username: String, email: String, password: String, confirmPassword: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api().signUp(
                    SignUpRequest(username, email, password, confirmPassword)
                )
                if (response.status == 0) {
                    Toast.makeText(this@SignUpActivity, response.message, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@SignUpActivity, getString(R.string.registration_failed), Toast.LENGTH_SHORT).show()
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("SignUpActivity", "Sign-up error: HTTP ${e.code()} ${e.message()} - $errorBody", e)
                Toast.makeText(this@SignUpActivity, getString(R.string.error_occurred), Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("SignUpActivity", "Sign-up error: ${e.message}", e)
                Toast.makeText(this@SignUpActivity, getString(R.string.error_occurred), Toast.LENGTH_SHORT).show()
            }
        }
    }
}
