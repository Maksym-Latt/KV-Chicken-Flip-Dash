package com.chicken.flipdash.ui.screens.game

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.chicken.flipdash.R
import com.chicken.flipdash.ui.components.GameText

@Composable
fun GameScreen(onNavigateBack: () -> Unit, viewModel: GameViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) { viewModel.onScreenVisible() }

    LifecycleEventEffect(Lifecycle.Event.ON_PAUSE) { viewModel.onAction(GameAction.Pause) }

    val goToMenu = {
        viewModel.returnToMenu()
        onNavigateBack()
    }

    val isTapEnabled =
        !uiState.gameState.isPaused && !uiState.gameState.isGameOver && !uiState.showIntro

    BackHandler {
        if (uiState.gameState.isGameOver) {
            goToMenu()
        } else if (!uiState.showIntro) {
            viewModel.onAction(GameAction.Pause)
        } else {
            onNavigateBack()
        }
    }

    Box(
        modifier =
            Modifier.fillMaxSize().let {
                if (isTapEnabled) {
                    it.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { viewModel.onAction(GameAction.Tap) }
                } else it
            }
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        GameCanvas(state = uiState.gameState)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.safeDrawing),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            JumpIndicator(state = uiState.gameState, modifier = Modifier.padding(bottom = 8.dp))
            GameText(text = uiState.gameState.score.toString(), fontSize = 56.sp, outlineWidth = 3)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth().windowInsetsPadding(WindowInsets.safeDrawing).padding(horizontal = 8.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            Box(
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    viewModel.onAction(GameAction.Pause)
                },
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
                    soundSettings = uiState.soundSettings,
                    onMusicVolumeChanged = viewModel::updateMusicVolume,
                    onSfxVolumeChanged = viewModel::updateSfxVolume,
                )
            }
        }
    }
}
