package com.chicken.flipdash.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chicken.flipdash.AudioController
import com.chicken.flipdash.domain.GameEngine
import com.chicken.flipdash.domain.GameState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class GameViewModel @Inject constructor(private val audioController: AudioController) :
        ViewModel() {

    private val engine = GameEngine()

    private val _gameState = MutableStateFlow(engine.createInitialState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private var gameJob: Job? = null

    init {
        startGameLoop()
    }

    private fun startGameLoop() {
        gameJob?.cancel()
        gameJob =
                viewModelScope.launch {
                    var lastTime = System.currentTimeMillis()
                    while (true) {
                        val currentTime = System.currentTimeMillis()
                        val deltaTime = currentTime - lastTime
                        lastTime = currentTime

                        if (!_gameState.value.isPaused && !_gameState.value.isGameOver) {
                            _gameState.value = engine.update(_gameState.value, deltaTime)

                            if (_gameState.value.isGameOver) {
                                audioController.playSfx(AudioController.SfxType.DEATH)
                            }
                        }
                        delay(16) // ~60 FPS
                    }
                }
    }

    fun onAction(action: GameAction) {
        when (action) {
            is GameAction.Tap -> {
                if (!_gameState.value.isGameOver && !_gameState.value.isPaused) {
                    _gameState.value = engine.flipGravity(_gameState.value)
                    audioController.playSfx(AudioController.SfxType.TAP)
                }
            }
            is GameAction.Pause -> {
                _gameState.value = _gameState.value.copy(isPaused = true)
            }
            is GameAction.Resume -> {
                _gameState.value = _gameState.value.copy(isPaused = false)
            }
            is GameAction.Restart -> {
                _gameState.value = engine.createInitialState()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        gameJob?.cancel()
    }
}

sealed class GameAction {
    object Tap : GameAction()
    object Pause : GameAction()
    object Resume : GameAction()
    object Restart : GameAction()
}
