package com.example.eticketing.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pengelola_requests")
data class PengelolaRequest(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val status: String = "pending", // pending/approved/rejected
    val timestamp: Long = System.currentTimeMillis()
)