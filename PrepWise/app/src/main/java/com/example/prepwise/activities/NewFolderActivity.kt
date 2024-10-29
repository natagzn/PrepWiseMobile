package com.example.prepwise.activities

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prepwise.R
import com.example.prepwise.SpaceItemDecoration
import com.example.prepwise.adapters.AdapterAddSet
import com.example.prepwise.models.Folder

class NewFolderActivity : AppCompatActivity() {

    private var adapterAddSet: AdapterAddSet? = null
    private lateinit var recyclerViewSet: RecyclerView

    private lateinit var titleTxt: TextView
    private lateinit var selectedSetId: ArrayList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_folder)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        titleTxt = findViewById(R.id.title)
        selectedSetId = arrayListOf()

        val mode = intent.getStringExtra("mode") ?: "create"
        val folderId = intent.getIntExtra("folderId", -1)

        if (mode == "edit" && folderId != -1) {
            loadDataForEditing(folderId)
            findViewById<TextView>(R.id.mode).text = getString(R.string.edit_folder)
        }

        // Закриття сторінки
        val close: TextView = findViewById(R.id.cancel)
        close.setOnClickListener {
            finish()
        }

        recyclerViewSet = findViewById(R.id.list_sets)
        recyclerViewSet.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapterAddSet = AdapterAddSet(MainActivity.setList, selectedSetId, this)
        recyclerViewSet.adapter = adapterAddSet

        val spacingInDp = 10
        val scale = this.resources.displayMetrics.density
        val spacingInPx = (spacingInDp * scale).toInt()
        recyclerViewSet.addItemDecoration(SpaceItemDecoration(spacingInPx))
    }

    private fun loadDataForEditing(folderId: Int) {
         val folderData = MainActivity.getFolderById(folderId)

        if (folderData != null) {
            titleTxt.text = folderData.name
            folderData.sets.forEach { set ->
                selectedSetId.add(set.id)
            }
            adapterAddSet?.updateSets(folderData.sets)
        }
    }
}