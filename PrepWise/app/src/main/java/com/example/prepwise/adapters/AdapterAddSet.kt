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
import com.example.prepwise.models.Question
import com.example.prepwise.models.Set

class AdapterAddSet(
    private val setlist: ArrayList<Set>,
    private val selectedSetId: ArrayList<Int>,
    private val context: Context
) : RecyclerView.Adapter<AdapterAddSet.SetViewHolder>() {

    var setsId = selectedSetId

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

        if (selectedSetId.contains(set.id)) {
            holder.addDelBtn.setImageResource(R.drawable.added_set)
        } else {
            holder.addDelBtn.setImageResource(R.drawable.add_set)
        }

        // Обробка кліка на кнопку додавання/видалення сету
        holder.addDelBtn.setOnClickListener {
            if (selectedSetId.contains(set.id)) {
                selectedSetId.remove(set.id)
                holder.addDelBtn.setImageResource(R.drawable.add_set)
            } else {
                selectedSetId.add(set.id)
                holder.addDelBtn.setImageResource(R.drawable.added_set)
            }
        }

        holder.itemView.findViewById<TextView>(R.id.date).visibility = View.GONE
    }

    // Повертаємо кількість елементів у списку
    override fun getItemCount(): Int {
        return setlist.size
    }

    fun updateSets(newSets: List<Set>) {
        setlist.clear()
        setlist.addAll(newSets)
        notifyDataSetChanged()
    }
}
