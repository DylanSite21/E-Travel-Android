package com.example.eticketing.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TicketDao {
    @Insert
    suspend fun bookTicket(ticket: Ticket)

    @Query("SELECT * FROM tickets WHERE userId = :userId ORDER BY bookingDate DESC")
    fun getTicketsByUserId(userId: Long): Flow<List<Ticket>>

    @Query("SELECT * FROM tickets ORDER BY bookingDate DESC")
    fun getAllTicketsAdmin(): Flow<List<Ticket>>
}
