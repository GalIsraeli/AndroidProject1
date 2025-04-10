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
    private val OBSTACLE_DELAY: Long = 1000
    private val TIMER_DELAY:Long = 100
    private val gameManager = GameManager(this)
    private lateinit var hearts: Array<AppCompatImageView>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Glide.with(this).load(R.drawable.space_background).into(binding.backgroundImg)
        playSoundEffect(R.raw.snd_com_mumble)

        binding.imgMoveLeft.setOnClickListener {
            gameManager.movePlayer(-1)
        }
        binding.imgMoveRight.setOnClickListener {
            gameManager.movePlayer(1)
        }

        hearts = arrayOf(
            binding.imgHeart1,
            binding.imgHeart2,
            binding.imgHeart3
        )
    }



    private fun uI_tick() {
        Log.d("pttt", "tick - " + Thread.currentThread().name)
        val currentTime = System.currentTimeMillis()
        binding.timerText.text = TimeFormatter.formatTime(currentTime - startTime)
        updateGameView()
    }
    private fun obstacleTick(){
        gameManager.tickObstacles()
    }



    private fun updateGameView() {
        gameManager.gameOver()
        updateObstacleField()
        updatePlayerField()
        updateScore()
        updateHealth()
    }

    private fun updateHealth() {
        val health = gameManager.getHealthPoints()

        for (i in hearts.indices) {
            if(i < health) {
                hearts[i].setImageResource(R.drawable.ic_heart)
            } else {
                hearts[i].setImageResource(0)
            }
        }
    }

    private fun updateScore() {
        binding.scoreText.text = "Score: "+gameManager.getTotalScore().toString()
    }

    private fun updatePlayerField() {

        val playerField: Array<Boolean> = gameManager.getPlayerField()
        val playerContainer:LinearLayoutCompat  = binding.playerObjectContainer
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

        val obstacleField: Array<Array<Obstacle?>> = gameManager.getObstacleField()
        val gameContainer: LinearLayoutCompat = binding.gameContainer
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
        // Tick Ui
        coroutineScope.launch {
            while (isActive) {
                uI_tick()
                delay(TIMER_DELAY)
            }
        }
        // Tick obstacles
        coroutineScope.launch {
            while (isActive) {
                obstacleTick()
                delay(OBSTACLE_DELAY)
            }
        }
    }

    private fun stopTimer() {
        coroutineScope.coroutineContext.cancelChildren()
    }
}