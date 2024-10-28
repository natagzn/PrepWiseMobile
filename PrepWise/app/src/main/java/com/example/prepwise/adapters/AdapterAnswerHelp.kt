package com.example.prepwise.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.prepwise.R
import com.example.prepwise.models.HelpAnswer
import java.time.format.DateTimeFormatter

class AdapterAnswerHelp(private val answers: List<HelpAnswer>) :
    RecyclerView.Adapter<AdapterAnswerHelp.AnswerViewHolder>() {

    class AnswerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val answerText: TextView = itemView.findViewById(R.id.answer)
        val username: TextView = itemView.findViewById(R.id.username)
        val answerDate: TextView = itemView.findViewById(R.id.date_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_answer, parent, false)
        return AnswerViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        val answer = answers[position]
        holder.answerText.text = answer.content
        holder.username.text = answer.username

        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        holder.answerDate.text = answer.dateTime.format(formatter)
    }

    override fun getItemCount() = answers.size
}