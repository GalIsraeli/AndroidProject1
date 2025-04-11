package com.gamepackage.androidproject1.logic

import android.content.Context
import com.gamepackage.androidproject1.R
import com.gamepackage.androidproject1.utils.playSoundEffect

class GameManager(
    private val context: Context,
    private val numLane: Int = 3,
    private val numRow: Int = 7,
    private val speed: Boolean = false,
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

    }

    private fun checkColision() {
        for (lane in 0 until numLane) {
            val obstacle = obstacleField[numRow-1][lane]
            if (obstacle != null && playerField[lane]) {
                colisionType(obstacle)
            }
        }
    }

    private fun colisionType(obstacle: Obstacle) {
        if (obstacle.type == ObstacleType.ENEMY) {
            enemyColision()
        }else if (obstacle.type == ObstacleType.BONUS) {
            bonusColision()
        }
    }

    private fun bonusColision() {
        TODO("Not yet implemented")
    }

    private fun enemyColision() {
        context.playSoundEffect(R.raw.snd_explosion_cut)
    }

    private fun spawnTopRow() {
        val topRow = 0
        for (lane in 0 until numLane) {
            obstacleField[topRow][lane] = generateObstacleOrNull()
        }
    }

    private fun generateObstacleOrNull(): Obstacle? {
        val chance = if (difficulty) 40 else 20
        return if ((0..99).random() < chance) {
            Obstacle(ObstacleType.ENEMY)
        } else {
            null
        }
    }

    fun getObstacleField(): Array<Array<Obstacle?>> {
        return obstacleField
    }
    
    fun getPlayerField():Array<Boolean>{
        return playerField
    }
}