package com.example.eticketing.activities

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eticketing.adapters.RequestPengelolaAdapter
import com.example.eticketing.data.AppDatabase
import com.example.eticketing.data.Message
import com.example.eticketing.databinding.ActivityRequestPengelolaBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RequestPengelolaActivity : BaseActivity() {

    private lateinit var binding: ActivityRequestPengelolaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestPengelolaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBackButton("Request Pengelola")

        val db = AppDatabase.getDatabase(this)

        val adapter = RequestPengelolaAdapter(
            onApprove = { request ->
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        // Update status request
                        db.pengelolaRequestDao().updateStatus(request.userId, "approved")

                        // Update role user jadi pengelola
                        db.userDao().updateRole(request.userId, "pengelola")
                        db.userDao().updateStatusRequest(request.userId, "approved")

                        // Kirim notifikasi ke user
                        db.messageDao().sendMessage(
                            Message(
                                senderId = 1L, // admin
                                receiverId = request.userId,
                                content = "🎉 Selamat! Permintaan Anda untuk menjadi Pengelola telah disetujui. Silakan login ulang untuk mengakses fitur pengelola.",
                                type = "notification"
                            )
                        )
                    }
                    Toast.makeText(this@RequestPengelolaActivity, "Request disetujui!", Toast.LENGTH_SHORT).show()
                }
            },
            onReject = { request ->
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        // Update status request
                        db.pengelolaRequestDao().updateStatus(request.userId, "rejected")
                        db.userDao().updateStatusRequest(request.userId, "rejected")

                        // Kirim notifikasi penolakan ke user
                        db.messageDao().sendMessage(
                            Message(
                                senderId = 1L,
                                receiverId = request.userId,
                                content = "❌ Mohon maaf, permintaan Anda untuk menjadi Pengelola belum dapat disetujui saat ini. Silakan hubungi admin untuk informasi lebih lanjut.",
                                type = "notification"
                            )
                        )
                    }
                    Toast.makeText(this@RequestPengelolaActivity, "Request ditolak", Toast.LENGTH_SHORT).show()
                }
            }
        )

        binding.rvRequest.layoutManager = LinearLayoutManager(this)
        binding.rvRequest.adapter = adapter

        lifecycleScope.launch {
            db.pengelolaRequestDao().getPendingRequests().collect { requests ->
                // Load nama user untuk setiap request
                val userMap = mutableMapOf<Long, String>()
                withContext(Dispatchers.IO) {
                    requests.forEach { req ->
                        val user = db.userDao().getUserById(req.userId)
                        userMap[req.userId] = user?.nama ?: "User #${req.userId}"
                    }
                }
                adapter.userNames = userMap
                adapter.submitList(requests)

                binding.tvEmpty.visibility =
                    if (requests.isEmpty()) android.view.View.VISIBLE
                    else android.view.View.GONE
            }
        }
    }
}