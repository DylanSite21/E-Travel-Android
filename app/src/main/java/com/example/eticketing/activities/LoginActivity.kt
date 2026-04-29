package com.example.eticketing.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.eticketing.data.AppDatabase
import com.example.eticketing.databinding.ActivityLoginBinding
// Tambahkan Import Manual di bawah ini untuk memastikan
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = AppDatabase.getDatabase(this)
        val userDao = db.userDao()

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Isi semua bidang!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val user = withContext(Dispatchers.IO) { userDao.login(email, password) }

                if (user != null) {
                    val prefs = getSharedPreferences("session", MODE_PRIVATE)
                    prefs.edit().putString("userRole", user.role).apply()

                    val intent = when (user.role) {
                        "admin" -> Intent(this@LoginActivity, AdminActivity::class.java)
                        "pengelola" -> Intent(this@LoginActivity, PengelolaActivity::class.java)
                        else -> Intent(this@LoginActivity, MainActivity::class.java)
                    }
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Login Gagal", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.tvDaftar.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
