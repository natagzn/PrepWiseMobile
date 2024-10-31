package com.example.prepwise

import android.content.Context
import androidx.appcompat.app.AlertDialog
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getString
import androidx.core.content.res.ResourcesCompat

object DialogUtils {
    fun showConfirmationDialog(
        context: Context,
        message: String,
        positiveButtonText: String,
        negativeButtonText: String,
        onResult: (Boolean) -> Unit
    ) {
        val builder = AlertDialog.Builder(context)

        val messageView = TextView(context).apply {
            text = message
            textSize = 18f
            typeface = ResourcesCompat.getFont(context, R.font.regular)
            setTextColor(ContextCompat.getColor(context, R.color.black))
            setPadding(50, 40, 50, 0)
        }
        builder.setView(messageView)

        builder.setPositiveButton(positiveButtonText) { dialog, _ ->
            onResult(true)
            dialog.dismiss()
        }
        builder.setNegativeButton(negativeButtonText) { dialog, _ ->
            onResult(false)
            dialog.dismiss()
        }

        val dialog = builder.create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.apply {
                textSize = 16f
                setTextColor(ContextCompat.getColor(context, R.color.red))
                typeface = ResourcesCompat.getFont(context, R.font.regular)
            }
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.apply {
                textSize = 16f
                setTextColor(ContextCompat.getColor(context, R.color.black))
                typeface = ResourcesCompat.getFont(context, R.font.regular)
            }
        }

        dialog.show()
    }

    // Функція для діалогу скарг
    fun showReportDialog(
        context: Context,
        onReportSubmitted: (String) -> Unit
    ) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_report, null)
        val reasonRadioGroup: RadioGroup = dialogView.findViewById(R.id.reason_radio_group)
        val submitButton: Button = dialogView.findViewById(R.id.submit_button)
        val otherReasonRadioButton: RadioButton = dialogView.findViewById(R.id.other)
        val otherReasonEditText: EditText = dialogView.findViewById(R.id.other_reason)

        val builder = AlertDialog.Builder(context, R.style.RoundedDialogTheme)
            .setView(dialogView)
            .setCancelable(true)

        val dialog = builder.create()

        // Показати або сховати поле для введення тексту при виборі пункту "Інше"
        reasonRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == otherReasonRadioButton.id) {
                otherReasonEditText.visibility = View.VISIBLE
            } else {
                otherReasonEditText.visibility = View.GONE
            }
        }

        submitButton.setOnClickListener {
            val selectedReasonId = reasonRadioGroup.checkedRadioButtonId
            if (selectedReasonId != -1) {
                val selectedReason = if (selectedReasonId == otherReasonRadioButton.id) {
                    val otherReasonText = otherReasonEditText.text.toString().trim()
                    if (otherReasonText.isEmpty()) {
                        Toast.makeText(context, context.getString(R.string.please_enter_your_reason), Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    otherReasonText
                } else {
                    dialogView.findViewById<RadioButton>(selectedReasonId).text.toString()
                }

                onReportSubmitted(selectedReason)
                dialog.dismiss()
            } else {
                Toast.makeText(context, context.getString(R.string.please_select_a_reason), Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

}

