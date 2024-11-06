package com.example.prepwise.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginActivity : AppCompatActivity() {

    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadLocale(this)

        // Перехід на сторінку реєстрації
        val goToSignup: TextView = findViewById(R.id.go_to_signup)
        goToSignup.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Перехід на сторінку скидання паролю
        val goToForgotPass: TextView = findViewById(R.id.forgot_pass)
        goToForgotPass.setOnClickListener{
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }

        val loginBtn: TextView = findViewById(R.id.login_btn)
        loginBtn.setOnClickListener{
            email = findViewById<TextInputEditText>(R.id.email).text.toString()
            password = findViewById<TextInputEditText>(R.id.password).text.toString()
            performLogin(email, password, this)
        }
    }

    private fun performLogin(email: String, password: String, context: Context) {
        // Перевірка на порожні поля
        if (email.isBlank()) {
            Toast.makeText(context, getString(R.string.enter_email), Toast.LENGTH_SHORT).show()
            return
        }
        if (password.isBlank()) {
            Toast.makeText(context, getString(R.string.enter_password), Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.login(LoginRequest(email, password))
                if (response.status == 200) {
                    val token = response.tokenData.token
                    saveAuthToken(token)
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                    (context as? Activity)?.finish()
                    Log.d("LoginActivity", "Login successful with token: $token")
                } else {
                    Log.e("LoginActivity", "Invalid credentials")
                    Toast.makeText(context, getString(R.string.incorrect_email_or_password), Toast.LENGTH_SHORT).show()
                }
            } catch (e: HttpException) {
                Log.e("LoginActivity", "Login error: ${e.code()} ${e.message()}", e)
                if (e.code() == 401) {
                    Toast.makeText(context, getString(R.string.incorrect_email_or_password), Toast.LENGTH_SHORT).show()
                } else if (e.code() == 422){
                    Toast.makeText(context, getString(R.string.email_validation_failed), Toast.LENGTH_SHORT).show()
                }
                else Toast.makeText(context, getString(R.string.server_error_try_again_later), Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("LoginActivity", "Login error: ${e.message}", e)
                Toast.makeText(context, getString(R.string.connection_error_check_your_internet_connection), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveAuthToken(token: String) {
        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPref.edit().putString("auth_token", token).apply()
    }
}