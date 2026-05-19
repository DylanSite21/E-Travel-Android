package com.example.eticketing.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Insert
    suspend fun sendMessage(message: Message)

    // Ambil semua pesan antara 2 user (chat)
    @Query("""
        SELECT * FROM messages 
        WHERE (senderId = :userId1 AND receiverId = :userId2)
        OR (senderId = :userId2 AND receiverId = :userId1)
        ORDER BY timestamp ASC
    """)
    fun getChat(userId1: Long, userId2: Long): Flow<List<Message>>

    // Ambil semua notifikasi untuk user tertentu
    @Query("""
        SELECT * FROM messages 
        WHERE receiverId = :userId AND type = 'notification'
        ORDER BY timestamp DESC
    """)
    fun getNotifications(userId: Long): Flow<List<Message>>

    // Ambil semua conversation (list chat) milik user
    @Query("""
        SELECT * FROM messages 
        WHERE (senderId = :userId OR receiverId = :userId)
        AND type = 'chat'
        ORDER BY timestamp DESC
    """)
    fun getConversations(userId: Long): Flow<List<Message>>

    // Hitung pesan belum dibaca
    @Query("""
        SELECT COUNT(*) FROM messages 
        WHERE receiverId = :userId AND isRead = 0
    """)
    fun getUnreadCount(userId: Long): Flow<Int>

    // Tandai pesan sudah dibaca
    @Query("UPDATE messages SET isRead = 1 WHERE receiverId = :userId AND senderId = :senderId")
    suspend fun markAsRead(userId: Long, senderId: Long)

    // Tandai semua notifikasi sudah dibaca
    @Query("UPDATE messages SET isRead = 1 WHERE receiverId = :userId AND type = 'notification'")
    suspend fun markAllNotificationsRead(userId: Long)
}