package com.gamepackage.androidproject1.activities

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import com.bumptech.glide.Glide
import com.gamepackage.androidproject1.R
import com.gamepackage.androidproject1.databinding.ActivityGameBinding
import com.gamepackage.androidproject1.logic.GameManager
import com.gamepackage.androidproject1.logic.Obstacle
import com.gamepackage.androidproject1.logic.ObstacleType
import com.gamepackage.androidproject1.utils.TimeFormatter
import com.gamepackage.androidproject1.utils.playSoundEffect
import kotlinx.coroutines.*

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private val startTime = System.currentTimeMillis()
    private val DELAY: Long = 1000
    private val gameManager = GameManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Glide.with(this).load(R.drawable.space_background).into(binding.backgroundImg)
        playSoundEffect(R.raw.snd_com_mumble)
    }

    private fun tick() {
        Log.d("pttt", "tick - " + Thread.currentThread().name)
        val currentTime = System.currentTimeMillis()
        binding.timerText.text = TimeFormatter.formatTime(currentTime - startTime)
        updateGameView()
        gameManager.tickObstacles()

    }

    private fun updateGameView() {

        updateObstacleField()
        updatePlayerField()

    }

    private fun updatePlayerField() {

        var playerField: Array<Boolean> = gameManager.getPlayerField()
        var playerContainer:LinearLayoutCompat  = binding.playerObjectContainer
        playerContainer.removeAllViews()

        for (lane in playerField.indices) {
            val cellView = AppCompatImageView(this).apply {
                layoutParams = LinearLayoutCompat.LayoutParams(
                    0,
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                    1f
                )
                scaleType = ImageView.ScaleType.FIT_CENTER
                if(playerField[lane]){
                    setImageResource(R.drawable.img_spaceship)
                }
            }
            playerContainer.addView(cellView)
        }

    }

    private fun updateObstacleField() {

        var obstacleField: Array<Array<Obstacle?>> = gameManager.getObstacleField()
        var gameContainer: LinearLayoutCompat = binding.gameContainer
        gameContainer.removeAllViews()

        for (row in obstacleField.indices) {
            val rowLayout = LinearLayoutCompat(this).apply {
                layoutParams = LinearLayoutCompat.LayoutParams(
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                    0,
                    1f
                )
                orientation = LinearLayoutCompat.HORIZONTAL
            }

            for (obstacle in obstacleField[row]) {
                val cellView = AppCompatImageView(this).apply {
                    layoutParams = LinearLayoutCompat.LayoutParams(
                        0,
                        LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                        1f
                    )
                    scaleType = ImageView.ScaleType.FIT_CENTER

                    // Set image based on whether there's an obstacle or not
                    when (obstacle?.type) {
                        ObstacleType.ENEMY -> setImageResource(R.drawable.img_alien1)
                        //ObstacleType.BONUS -> setImageResource(R.drawable.img_bonus)
                        else -> setImageDrawable(null) // Empty cell
                    }
                }
                rowLayout.addView(cellView)
            }

            gameContainer.addView(rowLayout)
        }
    }

    override fun onStart() {
        super.onStart()
        startTimer()
    }

    override fun onStop() {
        super.onStop()
        stopTimer()
    }
    //////////////////////////////// START STOP CODE ////////////////////////////////

    private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())

    private fun startTimer() {
        coroutineScope.launch {
            while (isActive) {
                tick()
                delay(DELAY)
            }
        }
    }

    private fun stopTimer() {
        coroutineScope.cancel()
    }
}