package com.example.prepwise.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prepwise.R
import com.example.prepwise.SpaceItemDecoration
import com.example.prepwise.adapters.AdapterAddSet
import com.example.prepwise.dataClass.FolderRequestBody
import com.example.prepwise.dataClass.QuestionRequestBody
import com.example.prepwise.dataClass.SetRequestBody
import com.example.prepwise.dataClass.UpdateFolderRequest
import com.example.prepwise.dataClass.UpdateSetRequest
import com.example.prepwise.models.Folder
import com.example.prepwise.objects.FolderRepository
import com.example.prepwise.objects.KeyboardUtils.hideKeyboard
import com.example.prepwise.objects.LocaleHelper.setLocale
import com.example.prepwise.objects.RetrofitInstance
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import retrofit2.HttpException

class NewFolderActivity : AppCompatActivity() {

    private var adapterAddSet: AdapterAddSet? = null
    private lateinit var recyclerViewSet: RecyclerView

    private lateinit var titleTxt: TextInputEditText
    private lateinit var selectedSetId: ArrayList<Int>

    private var originalSelectedSetIds = ArrayList<Int>()

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
        setContentView(R.layout.activity_new_folder)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadLocale(this)

        titleTxt = findViewById(R.id.title)
        selectedSetId = arrayListOf()

        titleTxt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard(this, currentFocus ?: View(this))
                true
            } else {
                false
            }
        }

        val mode = intent.getStringExtra("mode") ?: "create"
        val folderId = intent.getIntExtra("folderId", -1)

        if (mode == "edit" && folderId != -1) {
            val customScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
            customScope.launch {
                loadDataForEditing(folderId)
            }
            findViewById<TextView>(R.id.mode).text = getString(R.string.edit_folder)
        }

        recyclerViewSet = findViewById(R.id.list_sets)
        recyclerViewSet.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapterAddSet = MainActivity.currentUser?.let { AdapterAddSet(it.sets, selectedSetId, this) }
        recyclerViewSet.adapter = adapterAddSet

        val spacingInDp = 10
        val scale = this.resources.displayMetrics.density
        val spacingInPx = (spacingInDp * scale).toInt()
        recyclerViewSet.addItemDecoration(SpaceItemDecoration(spacingInPx))

        // Закриття сторінки
        val close: TextView = findViewById(R.id.cancel)
        close.setOnClickListener {
            finish()
        }

        findViewById<TextView>(R.id.save).setOnClickListener {
            val title = titleTxt.text.toString()

            if (title.isEmpty()) {
                titleTxt.error = getString(R.string.please_enter_a_title)
                return@setOnClickListener
            }

            if(mode=="create") {
                val customScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
                customScope.launch {
                    try {
                        val requestBody = FolderRequestBody(title, adapterAddSet!!.setsId)
                        val response = RetrofitInstance.api().createFolder(requestBody)

                        if (response.isSuccessful && response.body() != null) {
                            val newFolderId = response.body()!!.folder.folderId
                            Toast.makeText(
                                this@NewFolderActivity,
                                getString(R.string.folder_created_successfully),
                                Toast.LENGTH_SHORT
                            ).show()

                            // Якщо список сетів не порожній, додаємо їх у новостворену папку
                            if (adapterAddSet!!.setsId.isNotEmpty()) {
                                for (setId in adapterAddSet!!.setsId) {
                                    lifecycleScope.launch {
                                        try {
                                            val addSetResponse = RetrofitInstance.api()
                                                .AddSetToFolder(newFolderId, setId)
                                            if (!addSetResponse.isSuccessful) {
                                                Log.e(
                                                    "NewFolderActivity",
                                                    "Error adding set to folder: ${addSetResponse.message()}"
                                                )
                                            }
                                        } catch (e: HttpException) {
                                            Log.e(
                                                "NewFolderActivity",
                                                "HttpException: ${e.message}"
                                            )
                                        } catch (e: Exception) {
                                            Log.e("NewFolderActivity", "Exception: ${e.message}")
                                        }
                                    }
                                }
                            }
                            val resultIntent = Intent()
                            setResult(Activity.RESULT_OK, resultIntent)
                            finish() // Закриваємо активність після успішного створення
                        } else {
                            Log.e(
                                "NewFolderActivity",
                                "Error creating folder: ${response.message()}"
                            )
                        }
                    } catch (e: HttpException) {
                        Log.e("NewFolderActivity", "HttpException: ${e.message}")
                    } catch (e: Exception) {
                        Log.e("NewFolderActivity", "Exception: ${e.message}")
                    }
                }
            }
            else if (mode == "edit") {
                val updatedSelectedSetIds = adapterAddSet?.setsId ?: emptyList()

                val setsToAdd = updatedSelectedSetIds - originalSelectedSetIds
                val setsToDelete = originalSelectedSetIds - updatedSelectedSetIds

                updateFolderAndSets(title, folderId, setsToAdd, setsToDelete)
            }
        }
    }

    private fun updateFolderAndSets(title: String, folderId: Int, setsToAdd: List<Int>, setsToDelete: List<Int>) {
        val updateRequest = UpdateFolderRequest(name = title)

        CoroutineScope(SupervisorJob() + Dispatchers.Main).launch {
            try {
                val updateResponse = RetrofitInstance.api().updateFolder(folderId, updateRequest)
                if (updateResponse.isSuccessful) {
                    setsToAdd.forEach { setId ->
                        try {
                            val addSetResponse = RetrofitInstance.api().AddSetToFolder(folderId, setId)
                            if (!addSetResponse.isSuccessful) {
                                Log.e(
                                    "NewFolderActivity",
                                    "Error adding set to folder: ${addSetResponse.message()}"
                                )
                            }
                        } catch (e: Exception) {
                            Log.e("NewFolderActivity", "Exception adding set: ${e.message}")
                        }
                    }
                    setsToDelete.forEach { setId ->
                        try {
                            val deleteResponse = RetrofitInstance.api().DeleteSetFromFolder(folderId, setId)
                            if (!deleteResponse.isSuccessful) {
                                Log.e("NewFolderActivity", "Error deleting set: ${deleteResponse.message()}")
                            }
                        } catch (e: Exception) {
                            Log.e("NewFolderActivity", "Exception deleting set: ${e.message}")
                        }
                    }
                    val resultIntent = Intent()
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                } else {
                    Log.e("NewFolderActivity", "Error updating folder: ${updateResponse.message()}")
                }
            } catch (e: Exception) {
                Log.e("NewFolderActivity", "Exception: ${e.message}")
            }
        }
    }

    private suspend fun loadDataForEditing(folderId: Int) {
        val folderData = FolderRepository.getFolderById(folderId)

        if (folderData != null) {
            titleTxt.setText(folderData.name)
            folderData.sets.forEach { set ->
                selectedSetId.add(set.id)
            }
            adapterAddSet?.notifyDataSetChanged()
            originalSelectedSetIds = folderData.sets.map { it.id }.toMutableList() as ArrayList<Int>
        }
    }
}