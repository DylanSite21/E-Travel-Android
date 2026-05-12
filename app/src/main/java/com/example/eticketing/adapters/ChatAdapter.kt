package com.example.eticketing.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.eticketing.data.Message
import com.example.eticketing.databinding.ItemChatMessageBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatAdapter(
    private val myId: Long
) : ListAdapter<Message, ChatAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemChatMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message) {
            val isMine = message.senderId == myId
            val dateFormat = SimpleDateFormat("HH:mm", Locale("id"))

            binding.tvPesanSaya.visibility = if (isMine) View.VISIBLE else View.GONE
            binding.tvPesanLawan.visibility = if (!isMine) View.VISIBLE else View.GONE
            binding.tvWaktuSaya.visibility = if (isMine) View.VISIBLE else View.GONE
            binding.tvWaktuLawan.visibility = if (!isMine) View.VISIBLE else View.GONE

            if (isMine) {
                binding.tvPesanSaya.text = message.content
                binding.tvWaktuSaya.text = dateFormat.format(Date(message.timestamp))
            } else {
                binding.tvPesanLawan.text = message.content
                binding.tvWaktuLawan.text = dateFormat.format(Date(message.timestamp))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemChatMessageBinding.inflate(
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