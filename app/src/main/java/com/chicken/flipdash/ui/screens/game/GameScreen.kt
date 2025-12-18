package com.chicken.flipdash.ui.screens.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.chicken.flipdash.ui.components.GameOverOverlay
import com.chicken.flipdash.ui.components.IntroOverlay
import com.chicken.flipdash.ui.components.PauseOverlay
import com.chicken.flipdash.ui.components.SettingsOverlay

@Composable
fun GameScreen(onNavigateBack: () -> Unit, viewModel: GameViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) { viewModel.onScreenVisible() }

    val goToMenu = {
        viewModel.returnToMenu()
        onNavigateBack()
    }

    val isTapEnabled =
            !uiState.gameState.isPaused && !uiState.gameState.isGameOver && !uiState.showIntro

    Box(modifier = Modifier.fillMaxSize().let { if (isTapEnabled) it.clickable { viewModel.onAction(GameAction.Tap) } else it }) {
        Image(
                painter = painterResource(id = R.drawable.bg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
        )

        GameCanvas(state = uiState.gameState)

        Column(
                modifier = Modifier.fillMaxWidth().padding(top = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
        ) { GameText(text = uiState.gameState.score.toString(), fontSize = 56.sp, outlineWidth = 3) }

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

        when {
            uiState.showIntro -> {
                IntroOverlay(onStart = { viewModel.onAction(GameAction.StartIntro) })
            }
            uiState.gameState.isGameOver -> {
                GameOverOverlay(
                        score = uiState.gameState.score,
                        bestScore = uiState.bestScore,
                        onRestart = { viewModel.onAction(GameAction.Restart) },
                        onMenu = goToMenu
                )
            }
            uiState.gameState.isPaused -> {
                PauseOverlay(
                        onResumed = { viewModel.onAction(GameAction.Resume) },
                        onMenu = goToMenu,
                        settingsContent = {
                            SettingsOverlay(
                                    soundSettings = uiState.soundSettings,
                                    onMusicVolumeChanged = viewModel::updateMusicVolume,
                                    onSfxVolumeChanged = viewModel::updateSfxVolume,
                                    onHome = goToMenu,
                                    isCard = true
                            )
                        }
                )
            }
        }
    }
}
