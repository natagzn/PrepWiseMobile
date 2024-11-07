package com.example.prepwise.objects

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LocaleHelper {

    // Функція для встановлення мови
    fun setLocale(languageCode: String, context: Context) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)

        val sharedPref = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("My_lang", languageCode)
        editor.apply()
    }

    // Функція для завантаження мови
    fun loadLocale(context: Context) {
        val sharedPref = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val language = sharedPref.getString("My_lang", "")
        if (!language.isNullOrEmpty()) {
            setLocale(language, context)
        }
    }
}