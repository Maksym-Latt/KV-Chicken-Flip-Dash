package com.chicken.flipdash.domain

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import kotlin.random.Random

class GameEngine {
        private val chickenWidth = 50f
        private val chickenHeight = 50f
        private val worldWidth = 1000f
        private val worldHeight = 600f
        private val gravityStrength = 0.8f
        private val maxVelocityY = 15f

        fun createInitialState(): GameState {
                val initialPlatforms = mutableListOf<Platform>()
                // Start with a solid long platform
                initialPlatforms.add(Platform(Offset(0f, 450f), Size(1500f, 400f)))

                return GameState(
                        chicken =
                                Chicken(
                                        position = Offset(150f, 400f),
                                        size = Size(chickenWidth, chickenHeight),
                                        isGrounded = true,
                                        gravityDirection = 1f
                                ),
                        platforms = initialPlatforms,
                        gameSpeed = 6f,
                        worldWidth = worldWidth,
                        worldHeight = worldHeight
                )
        }

        fun update(state: GameState, deltaTime: Long): GameState {
                if (state.isGameOver || state.isPaused) return state

                val dt = (deltaTime / 16.6f).coerceIn(0.1f, 2.0f)
                val moveDistance = state.gameSpeed * dt

                // 1. Physics Update
                var velocityY =
                        state.chicken.velocityY +
                                (gravityStrength * state.chicken.gravityDirection * dt)
                velocityY = velocityY.coerceIn(-maxVelocityY, maxVelocityY)

                var newY = state.chicken.position.y + velocityY * dt
                var isGrounded = false
                var finalVelocityY = velocityY

                // 2. Entity Movement
                val nextPlatforms =
                        state.platforms
                                .map {
                                        it.copy(
                                                position =
                                                        it.position.copy(
                                                                x = it.position.x - moveDistance
                                                        )
                                        )
                                }
                                .filter { it.position.x + it.size.width > -1200f }

                val nextEggs =
                        state.eggs
                                .map {
                                        it.copy(
                                                position =
                                                        it.position.copy(
                                                                x = it.position.x - moveDistance
                                                        )
                                        )
                                }
                                .filter { it.position.x + it.size.width > -50f }

                // 3. Vertical Collision & Landing
                val chickenBounds =
                        Rect(
                                state.chicken.position.x + 10f,
                                newY,
                                state.chicken.position.x + chickenWidth - 10f,
                                newY + chickenHeight
                        )

                for (plat in nextPlatforms) {
                        val platRect = Rect(plat.position, plat.size)
                        if (chickenBounds.overlaps(platRect)) {
                                if (state.chicken.gravityDirection > 0) { // Falling Down
                                        if (state.chicken.position.y + chickenHeight <=
                                                        plat.position.y + 35f
                                        ) {
                                                newY = plat.position.y - chickenHeight
                                                finalVelocityY = 0f
                                                isGrounded = true
                                        }
                                } else { // Falling Up
                                        if (state.chicken.position.y >=
                                                        plat.position.y + plat.size.height - 35f
                                        ) {
                                                newY = plat.position.y + plat.size.height
                                                finalVelocityY = 0f
                                                isGrounded = true
                                        }
                                }
                        }
                }

                // 4. Rotation animation (smooth)
                val targetRotation = if (state.chicken.gravityDirection > 0) 0f else 180f
                val currentRotation =
                        state.chicken.rotation + (targetRotation - state.chicken.rotation) * 0.15f

                val newChicken =
                        state.chicken.copy(
                                position = state.chicken.position.copy(y = newY),
                                velocityY = finalVelocityY,
                                rotation = currentRotation,
                                isGrounded = isGrounded
                        )

                // 5. Game Over Logic
                var isGameOver = newY < -500f || newY > worldHeight + 300f

                if (!isGameOver) {
                        val frontRect =
                                Rect(
                                        newChicken.position.x + chickenWidth - 5f,
                                        newChicken.position.y + 15f,
                                        newChicken.position.x + chickenWidth,
                                        newChicken.position.y + chickenHeight - 15f
                                )
                        isGameOver =
                                nextPlatforms.any { plat ->
                                        val platRect = Rect(plat.position, plat.size)
                                        frontRect.overlaps(platRect) && !isGrounded
                                }
                }

                // 6. Egg Collection
                val finalChickenBounds = Rect(newChicken.position, newChicken.size)
                val collectedEggs =
                        nextEggs.filter { egg ->
                                Rect(egg.position, egg.size).overlaps(finalChickenBounds)
                        }
                val remainingEggs = nextEggs.filter { !collectedEggs.contains(it) }
                val newScore = state.score + collectedEggs.size

                // 7. Spawning
                val (finalPlatforms, finalEggs) =
                        spawnNewEntities(
                                nextPlatforms,
                                remainingEggs,
                                state.distanceTraveled + moveDistance,
                                state.gameSpeed
                        )

                return state.copy(
                        chicken = newChicken,
                        platforms = finalPlatforms,
                        eggs = finalEggs,
                        score = newScore,
                        isGameOver = isGameOver,
                        distanceTraveled = state.distanceTraveled + moveDistance,
                        gameSpeed = state.gameSpeed + 0.001f
                )
        }

