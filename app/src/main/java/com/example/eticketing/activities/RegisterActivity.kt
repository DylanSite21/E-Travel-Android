package com.example.eticketing.activities

import android.content.Intent
import android.os.Bundle
import android.view.View // Wajib ditambahkan agar View.VISIBLE/GONE berfungsi
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.eticketing.R
import com.example.eticketing.data.AppDatabase
import com.example.eticketing.data.PengelolaRequest
import com.example.eticketing.data.User
import com.example.eticketing.databinding.ActivityRegisterBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : BaseActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = AppDatabase.getDatabase(this)
        val userDao = db.userDao()

        binding.btnRegister.setOnClickListener {
            val nama = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val whatsapp = binding.etWhatsappRegister.text.toString().trim()

            // Cek apakah pilih pengelola
            val wantsPengelola = binding.rgRole.checkedRadioButtonId == R.id.rbPengelola

            if (nama.isEmpty()) {
                binding.etName.error = "Nama tidak boleh kosong"
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
                val existing = withContext(Dispatchers.IO) { userDao.getUserByEmail(email) }

                if (existing != null) {
                    withContext(Dispatchers.Main) {
                        binding.etEmail.error = "Email sudah terdaftar"
                        Toast.makeText(this@RegisterActivity, "Email sudah digunakan", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                // Semua user baru selalu role "user" dulu
                val newUser = User(
                    nama = nama,
                    email = email,
                    password = password,
                    whatsapp = if (whatsapp.isNotEmpty()) whatsapp else null,
                    role = "user",
                    statusRequest = if (wantsPengelola) "pending" else "none"
                )

                withContext(Dispatchers.IO) {
                    userDao.register(newUser)

                    if (wantsPengelola) {
                        val savedUser = userDao.getUserByEmail(email)
                        savedUser?.let {
                            db.pengelolaRequestDao().insert(
                                PengelolaRequest(
                                    userId = it.id,
                                    timestamp = System.currentTimeMillis()
                                )
                            )

                            db.messageDao().sendMessage(
                                com.example.eticketing.data.Message(
                                    senderId = it.id,
                                    receiverId = 1L, // admin
                                    content = "Ada permintaan baru untuk menjadi pengelola dari ${it.nama}",
                                    type = "notification"
                                )
                            )
                        }
                    }
                }

                withContext(Dispatchers.Main) {
                    val msg = if (wantsPengelola)
                        "Akun berhasil dibuat! Permintaan pengelola Anda sedang menunggu persetujuan admin. Anda dapat login sebagai user sementara."
                    else
                        "Akun berhasil dibuat! Silakan login."

                    Toast.makeText(this@RegisterActivity, msg, Toast.LENGTH_LONG).show()
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    finish()
                }
            }
        }

        binding.tvLogin.setOnClickListener { finish() }

        // --- TAMBAHAN LOGIKA SHOW/HIDE INFO PENGELOLA ---
        binding.rgRole.setOnCheckedChangeListener { _, checkedId ->
            binding.tvInfoPengelola.visibility =
                if (checkedId == R.id.rbPengelola) View.VISIBLE else View.GONE
        }
    }
}