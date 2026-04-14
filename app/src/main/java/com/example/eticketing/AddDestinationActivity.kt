package com.example.eticketing

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.eticketing.data.AppDatabase
import com.example.eticketing.data.Destination
import com.example.eticketing.databinding.ActivityAddDestinationBinding
import kotlinx.coroutines.launch

class AddDestinationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddDestinationBinding
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDestinationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getDatabase(this)

        binding.btnSaveDest.setOnClickListener {
            val name = binding.etDestName.text.toString()
            val location = binding.etDestLocation.text.toString()
            val priceStr = binding.etDestPrice.text.toString()
            val category = binding.etDestCategory.text.toString()
            val description = binding.etDestDescription.text.toString()

            if (name.isNotEmpty() && location.isNotEmpty() && priceStr.isNotEmpty() && category.isNotEmpty()) {
                val price = priceStr.toDoubleOrNull() ?: 0.0
                lifecycleScope.launch {
                    val destination = Destination(
                        name = name,
                        location = location,
                        price = price,
                        category = category,
                        description = description
                    )
                    db.destinationDao().insertDestination(destination)
                    Toast.makeText(this@AddDestinationActivity, "Destinasi Berhasil Disimpan", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } else {
                Toast.makeText(this, "Harap isi field yang wajib", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
