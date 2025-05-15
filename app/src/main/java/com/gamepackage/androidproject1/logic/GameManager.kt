package com.gamepackage.androidproject1.logic

import com.gamepackage.androidproject1.R
import com.gamepackage.androidproject1.utils.MySignal

class GameManager(
    private val numLane: Int = 3,
    private val numRow: Int = 7,
    private val difficulty: Boolean = false
) {
    private var obstacleField: Array<Array<Obstacle?>> = Array(numRow) { Array(numLane) { null } }
    private var playerField: Array<Boolean> = Array(numLane) { false }
    private var totalScore = 0
    private var healthPoints = 3
    private var gameOver = false
    private var currPlayerPosition : Int = numLane/2


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
        checkCollision()
        for (row in numRow - 1 downTo 1) {
            for (lane in 0 until numLane) {
                obstacleField[row][lane] = obstacleField[row - 1][lane]
            }
        }
        spawnTopRow()
    }

    private fun checkCollision() {
        for (lane in 0 until numLane) {
            val obstacle = obstacleField[numRow-1][lane]
            if (obstacle != null && playerField[lane]) {
                collisionType(obstacle)
            }
        }
        setTotalScore(totalScore+10)
    }

    private fun collisionType(obstacle: Obstacle) {
        if (obstacle.type == ObstacleType.ENEMY) {
            crashCollision()
        }else if (obstacle.type == ObstacleType.BONUS) {
            bonusCollision()
        }
    }

    private fun bonusCollision() {
        MySignal.getInstance().playSoundEffect(R.raw.snd_coin)
        MySignal.getInstance().vibrate(200)
        setTotalScore(totalScore+30)
    }

    private fun crashCollision() {
        MySignal.getInstance().playSoundEffect(R.raw.snd_explosion_cut)
        MySignal.getInstance().vibrate(200)
        MySignal.getInstance().showToast("WE ARE HIT!")
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
        val newPosition = (currPlayerPosition+i).coerceIn(0, numLane - 1)
        setPlayerLane(newPosition)
    }

    fun setPlayerLane(i :Int){
        val newPosition = i.coerceIn(0, numLane - 1)
        playerField[currPlayerPosition] = false
        playerField[newPosition] = true
        currPlayerPosition = newPosition
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

    fun gameOver(): Boolean{
        if(healthPoints <= 0 && !gameOver){
            gameOver = true
            MySignal.getInstance().vibrate(500)
            MySignal.getInstance().playSoundEffect(R.raw.snd_mayday_cut)
            return true
        }
        return false
    }
}