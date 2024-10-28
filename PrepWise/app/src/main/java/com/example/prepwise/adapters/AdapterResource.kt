package com.example.prepwise.adapters

import com.example.prepwise.models.Resourse

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
import java.time.format.DateTimeFormatter

class AdapterResource(private val resourceList: ArrayList<Resourse>, private val context: Context) :
    RecyclerView.Adapter<AdapterResource.SetViewHolder>() {

    // ViewHolder клас для утримання посилань на UI елементи
    class SetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val setArticleBook: TextView = itemView.findViewById(R.id.article_book)
        val setDescription: TextView = itemView.findViewById(R.id.description)
        val setLevel: TextView = itemView.findViewById(R.id.level)
        val setUsername: TextView = itemView.findViewById(R.id.username)
        val setDate: TextView = itemView.findViewById(R.id.date)
        val setCategory: TextView = itemView.findViewById(R.id.category)
        val setLike: ImageView = itemView.findViewById(R.id.like)
        val setDisLike: ImageView = itemView.findViewById(R.id.dislike)
        val numberOfLikes: TextView = itemView.findViewById(R.id.number_of_likes)
        val numberOfDislikes: TextView = itemView.findViewById(R.id.number_of_dislikes)
    }

    // Створюємо новий ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_resource, parent, false)
        return SetViewHolder(view)
    }

    // Прив'язуємо дані до ViewHolder
    override fun onBindViewHolder(holder: SetViewHolder, position: Int) {
        val resourse = resourceList[position]

        holder.setArticleBook.text = resourse.articleBook
        holder.setDescription.text = resourse.description
        holder.setLevel.text = resourse.level
        holder.setUsername.text = resourse.username
        holder.setCategory.text = resourse.category

        // Форматуємо дату
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        holder.setDate.text = resourse.date.format(formatter)

        // Встановлюємо кількість лайків і дизлайків
        holder.numberOfLikes.text = resourse.numberOfLikes.toString()
        holder.numberOfDislikes.text = resourse.numberOfDislikes.toString()

        // Відображаємо стан лайків
        if (resourse.isLiked) {
            holder.setLike.setImageResource(R.drawable.checkedlike)
        } else {
            holder.setLike.setImageResource(R.drawable.like)
        }

        if (resourse.isDisLiked) {
            holder.setDisLike.setImageResource(R.drawable.checkeddis)
        } else {
            holder.setDisLike.setImageResource(R.drawable.dislike)
        }

        // Обробляємо натискання лайків
        holder.setLike.setOnClickListener {
            if (!resourse.isLiked) {
                resourse.isLiked = true
                resourse.isDisLiked = false
                resourse.numberOfLikes++
                resourse.numberOfDislikes--
            } else {
                resourse.isLiked = false
                resourse.numberOfLikes--
            }
            notifyItemChanged(position)
        }

        // Обробляємо натискання дизлайків
        holder.setDisLike.setOnClickListener {
            if (!resourse.isDisLiked) {
                resourse.isDisLiked = true
                resourse.isLiked = false
                resourse.numberOfDislikes++
                resourse.numberOfLikes--
            } else {
                resourse.isDisLiked = false
                resourse.numberOfDislikes--
            }
            notifyItemChanged(position)
        }
    }

    // Повертаємо кількість елементів у списку
    override fun getItemCount(): Int {
        return resourceList.size
    }
}
