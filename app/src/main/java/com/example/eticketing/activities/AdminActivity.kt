package com.example.eticketing.activities

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

// AdminActivity: Dashboard untuk role "admin"
class AdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menggunakan Layout programatik
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(64, 64, 64, 64)
            setBackgroundColor(android.graphics.Color.WHITE)
        }

        val prefs = getSharedPreferences("session", MODE_PRIVATE)
        val nama = prefs.getString("userName", "Admin")

        val tvWelcome = TextView(this).apply {
            text = "👑 Selamat datang, $nama!\nRole: Admin"
            textSize = 20f
            setTextColor(android.graphics.Color.BLACK)
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 48)
        }

        val btnLogout = Button(this).apply {
            text = "Logout"
            setOnClickListener {
                // 1. Hapus session
                getSharedPreferences("session", MODE_PRIVATE).edit().clear().apply()

                // 2. Berpindah ke LoginActivity
                // Menggunakan context eksplisit agar tidak bingung dengan apply block
                val intent = Intent(this@AdminActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)

                // 3. Tutup activity ini
                finish()
            }
        }

        layout.addView(tvWelcome)
        layout.addView(btnLogout)
        setContentView(layout)
    }
}