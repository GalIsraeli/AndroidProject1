package com.gamepackage.androidproject1.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
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

class GameActivity : AppCompatActivity() , SensorEventListener {

    private lateinit var binding: ActivityGameBinding
    private val startTime = System.currentTimeMillis()
    private var OBSTACLE_DELAY: Long = 1000
    private var TILT_MODE: Boolean = false
    private val TIMER_DELAY:Long = 100
    private lateinit var gameManager: GameManager
    private lateinit var hearts: Array<AppCompatImageView>
    private var backgroundMusicPlayer: MediaPlayer? = null
    private val NUM_LANES : Int = 5
    private val NUM_ROWS : Int = 10

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Glide.with(this).load(R.drawable.space_background).into(binding.backgroundImg)
        playSoundEffect(R.raw.snd_com_mumble)
        gameManager = GameManager(numRow = NUM_ROWS, numLane = NUM_LANES)

        val speedMode = intent.getBooleanExtra("SPEED_MODE", false)
        val tiltMode = intent.getBooleanExtra("TILT_MODE", false)
        if(tiltMode){
            TILT_MODE = true
        }
        if(speedMode){
            OBSTACLE_DELAY = 500
        }

        if(TILT_MODE){
            setupTilt()
        }else{
            setupButtons()
        }

        hearts = arrayOf(
            binding.imgHeart1,
            binding.imgHeart2,
            binding.imgHeart3
        )
    }

    private fun setupTilt() {
        binding.buttonContainer.visibility = View.GONE;
        // adjusting view to fit screen
        val params = binding.playerObjectContainer.layoutParams as RelativeLayout.LayoutParams
        params.removeRule(RelativeLayout.ABOVE)
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        binding.playerObjectContainer.layoutParams = params
        //setting up sensor
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    private fun setupButtons() {
        binding.imgMoveLeft.setOnClickListener {
            gameManager.movePlayer(-1)
        }
        binding.imgMoveRight.setOnClickListener {
            gameManager.movePlayer(1)
        }
    }

    private fun uiTick() {
        Log.d("pttt", "tick - " + Thread.currentThread().name)
        val currentTime = System.currentTimeMillis()
        binding.timerText.text = TimeFormatter.formatTime(currentTime - startTime)
        updateGameView()
    }

    private fun obstacleTick(){
        gameManager.tickObstacles()
    }

    private fun updateGameView() {
        if(gameManager.gameOver()){
            stopTimer()
            stopBackgroundMusic()
            val intent = Intent(this, LeaderboardActivity::class.java)
            intent.putExtra("score", gameManager.getTotalScore())
            startActivity(intent)
            finish() // remove GameActivity from the back stack
        }
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

    @SuppressLint("SetTextI18n")
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
                        ObstacleType.BONUS -> setImageResource(R.drawable.img_coin)
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

    override fun onResume() {
        super.onResume()
        startBackgroundMusic()
        if (TILT_MODE) {
            accelerometer?.also {
                sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        stopBackgroundMusic()
        if (TILT_MODE) {
            sensorManager.unregisterListener(this)
        }
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
                uiTick()
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

    private fun startBackgroundMusic() {
        backgroundMusicPlayer = MediaPlayer.create(this, R.raw.snd_background_music)
        backgroundMusicPlayer?.isLooping = true
        backgroundMusicPlayer?.start()
    }

    private fun stopBackgroundMusic() {
        backgroundMusicPlayer?.stop()
        backgroundMusicPlayer?.release()
        backgroundMusicPlayer = null
    }


    ///////////////////sensor/////////////////////////////

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            val x = -it.values[0]
            //calculate lane
            val maxTilt = 5f // maximum tilt value
            val numLanes = NUM_LANES
            val clampedX = x.coerceIn(-maxTilt, maxTilt)
            val lane = (((clampedX + maxTilt) / (2 * maxTilt)) * (numLanes - 1)).toInt()

            gameManager.setPlayerLane(lane)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //not used
    }

}

