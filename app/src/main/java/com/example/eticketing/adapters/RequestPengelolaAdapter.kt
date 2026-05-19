package com.example.eticketing.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.eticketing.data.PengelolaRequest
import com.example.eticketing.databinding.ItemRequestPengelolaBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RequestPengelolaAdapter(
    private val onApprove: (PengelolaRequest) -> Unit,
    private val onReject: (PengelolaRequest) -> Unit
) : ListAdapter<PengelolaRequest, RequestPengelolaAdapter.ViewHolder>(DiffCallback()) {

    var userNames: Map<Long, String> = emptyMap()

    inner class ViewHolder(private val binding: ItemRequestPengelolaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(request: PengelolaRequest) {
            val dateFormat = SimpleDateFormat("dd MMM yyyy HH:mm", Locale("id"))
            binding.tvNamaUser.text = userNames[request.userId] ?: "User #${request.userId}"
            binding.tvTanggal.text = "📅 ${dateFormat.format(Date(request.timestamp))}"
            binding.tvStatus.text = "Status: ${request.status.replaceFirstChar { it.uppercase() }}"
            binding.btnApprove.setOnClickListener { onApprove(request) }
            binding.btnReject.setOnClickListener { onReject(request) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRequestPengelolaBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<PengelolaRequest>() {
        override fun areItemsTheSame(oldItem: PengelolaRequest, newItem: PengelolaRequest) =
            oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: PengelolaRequest, newItem: PengelolaRequest) =
            oldItem == newItem
    }
}