package com.example.prepwise.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.prepwise.R
import com.example.prepwise.models.Question
import com.example.prepwise.models.Set

class AdapterQuestion(private val questionlist: ArrayList<Question>, private val context: Context) :
    RecyclerView.Adapter<AdapterQuestion.SetViewHolder>() {
    // ViewHolder клас для утримання посилань на UI елементи
    class SetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val setQuestion: TextView = itemView.findViewById(R.id.question)
        val setAnswer: TextView = itemView.findViewById(R.id.answer)
        val help: ImageView = itemView.findViewById(R.id.help)
    }

    // Створюємо новий ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_question, parent, false)
        return SetViewHolder(view)
    }

    // Прив'язуємо дані до ViewHolder
    override fun onBindViewHolder(holder: SetViewHolder, position: Int) {
        val question = questionlist[position]
        holder.setQuestion.text = question.content
        holder.setAnswer.text = question.answer
        holder.help.setOnClickListener {

        }
    }

    // Повертаємо кількість елементів у списку
    override fun getItemCount(): Int {
        return questionlist.size
    }
}