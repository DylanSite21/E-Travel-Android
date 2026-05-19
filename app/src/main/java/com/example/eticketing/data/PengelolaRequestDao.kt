package com.example.eticketing.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PengelolaRequestDao {

    @Insert
    suspend fun insert(request: PengelolaRequest)

    // Ambil semua request yang pending — untuk ditampilkan ke admin
    @Query("SELECT * FROM pengelola_requests WHERE status = 'pending' ORDER BY timestamp DESC")
    fun getPendingRequests(): Flow<List<PengelolaRequest>>

    // Ambil request milik user tertentu
    @Query("SELECT * FROM pengelola_requests WHERE userId = :userId ORDER BY timestamp DESC LIMIT 1")
    suspend fun getRequestByUserId(userId: Long): PengelolaRequest?

    // Update status request
    @Query("UPDATE pengelola_requests SET status = :status WHERE userId = :userId")
    suspend fun updateStatus(userId: Long, status: String)
}