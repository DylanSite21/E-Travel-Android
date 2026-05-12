package com.example.eticketing.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.eticketing.data.Message
import com.example.eticketing.databinding.ItemNotifikasiBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotifikasiAdapter : ListAdapter<Message, NotifikasiAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemNotifikasiBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message) {
            binding.tvIsiPesan.text = message.content
            val dateFormat = SimpleDateFormat("dd MMM yyyy HH:mm", Locale("id"))
            binding.tvWaktu.text = dateFormat.format(Date(message.timestamp))

            // Warna berbeda untuk pesan belum dibaca
            binding.root.setCardBackgroundColor(
                if (!message.isRead) Color.parseColor("#E3F2FD")
                else Color.WHITE
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNotifikasiBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Message, newItem: Message) = oldItem == newItem
    }
}