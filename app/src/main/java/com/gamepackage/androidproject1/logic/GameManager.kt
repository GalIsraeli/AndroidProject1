package com.gamepackage.androidproject1.logic

import android.content.Context
import android.widget.Toast
import com.gamepackage.androidproject1.R
import com.gamepackage.androidproject1.utils.playSoundEffect
import com.gamepackage.androidproject1.utils.vibrateDevice

class GameManager(
    private val context: Context,
    private val numLane: Int = 3,
    private val numRow: Int = 7,
    //private val speed: Boolean = false,
    private val difficulty: Boolean = false
) {
    private var obstacleField: Array<Array<Obstacle?>> = Array(numRow) { Array(numLane) { null } }
    private var playerField: Array<Boolean> = Array(numLane) { false }
    private var totalScore = 0
    private var healthPoints = 3


    init {
        clearField()
        playerField[numLane / 2] = true
        spawnTopRow()
    }

    private fun clearField() {
        for (row in 0 until numRow) {
            for (lane in 0 until numLane) {
                obstacleField[row][lane] = null
            }
        }
    }

    fun tickObstacles() {
        checkColision()
        for (row in numRow - 1 downTo 1) {
            for (lane in 0 until numLane) {
                obstacleField[row][lane] = obstacleField[row - 1][lane]
            }
        }
        spawnTopRow()
        context.playSoundEffect(R.raw.snd_obstacle_tick_cut)
    }

    private fun checkColision() {
        for (lane in 0 until numLane) {
            val obstacle = obstacleField[numRow-1][lane]
            if (obstacle != null && playerField[lane]) {
                colisionType(obstacle)
            }
        }
        setTotalScore(totalScore+10)
    }

    private fun colisionType(obstacle: Obstacle) {
        if (obstacle.type == ObstacleType.ENEMY) {
            crashColision()
        }else if (obstacle.type == ObstacleType.BONUS) {
            bonusColision()
        }
    }

    private fun bonusColision() {
        context.playSoundEffect(R.raw.snd_coin)
        context.vibrateDevice(200)
        setTotalScore(totalScore+30)
    }

    private fun crashColision() {
        context.playSoundEffect(R.raw.snd_explosion_cut)
        context.vibrateDevice(200)
        Toast.makeText(context, "WE ARE HIT!", Toast.LENGTH_SHORT).show()
        setHealthPoints(healthPoints-1)
        setTotalScore(totalScore-30)
    }

    private fun setHealthPoints(i: Int) {
        healthPoints = i
    }

    private fun setTotalScore(i: Int) {
        totalScore = i
    }

    private fun spawnTopRow() {
        val topRow = 0
        for (lane in 0 until numLane) {
            obstacleField[topRow][lane] = generateObstacleOrNull()
        }
        //make sure there is at least one empty spot
        if (obstacleField[topRow].all { it?.type == ObstacleType.ENEMY }) {
            val colToClear = (0 until numLane).random()
            obstacleField[topRow][colToClear] = null
        }
    }

    private fun generateObstacleOrNull(): Obstacle? {
        val roll = (0..99).random()
        return when {
            roll < if (difficulty) 40 else 20 -> Obstacle(ObstacleType.ENEMY)
            roll < if (difficulty) 50 else 25 -> Obstacle(ObstacleType.BONUS)
            else -> null
        }
    }

    fun movePlayer(i: Int) {
        for(lane in 0 until numLane){
            if(playerField[lane]){
                val newPosition = (lane + i).coerceIn(0, numLane - 1)
                playerField[lane] = false
                playerField[newPosition] = true
                break
            }
        }
    }

    fun getObstacleField(): Array<Array<Obstacle?>> {
        return obstacleField
    }
    
    fun getPlayerField():Array<Boolean>{
        return playerField
    }

    fun getTotalScore():Int{
        return totalScore
    }

    fun getHealthPoints():Int{
        return healthPoints
    }

    fun gameOver(){
        if(healthPoints <= 0){
            healthPoints = 3
            context.vibrateDevice(500)
            context.playSoundEffect(R.raw.snd_mayday_cut)
        }
    }
}