package com.example.prepwise.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prepwise.R
import com.example.prepwise.adapters.CalendarAdapter
import com.example.prepwise.adapters.Day
import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

class CalendarFragment : Fragment() {

    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var calendarAdapter: CalendarAdapter
    private lateinit var monthTextView: TextView
    private val today: Int by lazy { LocalDate.now().dayOfMonth }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView)
        monthTextView = view.findViewById(R.id.monthTextView)

        val days = generateDaysForMonth()
        calendarAdapter = CalendarAdapter(days) { day ->
            // клік на день
        }

        calendarRecyclerView.adapter = calendarAdapter
        calendarRecyclerView.layoutManager = GridLayoutManager(requireContext(), 7)

        // Встановлюємо назву поточного місяця з урахуванням мови
        val currentDate = LocalDate.now()
        val monthName = getMonthName(currentDate.month)
        monthTextView.text = monthName

        return view
    }

    private fun generateDaysForMonth(): List<Day> {
        val days = mutableListOf<Day>()

        // Отримуємо поточний рік і місяць
        val currentDate = LocalDate.now()
        val year = currentDate.year
        val month = currentDate.month

        // Визначаємо перший день місяця і кількість днів у місяці
        val firstDayOfMonth = LocalDate.of(year, month, 1)
        val daysInMonth = month.length(firstDayOfMonth.isLeapYear)

        // Визначаємо день тижня для першого числа місяця
        val dayOfWeekOffset = firstDayOfMonth.dayOfWeek.value % 7

        // Додаємо порожні дні для заповнення перед першим днем місяця
        for (i in 1..dayOfWeekOffset) {
            days.add(Day(0, isActive = false, isToday = false)) // Порожній день
        }

        // Додаємо реальні дні місяця
        for (i in 1..daysInMonth) {
            days.add(Day(i, i == 4 || i == 5, i == today))
        }

        return days
    }

    private fun getMonthName(month: Month): String {
        val ukrainianMonthNames = mapOf(
            Month.JANUARY to "Січень",
            Month.FEBRUARY to "Лютий",
            Month.MARCH to "Березень",
            Month.APRIL to "Квітень",
            Month.MAY to "Травень",
            Month.JUNE to "Червень",
            Month.JULY to "Липень",
            Month.AUGUST to "Серпень",
            Month.SEPTEMBER to "Вересень",
            Month.OCTOBER to "Жовтень",
            Month.NOVEMBER to "Листопад",
            Month.DECEMBER to "Грудень"
        )

        // Перевіряємо, чи мова пристрою українська
        return if (Locale.getDefault().language == "uk") {
            ukrainianMonthNames[month] ?: month.getDisplayName(TextStyle.FULL, Locale.getDefault())
        } else {
            month.getDisplayName(TextStyle.FULL, Locale.getDefault())
        }
    }
}

