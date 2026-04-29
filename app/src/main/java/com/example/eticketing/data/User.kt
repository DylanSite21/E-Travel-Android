package com.example.eticketing.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Role yang tersedia:
// "admin"     → kelola semua data, lihat semua user
// "pengelola" → kelola destinasi miliknya sendiri
// "user"      → browse & booking destinasi

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nama: String,
    val email: String,
    val password: String,
    val role: String = "user"  // default role = user
)