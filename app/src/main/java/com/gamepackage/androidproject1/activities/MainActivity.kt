package com.gamepackage.androidproject1.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.gamepackage.androidproject1.R
import com.gamepackage.androidproject1.databinding.ActivityMainBinding
import androidx.core.graphics.toColorInt

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var selectedSpeed: Boolean = false // false = slow, true = fast
    private var tiltMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Glide.with(this).load(R.drawable.space_background).into(binding.backgroundImg)
        val defaultColor1 = "#6200EE".toColorInt() // Orange for active
        val defaultColor2 = "#018786".toColorInt() // Orange for active
        val selectedColor = "#FF9800".toColorInt()  // Blue for inactive

        binding.btnSpeedMode.setOnClickListener {
            selectedSpeed = !selectedSpeed
            if (selectedSpeed) {
                binding.btnSpeedMode.setBackgroundColor(selectedColor)
            } else {
                binding.btnSpeedMode.setBackgroundColor(defaultColor1)
            }
        }

        binding.btnTiltMode.setOnClickListener {
            tiltMode = !tiltMode
            if (tiltMode) {
                binding.btnTiltMode.setBackgroundColor(selectedColor)
            } else {
                binding.btnTiltMode.setBackgroundColor(defaultColor2)
            }
        }

        binding.btnStart.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("selectedSpeed", selectedSpeed)
            intent.putExtra("tiltMode", tiltMode)
            startActivity(intent)
        }

    }
}