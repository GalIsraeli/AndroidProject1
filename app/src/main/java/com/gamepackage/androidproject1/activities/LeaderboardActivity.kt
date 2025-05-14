package com.gamepackage.androidproject1.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.gamepackage.androidproject1.R
import com.gamepackage.androidproject1.databinding.ActivityLeaderboardBinding

class LeaderboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLeaderboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeaderboardBinding.inflate(layoutInflater)
        setContentView(binding.root)


        Glide.with(this).load(R.drawable.space_background).into(binding.backgroundImg)
        val score = intent.getIntExtra("score", 0)
        binding.scoreValText.text = score.toString()

        binding.btnBackToMenu.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish() // close LeaderboardActivity too
        }
    }
}
