package com.gamepackage.androidproject1.logic

data class Obstacle(
    val type: ObstacleType
)

enum class ObstacleType {
    ENEMY, BONUS
}