package com.example.eticketing.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.eticketing.data.AppDatabase
import com.example.eticketing.data.User
import com.example.eticketing.databinding.ActivityRegisterBinding
// IMPORT R SECARA MANUAL
import com.example.eticketing.R
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = AppDatabase.getDatabase(this)
        val userDao = db.userDao()

        binding.btnDaftar.setOnClickListener {
            val nama = binding.etNama.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            // Tentukan role dari RadioGroup
            val role = when (binding.rgRole.checkedRadioButtonId) {
                R.id.rbPengelola -> "pengelola"
                else -> "user"
            }

            // Validasi input
            if (nama.isEmpty()) {
                binding.etNama.error = "Nama tidak boleh kosong"
                return@setOnClickListener
            }
            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.etEmail.error = "Email tidak valid"
                return@setOnClickListener
            }
            if (password.length < 6) {
                binding.etPassword.error = "Password minimal 6 karakter"
                return@setOnClickListener
            }

            lifecycleScope.launch {
                // Cek email sudah terdaftar
                val existing = userDao.getUserByEmail(email)
                if (existing != null) {
                    runOnUiThread {
                        binding.etEmail.error = "Email sudah terdaftar"
                        Toast.makeText(this@RegisterActivity, "Email sudah digunakan", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                // Simpan user baru
                val newUser = User(
                    nama = nama,
                    email = email,
                    password = password,
                    role = role
                )
                userDao.register(newUser)

                runOnUiThread {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Akun berhasil dibuat! Silakan login.",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    finish()
                }
            }
        }

        // Link kembali ke login
        binding.tvLogin.setOnClickListener {
            finish()
        }
    }
}
