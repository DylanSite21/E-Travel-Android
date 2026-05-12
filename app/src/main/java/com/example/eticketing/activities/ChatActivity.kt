package com.example.eticketing.activities

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eticketing.adapters.ChatAdapter
import com.example.eticketing.data.AppDatabase
import com.example.eticketing.data.Message
import com.example.eticketing.data.SessionManager
import com.example.eticketing.databinding.ActivityChatBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatActivity : BaseActivity() {

    private lateinit var binding: ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val receiverId = intent.getLongExtra("receiverId", -1L)
        val receiverName = intent.getStringExtra("receiverName") ?: "Chat"
        setupBackButton(receiverName)

        val myId = SessionManager.getUserId(this)
        val db = AppDatabase.getDatabase(this)

        val adapter = ChatAdapter(myId)
        binding.rvChat.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true
        }
        binding.rvChat.adapter = adapter

        // Load pesan
        lifecycleScope.launch {
            db.messageDao().getChat(myId, receiverId).collect { messages ->
                adapter.submitList(messages)
                // Scroll ke bawah
                if (messages.isNotEmpty()) {
                    binding.rvChat.scrollToPosition(messages.size - 1)
                }
                // Tandai sudah dibaca
                withContext(Dispatchers.IO) {
                    db.messageDao().markAsRead(myId, receiverId)
                }
            }
        }

        // Kirim pesan
        binding.btnKirim.setOnClickListener {
            val text = binding.etPesan.text.toString().trim()
            if (text.isEmpty()) return@setOnClickListener

            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    db.messageDao().sendMessage(
                        Message(
                            senderId = myId,
                            receiverId = receiverId,
                            content = text,
                            type = "chat"
                        )
                    )
                }
                binding.etPesan.setText("")
            }
        }
    }
}