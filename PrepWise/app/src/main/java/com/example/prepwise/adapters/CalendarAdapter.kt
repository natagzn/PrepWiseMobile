package com.example.prepwise.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.prepwise.R

data class Day(val date: Int, val isActive: Boolean, val isToday: Boolean)

class CalendarAdapter(
    private val days: List<Day>,
    private val onDayClick: (Day) -> Unit
) : RecyclerView.Adapter<CalendarAdapter.DayViewHolder>() {

    inner class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dayNumber: TextView = itemView.findViewById(R.id.dayNumber)
        private val activityIcon: ImageView = itemView.findViewById(R.id.activityIcon)

        fun bind(day: Day) {
            if (day.date == 0) {
                dayNumber.text = ""
                itemView.setBackgroundResource(0)
            } else {
                dayNumber.text = day.date.toString()

                // Показуємо вогник для активних днів
                itemView.setBackgroundResource(if (day.isActive) R.drawable.fire else 0)

                // Показуємо точку для поточного дня
                activityIcon.visibility = if (day.isToday) View.VISIBLE else View.GONE
            }

            // Обробка кліку на день
            itemView.setOnClickListener { onDayClick(day) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.calendar_day_item, parent, false)
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        holder.bind(days[position])
    }

    override fun getItemCount(): Int = days.size
}

