package com.example.prepwise

import android.content.Context
import androidx.appcompat.app.AlertDialog
import android.graphics.Typeface
import android.widget.TextView
import androidx.core.content.ContextCompat
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
}

