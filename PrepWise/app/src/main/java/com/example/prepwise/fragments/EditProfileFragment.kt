package com.example.prepwise.fragments

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.prepwise.R
import com.example.prepwise.activities.MainActivity.Companion.currentUser
import com.example.prepwise.dataClass.QuestionRequestBody
import com.example.prepwise.dataClass.UpdateProfileRequest
import com.example.prepwise.objects.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import retrofit2.HttpException
import java.net.HttpURLConnection
import java.net.URL

class EditProfileFragment : Fragment() {

    private lateinit var email: TextView
    private lateinit var username: TextView
    private lateinit var descriptionEditText: EditText
    private lateinit var locationAutoCompleteTextView: AutoCompleteTextView
    private lateinit var saveButton: TextView
    private lateinit var cancelButton: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)

        descriptionEditText = view.findViewById(R.id.description)
        locationAutoCompleteTextView = view.findViewById(R.id.location)
        email = view.findViewById(R.id.email)
        username = view.findViewById(R.id.username)

        if(currentUser.bio!="-") descriptionEditText.setText(currentUser.bio)
        if(currentUser.location!="-") locationAutoCompleteTextView.setText(currentUser.location)
        email.text = currentUser.email
        username.text = currentUser.username

        saveButton = view.findViewById(R.id.save)
        cancelButton = view.findViewById(R.id.cancel)

        // завантаження списку країн для вибору локації
        fetchCountries()

        // обробка натискання Save
        saveButton.setOnClickListener {
            var newDescription = descriptionEditText.text.toString()
            var newLocation = locationAutoCompleteTextView.text.toString()
            if (newDescription == "") newDescription = "-"
            if (newLocation == "") newLocation = "-"

            val customScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
            customScope.launch {
                try {
                    val updateRequestBody = UpdateProfileRequest(
                        bio = newDescription,
                        location = newLocation
                    )
                    val updateResponse = RetrofitInstance.api().updateProfile(updateRequestBody)
                    if (!updateResponse.isSuccessful) {
                        Log.e(
                            "EditProfileFragmant",
                            "Не вдалося оновити профіль: ${updateResponse.message()}"
                        )
                    }
                    else Log.d("EditProfileFragmant", "ПРОФЛЬ ОНОВЛЕНО" )

                } catch (e: Exception) {
                    Log.e("EditProfileFragmant", "Виняток: ${e.message}")
                }
            }

            // Встановлення результату
            requireActivity().supportFragmentManager.setFragmentResult(
                "profileUpdate",
                Bundle().apply { putBoolean("isUpdated", true) }
            )

            // Повернення до `ProfileFragment`
            requireActivity().supportFragmentManager.popBackStack()
        }

        // обробка натискання Cancel
        cancelButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        // Встановлення фото профілю
        val (initials, backgroundColor) = generateAvatar(currentUser.username)
        val userInitialsView:TextView = view.findViewById(R.id.user_initials)
        userInitialsView.text = initials

        val drawable = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            color = ColorStateList.valueOf(backgroundColor)
        }
        userInitialsView.background = drawable

        return view
    }

    private fun fetchCountries() {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    val url = URL("https://restcountries.com/v3.1/all")
                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "GET"
                    connection.inputStream.bufferedReader().use { it.readText() }
                }

                val countriesJson = JSONArray(response)
                val countries = mutableListOf<String>()
                for (i in 0 until countriesJson.length()) {
                    val country = countriesJson.getJSONObject(i)
                    countries.add(country.getJSONObject("name").getString("common"))
                }
                countries.sort()

                // Використовуємо кастомний макет для випадаючого списку
                val adapter = ArrayAdapter(requireContext(), R.layout.custom_dropdown_item, countries)
                locationAutoCompleteTextView.setAdapter(adapter)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Failed to load countries", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun generateAvatar(username: String): Pair<String, Int> {
        val initials = if (username.isNotEmpty()) username.take(2).uppercase() else "N/A"

        // Генерація кольору на основі хешу імені
        val hash = username.fold(0) { acc, char -> acc + char.code }
        val hue = hash % 360
        val color = Color.HSVToColor(floatArrayOf(hue.toFloat(), 0.3f, 0.7f)) // Колір у форматі HSL

        return Pair(initials, color)
    }
}


