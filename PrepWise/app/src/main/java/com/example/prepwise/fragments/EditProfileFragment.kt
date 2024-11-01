package com.example.prepwise.fragments

import android.os.Bundle
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

class EditProfileFragment : Fragment() {

    private lateinit var descriptionEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var locationAutoCompleteTextView: AutoCompleteTextView
    private lateinit var saveButton: TextView
    private lateinit var cancelButton: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)

        descriptionEditText = view.findViewById(R.id.description)
        emailEditText = view.findViewById(R.id.email)
        locationAutoCompleteTextView = view.findViewById(R.id.location)

        saveButton = view.findViewById(R.id.save)
        cancelButton = view.findViewById(R.id.cancel)

        // завантаження списку країн для вибору локації
        fetchCountries()

        // обробка натискання Save
        saveButton.setOnClickListener {
            saveUserData()
            requireActivity().supportFragmentManager.popBackStack()
        }

        // обробка натискання Cancel
        cancelButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

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

    private fun saveUserData() {
        val description = descriptionEditText.text.toString()
        val email = emailEditText.text.toString()
        val location = locationAutoCompleteTextView.text.toString()

    }
}


