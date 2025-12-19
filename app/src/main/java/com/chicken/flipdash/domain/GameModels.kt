package com.chicken.flipdash.domain

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size

data class GameState(
        val chicken: Chicken = Chicken(),
        val platforms: List<Platform> = emptyList(),
        val eggs: List<Egg> = emptyList(),
        val score: Int = 0,
        val isGameOver: Boolean = false,
        val isPaused: Boolean = false,
        val gameSpeed: Float = 5f,
        val distanceTraveled: Float = 0f,
        val worldWidth: Float = 1000f,
        val worldHeight: Float = 600f,
        val jumpCooldownMs: Float = 0f,
        val maxJumpCooldownMs: Float = 200f
)

data class Chicken(
        val position: Offset = Offset(150f, 300f),
        val size: Size = Size(80f, 116f),
        val velocityY: Float = 0f,
        val rotation: Float = 0f,
        val isGrounded: Boolean = false,
        val gravityDirection: Float = 1f // 1 for down, -1 for up
)

sealed class GameObject {
        abstract val position: Offset
        abstract val size: Size
}

data class Platform(
        override val position: Offset,
        override val size: Size,
        val isCeiling: Boolean = false
) : GameObject()

data class Egg(override val position: Offset, override val size: Size = Size(16f, 21f)) :
        GameObject()
