package com.example.eticketing.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eticketing.adapters.ChatListAdapter
import com.example.eticketing.data.AppDatabase
import com.example.eticketing.data.SessionManager
import com.example.eticketing.databinding.ActivityChatListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatListActivity : BaseActivity() {

    private lateinit var binding: ActivityChatListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBackButton("Chat")

        val userId = SessionManager.getUserId(this)
        val role = SessionManager.getUserRole(this)
        val db = AppDatabase.getDatabase(this)

        val adapter = ChatListAdapter { selectedUser ->
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("receiverId", selectedUser.id)
            intent.putExtra("receiverName", selectedUser.nama)
            startActivity(intent)
        }

        binding.rvChatList.layoutManager = LinearLayoutManager(this)
        binding.rvChatList.adapter = adapter

        // Load daftar user yang bisa di-chat berdasarkan role
        lifecycleScope.launch {
            val allUsers = withContext(Dispatchers.IO) { db.userDao().getAllUsers() }

            val chatableUsers = when (role) {
                "admin" -> allUsers.filter { it.id != userId && it.role != "admin" }
                "pengelola" -> allUsers.filter { it.id != userId }
                else -> allUsers.filter { it.id != userId &&
                        (it.role == "pengelola" || it.role == "admin") }
            }

            adapter.submitList(chatableUsers)
        }
    }
}