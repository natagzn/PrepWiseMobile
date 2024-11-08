package com.example.prepwise.objects

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.Gravity
import androidx.appcompat.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.PopupWindow
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.prepwise.R
import com.example.prepwise.SelectableItem
import com.example.prepwise.activities.MainActivity
import com.example.prepwise.activities.PremiumActivity
import com.example.prepwise.models.Category
import com.example.prepwise.models.Level
import java.time.LocalDate

object DialogUtils {
    fun showConfirmationDialog(
        context: Context,
        message: String,
        positiveButtonText: String,
        negativeButtonText: String,
        onResult: (Boolean) -> Unit
    ) {
        val builder = AlertDialog.Builder(context, R.style.RoundedDialogTheme)

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
                textSize = 17f
                setTextColor(ContextCompat.getColor(context, R.color.red))
                typeface = ResourcesCompat.getFont(context, R.font.regular)
                isAllCaps = false
            }
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.apply {
                textSize = 17f
                setTextColor(ContextCompat.getColor(context, R.color.black))
                typeface = ResourcesCompat.getFont(context, R.font.regular)
                isAllCaps = false
            }
        }

        dialog.show()
    }

    // Функція для діалогу скарг
    fun showReportDialog(
        context: Context,
        title: String,
        onReportSubmitted: (String) -> Unit
    ) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_report, null)
        val titleTextView: TextView = dialogView.findViewById(R.id.title)
        val reasonRadioGroup: RadioGroup = dialogView.findViewById(R.id.reason_radio_group)
        val submitButton: Button = dialogView.findViewById(R.id.submit_button)
        val otherReasonRadioButton: RadioButton = dialogView.findViewById(R.id.other)
        val otherReasonEditText: EditText = dialogView.findViewById(R.id.other_reason)
        val close: ImageView = dialogView.findViewById(R.id.close)

        // Встановлюємо текст заголовку
        titleTextView.text = title

        val builder = AlertDialog.Builder(context, R.style.TransparentDialogTheme)
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

        close.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    // Функція для створення діалогового вікна щоб користувач задав питання у підримку
    fun showAnswerDialog(context: Context) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_support, null)
        val textQuestion: EditText = dialogView.findViewById(R.id.question_input)
        val applyButton: TextView = dialogView.findViewById(R.id.apply_button)
        val cancelButton: TextView = dialogView.findViewById(R.id.cancel_button)

        val builder = AlertDialog.Builder(context, R.style.RoundedDialogTheme)
            .setView(dialogView)
            .setCancelable(true)

        val dialog = builder.create()

        applyButton.setOnClickListener {
            val answer = textQuestion.text.toString()
            if (answer.isNotBlank()) {
                Toast.makeText(context, context.getString(R.string.question_sent), Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                Toast.makeText(context, context.getString(R.string.please_enter_a_question), Toast.LENGTH_SHORT).show()
            }
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    fun <T : SelectableItem> showSelectionPopup(
        context: Context,
        anchorView: View,
        title: String,
        items: List<T>,
        selectedItemTextViewId: Int,
        dialogLayoutId: Int,
        itemLayoutId: Int,
        onItemSelected: (T) -> Unit
    ) {
        val popupView = LayoutInflater.from(context).inflate(dialogLayoutId, null)
        val listView: ListView = popupView.findViewById(R.id.levels_list)
        val dialogTitle: TextView = popupView.findViewById(R.id.dialog_title)
        dialogTitle.text = title

        val adapter = object : ArrayAdapter<T>(context, itemLayoutId, items) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = convertView ?: LayoutInflater.from(parent.context).inflate(itemLayoutId, parent, false)
                val textView: TextView = view.findViewById(R.id.level_item)
                textView.text = getItem(position)?.name
                return view
            }
        }
        listView.adapter = adapter

        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(context,
            R.drawable.white_green_rounded_background
        ))
        popupWindow.elevation = 8f

        val dimBackground = (context as Activity).findViewById<View>(R.id.dim_background)
        dimBackground.visibility = View.VISIBLE

        popupWindow.setOnDismissListener {
            dimBackground.visibility = View.GONE
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = items[position]
            val selectedTextView: TextView = (context as Activity).findViewById(selectedItemTextViewId)
            selectedTextView.text = selectedItem.name
            onItemSelected(selectedItem)
            popupWindow.dismiss()
        }

        popupWindow.showAsDropDown(anchorView, 0, 10)
    }


    fun <T> showSortPopupMenu(
        context: Context,
        anchorView: View,
        list: MutableList<T>,
        adapter: RecyclerView.Adapter<*>,
        getDate: ((T) -> LocalDate)? = null,
        getName: ((T) -> String)? = null
    ) {
        // Інфлюємо макет для PopupWindow
        val popupView = LayoutInflater.from(context).inflate(R.layout.dialog_sort_menu, null)
        val popupWindow = PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true)

        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(context,
            R.drawable.white_green_rounded_background
        ))
        popupWindow.elevation = 8f

        val dimBackground = (context as Activity).findViewById<View>(R.id.dim_background)
        dimBackground.visibility = View.VISIBLE

        popupWindow.setOnDismissListener {
            dimBackground.visibility = View.GONE
        }

        // Отримуємо посилання на елементи сортування
        val sortCreatedOldNew = popupView.findViewById<TextView>(R.id.sort_created_old_new)
        val sortCreatedNewOld = popupView.findViewById<TextView>(R.id.sort_created_new_old)
        val sortNameAZ = popupView.findViewById<TextView>(R.id.sort_name_a_z)
        val sortNameZA = popupView.findViewById<TextView>(R.id.sort_name_z_a)

        // Додаємо обробники кліків
        sortCreatedOldNew.setOnClickListener {
            if (getDate != null) sortListByDate(list, adapter, ascending = true, getDate)
            popupWindow.dismiss()
        }

        sortCreatedNewOld.setOnClickListener {
            if (getDate != null) sortListByDate(list, adapter, ascending = false, getDate)
            popupWindow.dismiss()
        }

        sortNameAZ.setOnClickListener {
            if (getName != null) sortListByName(list, adapter, ascending = true, getName)
            popupWindow.dismiss()
        }

        sortNameZA.setOnClickListener {
            if (getName != null) sortListByName(list, adapter, ascending = false, getName)
            popupWindow.dismiss()
        }

        popupWindow.showAsDropDown(anchorView, 0, 10)
    }

    private fun <T> sortListByDate(
        list: MutableList<T>,
        adapter: RecyclerView.Adapter<*>,
        ascending: Boolean,
        getDate: (T) -> LocalDate
    ) {
        if (ascending) {
            list.sortBy { getDate(it) }
        } else {
            list.sortByDescending { getDate(it) }
        }
        adapter.notifyDataSetChanged()
    }

    private fun <T> sortListByName(
        list: MutableList<T>,
        adapter: RecyclerView.Adapter<*>,
        ascending: Boolean,
        getName: (T) -> String
    ) {
        if (ascending) {
            list.sortBy { getName(it) }
        } else {
            list.sortByDescending { getName(it) }
        }
        adapter.notifyDataSetChanged()
    }

    fun showFilterPopup(
        context: Context,
        anchorView: View,
        onApplyFilters: (selectedCategories: List<Category>, selectedLevels: List<Level>, selectedAccesses: List<String>) -> Unit,
        accessOptions: List<String>,
        currentCategories: List<Category>,
        currentLevels: List<Level>,
        currentAccesses: List<String>,
        showAccessFilter: Boolean
    ) {
        val popupView = LayoutInflater.from(context).inflate(R.layout.dialog_filter_menu, null)
        val popupWindow = PopupWindow(
            popupView,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1400
        )

        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(context,
            R.drawable.white_rounded_background
        ))
        popupWindow.elevation = 8f
        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true

        val dimBackground = (context as Activity).findViewById<View>(R.id.dim_background)
        dimBackground.visibility = View.VISIBLE
        popupWindow.setOnDismissListener { dimBackground.visibility = View.GONE }

        val categoryContainer = popupView.findViewById<LinearLayout>(R.id.categoryContainer)
        val levelContainer = popupView.findViewById<LinearLayout>(R.id.levelContainer)
        val accessContainer = popupView.findViewById<LinearLayout>(R.id.accessContainer)

        accessContainer.visibility = if (showAccessFilter) View.VISIBLE else View.GONE
        popupView.findViewById<TextView>(R.id.access_txt).visibility = if (showAccessFilter) View.VISIBLE else View.GONE
        popupView.findViewById<TextView>(R.id.access_txt1).visibility = if (showAccessFilter) View.VISIBLE else View.GONE

        val categories: List<Category> = MainActivity.categories
        val levels: List<Level> = MainActivity.levels

        categories.forEach { category ->
            val checkBox = CheckBox(context).apply {
                text = category.name // Відображаємо назву категорії
                isChecked = currentCategories.any { it.id == category.id } // Перевірка наявності в вибраних категоріях
            }
            categoryContainer.addView(checkBox)
        }

        levels.forEach { level ->
            val checkBox = CheckBox(context).apply {
                text = level.name
                isChecked = currentLevels.any { it.id == level.id }
            }
            levelContainer.addView(checkBox)
        }

        accessOptions.forEach { access ->
            val checkBox = CheckBox(context).apply {
                text = access
                isChecked = currentAccesses.contains(access)
            }
            accessContainer.addView(checkBox)
        }

        popupView.findViewById<TextView>(R.id.apply_button).setOnClickListener {
            val selectedCategories = categories.filterIndexed { index, _ ->
                (categoryContainer.getChildAt(index) as CheckBox).isChecked
            }
            val selectedLevels = levels.filterIndexed { index, _ ->
                (levelContainer.getChildAt(index) as CheckBox).isChecked
            }
            val selectedAccesses = accessOptions.filterIndexed { index, _ ->
                (accessContainer.getChildAt(index) as CheckBox).isChecked
            }

            onApplyFilters(selectedCategories, selectedLevels, selectedAccesses)
            popupWindow.dismiss()
        }

        popupView.findViewById<TextView>(R.id.cancel_button).setOnClickListener {
            popupWindow.dismiss()
        }

        popupView.findViewById<TextView>(R.id.reset_button).setOnClickListener {
            // Reset checkboxes
            for (i in 0 until categoryContainer.childCount) {
                (categoryContainer.getChildAt(i) as CheckBox).isChecked = false
            }
            for (i in 0 until levelContainer.childCount) {
                (levelContainer.getChildAt(i) as CheckBox).isChecked = false
            }
            for (i in 0 until accessContainer.childCount) {
                (accessContainer.getChildAt(i) as CheckBox).isChecked = false
            }
        }

        //popupWindow.showAsDropDown(anchorView, 0, 10)
        popupWindow.showAtLocation(anchorView, Gravity.END, 40, 150)
    }

    fun showPremiumDialog(context: Context) {
        val builder = AlertDialog.Builder(context, R.style.RoundedDialogTheme)

        val titleView = TextView(context).apply {
            text = context.getString(R.string.prepWise_premium)
            textSize = 21f
            typeface = ResourcesCompat.getFont(context, R.font.bold)
            setTextColor(ContextCompat.getColor(context, R.color.black))
            setPadding(50, 40, 50, 0)
        }
        builder.setCustomTitle(titleView)

        val messageView = TextView(context).apply {
            text = context.getString(R.string.want_to_get_more_)
            textSize = 17f
            typeface = ResourcesCompat.getFont(context, R.font.regular)
            setTextColor(ContextCompat.getColor(context, R.color.black))
            setPadding(50, 40, 50, 0)
        }
        builder.setView(messageView)

        builder.setPositiveButton(context.getString(R.string.View)) { dialog, _ ->
            val intent = Intent(context, PremiumActivity::class.java)
            context.startActivity(intent)
            dialog.dismiss()
        }
        builder.setNegativeButton(context.getString(R.string.cancel)) { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.apply {
                textSize = 17f
                setTextColor(ContextCompat.getColor(context, R.color.green))
                typeface = ResourcesCompat.getFont(context, R.font.medium)
                isAllCaps = false
            }
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.apply {
                textSize = 17f
                setTextColor(ContextCompat.getColor(context, R.color.black))
                typeface = ResourcesCompat.getFont(context, R.font.medium)
                isAllCaps = false
            }
        }

        dialog.show()
    }
}
