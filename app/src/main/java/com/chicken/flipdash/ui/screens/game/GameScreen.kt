package com.chicken.flipdash.ui.screens.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chicken.flipdash.R
import com.chicken.flipdash.ui.GameAction
import com.chicken.flipdash.ui.GameViewModel
import com.chicken.flipdash.ui.components.GameCanvas
import com.chicken.flipdash.ui.components.GameText

@Composable
fun GameScreen(onNavigateBack: () -> Unit, viewModel: GameViewModel = hiltViewModel()) {
    val state by viewModel.gameState.collectAsState()

    Box(modifier = Modifier.fillMaxSize().clickable { viewModel.onAction(GameAction.Tap) }) {
        // Game Background Image
        Image(
                painter = painterResource(id = R.drawable.bg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
        )

        GameCanvas(state = state)

        // HUD - Score (Top Center)
        Column(
                modifier = Modifier.fillMaxWidth().padding(top = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
        ) { GameText(text = state.score.toString(), fontSize = 56.sp, outlineWidth = 3) }

        // HUD - Pause Button (Top Right)
        Box(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                contentAlignment = Alignment.TopEnd
        ) {
            Box(
                    modifier = Modifier.clickable { viewModel.onAction(GameAction.Pause) },
                    contentAlignment = Alignment.Center
            ) {
                Image(
                        painter = painterResource(id = R.drawable.btn_bg_round_orange),
                        contentDescription = null,
                        modifier = Modifier.size(54.dp)
                )
                Image(
                        painter = painterResource(id = R.drawable.ic_pause),
                        contentDescription = "Pause",
                        modifier = Modifier.size(24.dp)
                )
            }
        }

        // Overlays
        if (state.isGameOver) {
            GameOverOverlay(
                    score = state.score,
                    onRestart = { viewModel.onAction(GameAction.Restart) },
                    onMenu = onNavigateBack
            )
        } else if (state.isPaused) {
            PauseOverlay(
                    onResumed = { viewModel.onAction(GameAction.Resume) },
                    onMenu = onNavigateBack
            )
        }
    }
}

@Composable
fun GameOverOverlay(score: Int, onRestart: () -> Unit, onMenu: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            GameText(text = "GAME OVER", fontSize = 50.sp, color = Color.White)
            Spacer(modifier = Modifier.height(20.dp))
            GameText(text = "SCORE: $score", fontSize = 32.sp)
            Spacer(modifier = Modifier.height(40.dp))

            Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.clickable { onRestart() }
            ) {
                Image(
                        painter = painterResource(R.drawable.btn_bg_primary_orange),
                        contentDescription = null,
                        modifier = Modifier.width(220.dp).height(70.dp)
                )
                GameText(text = "RETRY", fontSize = 28.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Box(contentAlignment = Alignment.Center, modifier = Modifier.clickable { onMenu() }) {
                Image(
                        painter = painterResource(R.drawable.btn_bg_primary_red),
                        contentDescription = null,
                        modifier = Modifier.width(220.dp).height(70.dp)
                )
                GameText(text = "MENU", fontSize = 28.sp)
            }
        }
    }
}

@Composable
fun PauseOverlay(onResumed: () -> Unit, onMenu: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            GameText(text = "PAUSED", fontSize = 50.sp)
            Spacer(modifier = Modifier.height(40.dp))

            Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.clickable { onResumed() }
            ) {
                Image(
                        painter = painterResource(R.drawable.btn_bg_primary_orange),
                        contentDescription = null,
                        modifier = Modifier.width(220.dp).height(70.dp)
                )
                GameText(text = "RESUME", fontSize = 28.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Box(contentAlignment = Alignment.Center, modifier = Modifier.clickable { onMenu() }) {
                Image(
                        painter = painterResource(R.drawable.btn_bg_primary_red),
                        contentDescription = null,
                        modifier = Modifier.width(220.dp).height(70.dp)
                )
                GameText(text = "MENU", fontSize = 28.sp)
            }
        }
    }
}
