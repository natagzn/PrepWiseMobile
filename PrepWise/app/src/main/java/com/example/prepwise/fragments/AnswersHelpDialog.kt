package com.example.prepwise.fragments

import android.app.Dialog
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prepwise.R
import com.example.prepwise.adapters.AdapterAnswerHelp
import com.example.prepwise.models.HelpAnswer

class AnswersHelpDialog(private val answers: List<HelpAnswer>) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_answers_help, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_answers)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = AdapterAnswerHelp(answers)

        val close: ImageView = view.findViewById(R.id.close_button)
        close.setOnClickListener{dismiss()}

        return view
    }

    companion object {
        fun newInstance(answers: List<HelpAnswer>): AnswersHelpDialog {
            return AnswersHelpDialog(answers)
        }
    }

    override fun onStart() {
        super.onStart()

        val width = (resources.displayMetrics.widthPixels * 0.9).toInt()
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}
