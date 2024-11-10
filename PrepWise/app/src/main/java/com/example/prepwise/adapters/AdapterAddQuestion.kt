package com.example.prepwise.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.prepwise.utils.DialogUtils
import com.example.prepwise.R
import com.example.prepwise.activities.MainActivity
import com.example.prepwise.models.Question

class AdapterAddQuestion(
    val questions: MutableList<Question>,
    private val context: Context
) : RecyclerView.Adapter<AdapterAddQuestion.QuestionViewHolder>() {

    var questionsToUpdate = mutableListOf<Question>()
    var questionsToDelete = mutableListOf<Question>()

    // ViewHolder для утримання UI елементів
    class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val questionEditText: EditText = itemView.findViewById(R.id.question)
        val answerEditText: EditText = itemView.findViewById(R.id.answer)
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
            questionsToDelete.add(question) // Додаємо до списку питань на видалення
            removeQuestion(position)
        }

        holder.questionEditText.addTextChangedListener {
            if (question.content != it.toString()) {
                question.content = it.toString()
                if (question.question_id != -1 && !questionsToUpdate.contains(question)) {
                    questionsToUpdate.add(question) // Додаємо до списку оновлень
                }
            }
        }

        holder.answerEditText.addTextChangedListener {
            if (question.answer != it.toString()) {
                question.answer = it.toString()
                if (question.question_id != -1 && !questionsToUpdate.contains(question)) {
                    questionsToUpdate.add(question) // Додаємо до списку оновлень
                }
            }
        }
    }

    // Повертаємо кількість елементів у списку
    override fun getItemCount(): Int {
        return questions.size
    }

    // Додаємо нове питання
    fun addQuestion() {
        if (!MainActivity.currentUser!!.premium && questions.size >= 20) {
            DialogUtils.showPremiumDialog(context)
            return
        }
        val newQuestion = Question(-1, "", "", false)
        questions.add(newQuestion)
        notifyItemInserted(questions.size - 1)
    }

    fun updateQuestions(newQuestions: List<Question>) {
        questions.clear()
        questions.addAll(newQuestions)
        notifyDataSetChanged()
    }

    private fun removeQuestion(position: Int) {
        val question = questions[position]
        /*if (question.question_id != -1) {
            questionsToDelete.add(question)
        }*/
        questions.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, questions.size)
    }
}
