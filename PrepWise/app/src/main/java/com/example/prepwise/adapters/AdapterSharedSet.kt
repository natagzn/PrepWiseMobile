package com.example.prepwise.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.prepwise.R
import com.example.prepwise.models.SharedSet
import java.time.format.DateTimeFormatter

class AdapterSharedSet(private val sharingSetList: ArrayList<SharedSet>, private val context: Context) :
    RecyclerView.Adapter<AdapterSharedSet.SetViewHolder>() {

    // ViewHolder клас для утримання посилань на UI елементи
    class SetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val setName: TextView = itemView.findViewById(R.id.set_name)
        val setLevel: TextView = itemView.findViewById(R.id.level)
        val setAuthorUsername: TextView = itemView.findViewById(R.id.username_author)
        val setNumberOfQuestions: TextView = itemView.findViewById(R.id.number_of_questions)
        val setDate: TextView = itemView.findViewById(R.id.date)
        val categoriesContainer: LinearLayout = itemView.findViewById(R.id.categories_container)
        val setLike: ImageView = itemView.findViewById(R.id.like)
        val setCoAuthors: TextView = itemView.findViewById(R.id.username_co_authors)
        val setType: TextView = itemView.findViewById(R.id.sharing_type)
    }

    // Створюємо новий ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_shared_set, parent, false)
        return SetViewHolder(view)
    }

    // Прив'язуємо дані до ViewHolder
    override fun onBindViewHolder(holder: SetViewHolder, position: Int) {
        val sharedSet = sharingSetList[position]

        holder.setName.text = sharedSet.name
        holder.setLevel.text = sharedSet.level
        holder.setAuthorUsername.text = sharedSet.username
        holder.setNumberOfQuestions.text = sharedSet.questions.size.toString()
        holder.setType.text = sharedSet.type
        holder.setCoAuthors.text = sharedSet.coAuthors.joinToString(", ") { it.username }

        // Форматуємо дату
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        holder.setDate.text = sharedSet.date.format(formatter)

        // Очищуємо контейнер категорій перед додаванням нових категорій
        holder.categoriesContainer.removeAllViews()

        // Додаємо категорії в контейнер
        for (category in sharedSet.categories) {
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

        // Налаштовуємо відображення лайків
        if(sharedSet.isLiked) {
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
                sharedSet.isLiked = true
                holder.setLike.setImageResource(R.drawable.save)
                holder.setLike.setTag(R.id.set_like_tag, R.drawable.save)
            } else {
                sharedSet.isLiked = false
                holder.setLike.setImageResource(R.drawable.not_save)
                holder.setLike.setTag(R.id.set_like_tag, R.drawable.not_save)
            }
        }
    }

    // Повертаємо кількість елементів у списку
    override fun getItemCount(): Int {
        return sharingSetList.size
    }

    // Функція для конвертації dp в пікселі
    fun dpToPx(dp: Int, context: Context): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }
}
