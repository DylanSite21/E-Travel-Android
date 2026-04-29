package com.example.eticketing.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

// PengelolaActivity: Dashboard untuk role "pengelola"
// Tahap berikutnya akan diisi dengan: tambah/edit/hapus destinasi, kelola tiket, dll.
class PengelolaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = android.widget.LinearLayout(this).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            gravity = android.view.Gravity.CENTER
            setPadding(64, 64, 64, 64)
        }

        val prefs = getSharedPreferences("session", MODE_PRIVATE)
        val nama = prefs.getString("userName", "Pengelola")

        val tvWelcome = TextView(this).apply {
            text = "🏨 Selamat datang, $nama!\nRole: Pengelola"
            textSize = 20f
            gravity = android.view.Gravity.CENTER
        }

        val btnLogout = Button(this).apply {
            text = "Logout"
            setOnClickListener {
                getSharedPreferences("session", MODE_PRIVATE).edit().clear().apply()
                startActivity(Intent(this@PengelolaActivity, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
            }
        }

        layout.addView(tvWelcome)
        layout.addView(btnLogout)
        setContentView(layout)
    }
}