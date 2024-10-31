package com.example.prepwise.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.prepwise.R
import com.example.prepwise.models.Folder
import com.example.prepwise.models.Question
import com.google.android.material.textfield.TextInputEditText
import java.time.format.DateTimeFormatter

class AdapterAddQuestion(
    private val questions: MutableList<Question>,
    private val context: Context
) : RecyclerView.Adapter<AdapterAddQuestion.QuestionViewHolder>() {

    // ViewHolder для утримання UI елементів
    class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val questionEditText: TextInputEditText = itemView.findViewById(R.id.question)
        val answerEditText: TextInputEditText = itemView.findViewById(R.id.answer)
        val deleteButton: ImageView = itemView.findViewById(R.id.delete)
    }

    // Створюємо новий ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_add_question, parent, false)
        return QuestionViewHolder(view)
    }

    // Прив'язуємо дані до ViewHolder
    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questions[position]

        holder.questionEditText.setText(question.content)
        holder.answerEditText.setText(question.answer)

        holder.deleteButton.setOnClickListener {
            removeQuestion(position)
        }

        holder.questionEditText.addTextChangedListener {
            question.content = it.toString()
        }

        holder.answerEditText.addTextChangedListener {
            question.answer = it.toString()
        }
    }

    // Повертаємо кількість елементів у списку
    override fun getItemCount(): Int {
        return questions.size
    }

    // Додаємо нове питання
    fun addQuestion() {
        questions.add(Question("", "", false))
        notifyItemInserted(questions.size - 1)
    }

    fun updateQuestions(newQuestions: List<Question>) {
        questions.clear()
        questions.addAll(newQuestions)
        notifyDataSetChanged()
    }

    private fun removeQuestion(position: Int) {
        questions.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, questions.size)
    }
}
