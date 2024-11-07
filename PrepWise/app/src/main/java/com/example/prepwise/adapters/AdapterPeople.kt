package com.example.prepwise.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prepwise.R
import com.example.prepwise.activities.MainActivity.Companion.currentUser
import com.example.prepwise.fragments.UserProfileFragment
import com.example.prepwise.models.People

class AdapterPeople(private val userList: ArrayList<People>, private val context: Context, private val fragmentManager: FragmentManager) :
    RecyclerView.Adapter<AdapterPeople.SetViewHolder>() {

    // ViewHolder клас для утримання посилань на UI елементи
    class SetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val setUsername: TextView = itemView.findViewById(R.id.username)
        val setNumberOfSets: TextView = itemView.findViewById(R.id.number_of_sets)
        val setNumberOfResource: TextView = itemView.findViewById(R.id.number_of_resources)
        val setPeopleStatus: TextView = itemView.findViewById(R.id.people_status)
        val userInitialsView:TextView = itemView.findViewById(R.id.user_initials)
    }

    // Створюємо новий ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_people, parent, false)
        return SetViewHolder(view)
    }

    // Прив'язуємо дані до ViewHolder
    override fun onBindViewHolder(holder: SetViewHolder, position: Int) {
        val user = userList[position]

        holder.setUsername.text = user.username
        holder.setNumberOfSets.text = user.sets.size.toString()
        holder.setNumberOfResource.text = user.resouces.size.toString()
        holder.setPeopleStatus.text = context.getString(R.string.view_profile)

        // Додаємо клік на елемент
        holder.itemView.setOnClickListener {
            // Передаємо дані через Bundle у фрагмент
            val fragment = UserProfileFragment.newInstance(user.id)
            fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        // Встановлення фото профілю
        val (initials, backgroundColor) = generateAvatar(currentUser.username)
        holder.userInitialsView.text = initials

        val drawable = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            color = ColorStateList.valueOf(backgroundColor)
        }
        holder.userInitialsView.background = drawable
    }

    fun generateAvatar(username: String): Pair<String, Int> {
        val initials = if (username.isNotEmpty()) username.take(2).uppercase() else "N/A"

        // Генерація кольору на основі хешу імені
        val hash = username.fold(0) { acc, char -> acc + char.code }
        val hue = hash % 360
        val color = Color.HSVToColor(floatArrayOf(hue.toFloat(), 0.3f, 0.7f)) // Колір у форматі HSL

        return Pair(initials, color)
    }

    // Повертаємо кількість елементів у списку
    override fun getItemCount(): Int {
        return userList.size
    }
}