        fun flipGravity(state: GameState): GameState {
                if (state.isGameOver || state.isPaused) return state
                return state.copy(
                        chicken =
                                state.chicken.copy(
                                        gravityDirection = -state.chicken.gravityDirection,
                                        isGrounded = false
                                )
                )
        }

        private fun spawnNewEntities(
                platforms: List<Platform>,
                eggs: List<Egg>,
                distance: Float,
                speed: Float
        ): Pair<List<Platform>, List<Egg>> {
                val mutPlat = platforms.toMutableList()
                val mutEggs = eggs.toMutableList()

                val lastX = mutPlat.maxOfOrNull { it.position.x + it.size.width } ?: 0f

                if (lastX < 3000f) {
                        val isPit = Random.nextFloat() < 0.3f

                        if (isPit) {
                                // Large pit (significant but clean)
                                val pitSize = Random.nextFloat() * 200f + 400f
                                val nextFloorX = lastX + pitSize
                                val floorWidth = 800f
                                val floorY = Random.nextFloat() * 100f + 400f
                                spawnReliefCluster(mutPlat, nextFloorX, floorY, floorWidth, false)

                                // LARGE SIMPLE ISLAND ABOVE PIT (Even higher)
                                val islandY = Random.nextFloat() * 50f + 60f
                                spawnReliefCluster(
                                        mutPlat,
                                        lastX + (pitSize / 2f),
                                        islandY,
                                        600f,
                                        true
                                )
                        } else {
                                // Large Stable Floors
                                val prevFloor = mutPlat.lastOrNull { !it.isCeiling }
                                val prevFloorY = prevFloor?.position?.y ?: 450f
                                val nextFloorY =
                                        (prevFloorY + if (Random.nextBoolean()) 120f else -120f)
                                                .coerceIn(350f, 530f)
                                val width = 800f

                                spawnReliefCluster(mutPlat, lastX - 5f, nextFloorY, width, false)

                                // High Frequency Stable Islands (Higher clearance)
                                if (Random.nextInt(4) < 3) {
                                        val islandY = Random.nextFloat() * 120f + 50f
                                        spawnReliefCluster(
                                                mutPlat,
                                                lastX + 100f,
                                                islandY.coerceIn(50f, 200f),
                                                500f,
                                                true
                                        )
                                }
                        }
                }

                return mutPlat to mutEggs
        }

        private fun spawnReliefCluster(
                list: MutableList<Platform>,
                startX: Float,
                baseY: Float,
                totalWidth: Float,
                isIsland: Boolean
        ) {
                var currentX = startX
                val segmentCount = Random.nextInt(1, 3)
                val segmentWidth = totalWidth / segmentCount

                // Reduced thickness: 75f (half of previous 150f)
                val islandBottomY = baseY + 75f

                var currentY = baseY
                for (i in 0 until segmentCount) {
                        val h = if (isIsland) (islandBottomY - currentY) else 800f
                        list.add(
                                Platform(
                                        Offset(currentX, currentY),
                                        Size(segmentWidth + 10f, h),
                                        isIsland
                                )
                        )

                        currentX += segmentWidth
                        if (segmentCount > 1 && i == 0) {
                                currentY += if (Random.nextBoolean()) 100f else -100f
                                currentY =
                                        currentY.coerceIn(
                                                if (isIsland) 50f else 300f,
                                                if (isIsland) 300f else 550f
                                        )
                        }
                }
        }
}
