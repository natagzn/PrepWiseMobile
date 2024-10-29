package com.example.prepwise.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prepwise.R
import com.example.prepwise.fragments.ViewSetFragment
import com.example.prepwise.models.Set
import java.time.format.DateTimeFormatter

class AdapterSetInFolder(
    private val setList: ArrayList<Set>,
    private val context: Context,
    private val fragmentManager: FragmentManager
) : RecyclerView.Adapter<AdapterSetInFolder.SetViewHolder>() {

    // ViewHolder клас для утримання посилань на UI елементи
    class SetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val setName: TextView = itemView.findViewById(R.id.set_name)
        val setNumberOfQuestions: TextView = itemView.findViewById(R.id.number_of_questions)
        val setDate: TextView = itemView.findViewById(R.id.date)
    }

    // Створюємо новий ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_add_set, parent, false)
        return SetViewHolder(view)
    }

    // Прив'язуємо дані до ViewHolder
    override fun onBindViewHolder(holder: SetViewHolder, position: Int) {
        val set = setList[position]

        holder.setName.text = set.name
        holder.setNumberOfQuestions.text = set.questions.size.toString()

        // Форматуємо дату
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        holder.setDate.text = set.date.format(formatter)

        // Додаємо клік на елемент
        holder.itemView.setOnClickListener {
            // Передаємо дані через Bundle у фрагмент
            val fragment = ViewSetFragment.newInstance(set.id)
            fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        holder.itemView.findViewById<ImageView>(R.id.add_delete_to_folder).visibility = View.GONE
    }

    // Повертаємо кількість елементів у списку
    override fun getItemCount(): Int {
        return setList.size
    }
}
