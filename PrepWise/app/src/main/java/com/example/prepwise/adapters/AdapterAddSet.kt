package com.example.prepwise.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.prepwise.R
import com.example.prepwise.models.Set

class AdapterAddSet(private val setlist: ArrayList<Set>, private val context: Context) :
    RecyclerView.Adapter<AdapterAddSet.SetViewHolder>() {
    // ViewHolder клас для утримання посилань на UI елементи
    class SetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val setName: TextView = itemView.findViewById(R.id.set_name)
        val setNumberOfQuestions: TextView = itemView.findViewById(R.id.number_of_questions)
        val addDelBtn: ImageView = itemView.findViewById(R.id.add_delete_to_folder)
    }

    // Створюємо новий ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_add_set, parent, false)
        return SetViewHolder(view)
    }

    // Прив'язуємо дані до ViewHolder
    override fun onBindViewHolder(holder: SetViewHolder, position: Int) {
        val set = setlist[position]
        holder.setName.text = set.name
        holder.setNumberOfQuestions.text = set.questions.size.toString()
        holder.addDelBtn.setImageResource(R.drawable.add_set)
        holder.addDelBtn.setOnClickListener{
            holder.addDelBtn.setImageResource(R.drawable.added_set)
        }
    }

    // Повертаємо кількість елементів у списку
    override fun getItemCount(): Int {
        return setlist.size
    }
}