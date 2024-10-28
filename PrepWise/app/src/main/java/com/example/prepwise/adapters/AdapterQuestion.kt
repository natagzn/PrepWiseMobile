package com.example.prepwise.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prepwise.R
import com.example.prepwise.fragments.AnswersHelpDialog
import com.example.prepwise.fragments.FriendHelpDialog
import com.example.prepwise.models.HelpAnswer
import com.example.prepwise.models.Question
import java.time.LocalDateTime

class AdapterQuestion(
    private val questionList: ArrayList<Question>,
    private val context: Context,
    private val fragmentManager: FragmentManager
) : RecyclerView.Adapter<AdapterQuestion.SetViewHolder>() {

    class SetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val setQuestion: TextView = itemView.findViewById(R.id.question)
        val setAnswer: TextView = itemView.findViewById(R.id.answer)
        val help: ImageView = itemView.findViewById(R.id.help)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_question, parent, false)
        return SetViewHolder(view)
    }

    override fun onBindViewHolder(holder: SetViewHolder, position: Int) {
        val question = questionList[position]
        holder.setQuestion.text = question.content
        holder.setAnswer.text = question.answer

        holder.help.setOnClickListener { view ->
            showPopupMenu(view)
        }
    }

    override fun getItemCount(): Int {
        return questionList.size
    }

    private fun showPopupMenu(view: View) {
        val popup = PopupMenu(ContextThemeWrapper(context, R.style.CustomPopupMenu), view)
        popup.menuInflater.inflate(R.menu.popup_set_menu, popup.menu)


        // для відображення іконок в меню
        try {
            val fieldMPopup = PopupMenu::class.java.getDeclaredField("mPopup")
            fieldMPopup.isAccessible = true
            val mPopup = fieldMPopup.get(popup)
            mPopup.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(mPopup, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.ask_for_help -> {
                    val answersDialog = FriendHelpDialog()
                    answersDialog.show(fragmentManager, "AnswersDialog")
                    true
                }
                R.id.look_answer -> {
                    val answers = listOf(
                        HelpAnswer(1, "Answer 1 lorem lorem", "nahalkaanna", LocalDateTime.now()),
                        HelpAnswer(2, "Answer 2 lorem lorem", "nahalkaanna", LocalDateTime.now())
                    )

                    val answersDialog = AnswersHelpDialog.newInstance(answers)
                    answersDialog.show(fragmentManager, "AnswersDialog")
                    true
                }
                else -> false
            }
        }

        popup.show()
    }
}
