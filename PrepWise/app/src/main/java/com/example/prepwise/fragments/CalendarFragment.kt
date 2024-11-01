package com.example.prepwise.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prepwise.R
import com.example.prepwise.adapters.CalendarAdapter
import com.example.prepwise.adapters.Day
import java.time.LocalDate

class CalendarFragment : Fragment() {

    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var calendarAdapter: CalendarAdapter
    private val today: Int by lazy { LocalDate.now().dayOfMonth }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView)
        val days = generateDaysForMonth()
        calendarAdapter = CalendarAdapter(days) { day ->
            // клік на день
        }
        calendarRecyclerView.adapter = calendarAdapter
        calendarRecyclerView.layoutManager = GridLayoutManager(requireContext(), 7)

        return view
    }

    private fun generateDaysForMonth(): List<Day> {
        val days = mutableListOf<Day>()
        for (i in 1..30) {
            days.add(Day(i, i == 4 || i == 5, i == today)) // Позначте активні дні та поточний день
        }
        return days
    }
}
