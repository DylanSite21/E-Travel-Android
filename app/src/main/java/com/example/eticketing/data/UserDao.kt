package com.example.eticketing.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun register(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    suspend fun login(email: String, password: String): User?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>

    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUser(userId: Long)

    @Query("SELECT * FROM users WHERE role = 'pengelola'")
    suspend fun getAllPengelola(): List<User>

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: Long): User?

    // Update foto profil
    @Query("UPDATE users SET photoPath = :path WHERE id = :userId")
    suspend fun updatePhoto(userId: Long, path: String)

    // Update nomor WA
    @Query("UPDATE users SET whatsapp = :whatsapp WHERE id = :userId")
    suspend fun updateWhatsapp(userId: Long, whatsapp: String)

    // Update status request pengelola
    @Query("UPDATE users SET statusRequest = :status WHERE id = :userId")
    suspend fun updateStatusRequest(userId: Long, status: String)

    // Update role user
    @Query("UPDATE users SET role = :role WHERE id = :userId")
    suspend fun updateRole(userId: Long, role: String)
}