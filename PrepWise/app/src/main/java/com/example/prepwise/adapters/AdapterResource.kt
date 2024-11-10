package com.example.prepwise.adapters

import com.example.prepwise.models.Resource

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.prepwise.utils.DialogUtils
import com.example.prepwise.R
import com.example.prepwise.repositories.ResourceRepository
import com.example.prepwise.utils.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.time.format.DateTimeFormatter

class AdapterResource(private var resourceList: ArrayList<Resource>, private val context: Context, private val param: String = "report") :
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
        val delete: ImageView = itemView.findViewById(R.id.delete)
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
        holder.setLevel.text = resourse.level.name
        holder.setUsername.text = resourse.username
        holder.setCategory.text = resourse.category.name

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

        if(param!="report"){
            holder.report.visibility = View.GONE
            holder.delete.visibility = View.VISIBLE
        }
        else{
            holder.delete.visibility = View.GONE
            holder.report.visibility = View.VISIBLE
        }

        holder.setLike.setOnClickListener {
            if(!resourse.isLiked && !resourse.isDisLiked){
                resourse.isLiked = true
                resourse.numberOfLikes += 1
                CoroutineScope(Dispatchers.IO).launch {
                    ResourceRepository.addFavoriteResource(resourse.id, true)
                }
            }
            else if(resourse.isDisLiked){
                resourse.isDisLiked = false
                resourse.isLiked = true
                resourse.numberOfLikes += 1
                resourse.numberOfDislikes -= 1
                CoroutineScope(Dispatchers.IO).launch {
                    ResourceRepository.addFavoriteResource(resourse.id, true)
                }
            }
            else if(resourse.isLiked){
                resourse.numberOfLikes -= 1
                CoroutineScope(Dispatchers.IO).launch {
                    ResourceRepository.removeFavoriteResource(resourse.id)
                }
            }
        }

        holder.setDisLike.setOnClickListener {
            if(!resourse.isLiked && !resourse.isDisLiked){
                resourse.isDisLiked = true
                resourse.numberOfDislikes += 1
                CoroutineScope(Dispatchers.IO).launch {
                    ResourceRepository.addFavoriteResource(resourse.id, false)
                }
            }
            else if(resourse.isLiked){
                resourse.isDisLiked = true
                resourse.isLiked = false
                resourse.numberOfDislikes += 1
                resourse.numberOfLikes -= 1
                CoroutineScope(Dispatchers.IO).launch {
                    ResourceRepository.addFavoriteResource(resourse.id, false)
                }
            }
            else if(resourse.isDisLiked){
                resourse.numberOfDislikes -= 1
                CoroutineScope(Dispatchers.IO).launch {
                    ResourceRepository.removeFavoriteResource(resourse.id)
                }
            }
        }

        holder.report.setOnClickListener{
            DialogUtils.showReportDialog(context, context.getString(R.string.report_this_resource)) { selectedReason ->
                Toast.makeText(context, context.getString(R.string.report_sent_successfully_thank_you_for_your_help), Toast.LENGTH_SHORT).show()
            }
        }

        holder.delete.setOnClickListener {
            DialogUtils.showConfirmationDialog(
                context = context,
                message = context.getString(R.string.are_you_sure_you_want_to_delete_this_resource),
                positiveButtonText = context.getString(R.string.Delete),
                negativeButtonText = context.getString(R.string.cancel)
            ) { confirmed ->
                if (confirmed) {
                    CoroutineScope(Dispatchers.IO).launch {
                        delResource(resourceId = resourse.id)
                    }
                } else {

                }
            }
        }
    }

    suspend fun delResource(resourceId: Int): Response<Unit> {
        return try {
            val response = RetrofitInstance.api().deleteResource(resourceId)
            if (response.isSuccessful) {
                Log.d("ResourceRepository", "Ресурс успішно видлано")
            } else {
                Log.e("ResourceRepository", "Помилка при видалення ресурсу: ${response.message()}")
            }
            response
        } catch (e: Exception) {
            Log.e("ResourceRepository", "Exception: ${e.message}")
            throw e
        }
    }

    fun updateData(newList: List<Resource>) {
        resourceList = newList as ArrayList<Resource>
        notifyDataSetChanged() // Оновлення адаптера з новими даними
    }

    // Повертаємо кількість елементів у списку
    override fun getItemCount(): Int {
        return resourceList.size
    }
}
