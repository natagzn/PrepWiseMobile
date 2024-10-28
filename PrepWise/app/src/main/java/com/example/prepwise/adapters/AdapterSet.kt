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

class AdapterSet(
    private val setList: ArrayList<Set>,
    private val context: Context,
    private val fragmentManager: FragmentManager,
    val param: String
) : RecyclerView.Adapter<AdapterSet.SetViewHolder>() {

    // ViewHolder клас для утримання посилань на UI елементи
    class SetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val setName: TextView = itemView.findViewById(R.id.set_name)
        val setLevel: TextView = itemView.findViewById(R.id.level)
        val setUsername: TextView = itemView.findViewById(R.id.username)
        val setNumberOfQuestions: TextView = itemView.findViewById(R.id.number_of_questions)
        val setDate: TextView = itemView.findViewById(R.id.date)
        val elemAccess: LinearLayout = itemView.findViewById(R.id.visisibility)
        val setAccessType: TextView = itemView.findViewById(R.id.access_type)
        val setAccessImg: ImageView = itemView.findViewById(R.id.access_img)
        val categoriesContainer: LinearLayout = itemView.findViewById(R.id.categories_container)
        val setLike: ImageView = itemView.findViewById(R.id.like)
    }

    // Створюємо новий ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_set, parent, false)
        return SetViewHolder(view)
    }

    // Прив'язуємо дані до ViewHolder
    override fun onBindViewHolder(holder: SetViewHolder, position: Int) {
        val set = setList[position]

        holder.setName.text = set.name
        holder.setLevel.text = set.level
        holder.setUsername.text = set.username
        holder.setNumberOfQuestions.text = set.questions.size.toString()

        // Форматуємо дату
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        holder.setDate.text = set.date.format(formatter)

        // Налаштування доступу (public/private)
        holder.setAccessType.text = set.access
        if (set.access.toLowerCase() == "public") {
            holder.setAccessImg.setImageResource(R.drawable.resource_public)
        } else {
            holder.setAccessImg.setImageResource(R.drawable.resource_private)
        }

        holder.categoriesContainer.removeAllViews()

        // Додаємо категорії в контейнер
        for (category in set.categories) {
            val categoryTextView = TextView(context)
            categoryTextView.text = category
            categoryTextView.setBackgroundResource(R.drawable.blue_rounded_background)
            categoryTextView.setPadding(
                dpToPx(10, context), dpToPx(2, context),
                dpToPx(10, context), dpToPx(2, context)
            )
            categoryTextView.setTextColor(ContextCompat.getColor(context, R.color.black))
            categoryTextView.setTextAppearance(R.style.medium_11)

            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(dpToPx(10, context), 0, 0, 0)
            categoryTextView.layoutParams = layoutParams

            holder.categoriesContainer.addView(categoryTextView)
        }

        if(param=="without access") holder.elemAccess.visibility = View.GONE
        else holder.elemAccess.visibility = View.VISIBLE

        // Налаштовуємо відображення лайків
        if(set.isLiked) {
            holder.setLike.setImageResource(R.drawable.save)
            holder.setLike.setTag(R.id.set_like_tag, R.drawable.save)
        }
        else{
            holder.setLike.setImageResource(R.drawable.not_save)
            holder.setLike.setTag(R.id.set_like_tag, R.drawable.not_save)
        }
        holder.setLike.setOnClickListener {
            val currentTag = holder.setLike.getTag(R.id.set_like_tag) as? Int
            if (currentTag == R.drawable.not_save) {
                set.isLiked = true
                holder.setLike.setImageResource(R.drawable.save)
                holder.setLike.setTag(R.id.set_like_tag, R.drawable.save)
            } else {
                set.isLiked = false
                holder.setLike.setImageResource(R.drawable.not_save)
                holder.setLike.setTag(R.id.set_like_tag, R.drawable.not_save)
            }
        }

        // Додаємо клік на елемент
        holder.itemView.setOnClickListener {
            // Передаємо дані через Bundle у фрагмент
            val fragment = ViewSetFragment.newInstance(set.id)
            fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    // Повертаємо кількість елементів у списку
    override fun getItemCount(): Int {
        return setList.size
    }

    // Функція для конвертації dp в пікселі
    fun dpToPx(dp: Int, context: Context): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }
}
