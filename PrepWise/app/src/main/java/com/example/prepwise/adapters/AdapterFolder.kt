package com.example.prepwise.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.prepwise.R
import com.example.prepwise.models.Folder
import java.time.format.DateTimeFormatter

class AdapterFolder(private val folderList: ArrayList<Folder>, private val context: Context) :
    RecyclerView.Adapter<AdapterFolder.SetViewHolder>() {

    // ViewHolder клас для утримання посилань на UI елементи
    class SetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val setName: TextView = itemView.findViewById(R.id.folder_name)
        val setDate: TextView = itemView.findViewById(R.id.date)
        val setNumberOfSets: TextView = itemView.findViewById(R.id.number_of_sets)
        val setLike: ImageView = itemView.findViewById(R.id.like)
    }

    // Створюємо новий ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_folder, parent, false)
        return SetViewHolder(view)
    }

    // Прив'язуємо дані до ViewHolder
    override fun onBindViewHolder(holder: SetViewHolder, position: Int) {
        val folder = folderList[position]

        holder.setName.text = folder.name
        holder.setNumberOfSets.text = folder.sets.size.toString()

        // Форматуємо дату
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        holder.setDate.text = folder.date.format(formatter)

        // Налаштовуємо відображення лайків
        if(folder.isLiked) {
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
                folder.isLiked = true
                holder.setLike.setImageResource(R.drawable.save)
                holder.setLike.setTag(R.id.set_like_tag, R.drawable.save)
            } else {
                folder.isLiked = false
                holder.setLike.setImageResource(R.drawable.not_save)
                holder.setLike.setTag(R.id.set_like_tag, R.drawable.not_save)
            }
        }
    }

    // Повертаємо кількість елементів у списку
    override fun getItemCount(): Int {
        return folderList.size
    }
}