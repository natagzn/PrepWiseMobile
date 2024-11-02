package com.example.prepwise.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat.getString
import androidx.recyclerview.widget.RecyclerView
import com.example.prepwise.R
import com.example.prepwise.models.People

class AccessAdapter(
    private val users: List<People>,
    private val context: Context,
    private val onAccessChanged: (String, String) -> Unit
) : RecyclerView.Adapter<AccessAdapter.ViewHolder>() {

    private val accessLevels = arrayOf(getString(context ,R.string.None), getString(context ,R.string.View), getString(context ,R.string.Edit))

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userName: TextView = view.findViewById(R.id.username)
        val accessSpinner: Spinner = view.findViewById(R.id.access_spinner)

        fun bind(user: String) {
            userName.text = user
            val adapter = ArrayAdapter(itemView.context, android.R.layout.simple_spinner_item, accessLevels)
            adapter.setDropDownViewResource(R.layout.custom_dropdown_item)
            accessSpinner.adapter = adapter

            accessSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    onAccessChanged(user, accessLevels[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_access, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(users[position].username)
    }

    override fun getItemCount(): Int = users.size
}
