package com.example.prepwise.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.prepwise.R
import com.example.prepwise.models.HelpAnswer
import com.example.prepwise.models.People
import java.time.format.DateTimeFormatter

class AdapterFriendHelp(private val friends: List<People>, private val context: Context) :
    RecyclerView.Adapter<AdapterFriendHelp.AnswerViewHolder>() {

    class AnswerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username: TextView = itemView.findViewById(R.id.username)
        val sendRequest: TextView = itemView.findViewById(R.id.friend_status)
        val imgSend: ImageView = itemView.findViewById(R.id.img_send)
        val btnSend: LinearLayout = itemView.findViewById(R.id.btn_accept)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_friend, parent, false)
        return AnswerViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        val friend = friends[position]
        holder.sendRequest.text = context.getString(R.string.send_request)
        holder.username.text = friend.username

        holder.btnSend.setOnClickListener{
            holder.imgSend.visibility =View.VISIBLE
            holder.sendRequest.visibility = View.GONE
        }
    }

    override fun getItemCount() = friends.size
}