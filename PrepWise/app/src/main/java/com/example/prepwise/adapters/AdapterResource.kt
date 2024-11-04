package com.example.prepwise.adapters

import com.example.prepwise.models.Resourse

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.prepwise.DialogUtils
import com.example.prepwise.R
import com.example.prepwise.models.Set
import java.time.format.DateTimeFormatter

class AdapterResource(private var resourceList: ArrayList<Resourse>, private val context: Context) :
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
        val report: ImageView = itemView.findViewById(R.id.report)
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

        holder.report.setOnClickListener{
            DialogUtils.showReportDialog(context, context.getString(R.string.report_this_resource)) { selectedReason ->
                Toast.makeText(context, context.getString(R.string.report_sent_successfully_thank_you_for_your_help), Toast.LENGTH_SHORT).show()
            }
        }

        holder.itemView.setOnLongClickListener {
            DialogUtils.showConfirmationDialog(
                context = context,
                message = context.getString(R.string.are_you_sure_you_want_to_delete_this_resource),
                positiveButtonText = context.getString(R.string.Delete),
                negativeButtonText = context.getString(R.string.cancel)
            ) { confirmed ->
                if (confirmed) {

                } else {

                }
            }
            true
        }
    }

    fun updateData(newList: List<Resourse>) {
        resourceList = newList as ArrayList<Resourse>
        notifyDataSetChanged() // Оновлення адаптера з новими даними
    }

    // Повертаємо кількість елементів у списку
    override fun getItemCount(): Int {
        return resourceList.size
    }
}
