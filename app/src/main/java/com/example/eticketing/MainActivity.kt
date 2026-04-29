package com.example.eticketing.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
// IMPORT R SECARA MANUAL AGAR MERAHNYA HILANG
import com.example.eticketing.R
import com.example.eticketing.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Inisialisasi ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 2. Setup Navigation Host
        // supportFragmentManager mencari Fragment di activity_main.xml
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as? NavHostFragment

        if (navHostFragment != null) {
            val navController = navHostFragment.navController

            // 3. Setup Bottom Navigation dengan NavController
            binding.bottomNavigation.setupWithNavController(navController)
        } else {
            // Jika crash disini, pastikan ID di activity_main.xml adalah nav_host_fragment
            throw IllegalStateException("NavHostFragment tidak ditemukan! Periksa ID di activity_main.xml")
        }
    }
}