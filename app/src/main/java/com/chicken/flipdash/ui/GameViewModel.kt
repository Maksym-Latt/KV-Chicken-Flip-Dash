package com.chicken.flipdash.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chicken.flipdash.AudioController
import com.chicken.flipdash.data.SettingsRepository
import com.chicken.flipdash.data.SoundSettings
import com.chicken.flipdash.domain.GameEngine
import com.chicken.flipdash.domain.GameState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GameUiState(
        val gameState: GameState,
        val soundSettings: SoundSettings = SoundSettings(),
        val bestScore: Int = 0,
        val showIntro: Boolean = true
)

@HiltViewModel
class GameViewModel
@Inject
constructor(
        private val audioController: AudioController,
        private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val engine = GameEngine()

    private val _uiState =
            MutableStateFlow(
                    GameUiState(
                            gameState = engine.createInitialState().copy(isPaused = true),
                            soundSettings = SoundSettings(),
                            bestScore = 0,
                            showIntro = true
                    )
            )
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var gameJob: Job? = null

    init {
        startGameLoop()
        observeSettings()
    }

    private fun observeSettings() {
        viewModelScope.launch {
            settingsRepository.soundSettingsFlow.collect { settings ->
                _uiState.update { it.copy(soundSettings = settings) }
            }
        }

        viewModelScope.launch {
            settingsRepository.bestScoreFlow.collect { best ->
                _uiState.update { it.copy(bestScore = best) }
            }
        }
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

                        val snapshot = _uiState.value
                        val state = snapshot.gameState

                        if (!snapshot.showIntro && !state.isPaused && !state.isGameOver) {
                            val updated = engine.update(state, deltaTime)

                            if (updated.score > state.score) {
                                audioController.playSfx(AudioController.SfxType.PICK_EGG)
                            }

                            if (!state.isGameOver && updated.isGameOver) {
                                audioController.playSfx(AudioController.SfxType.LOSE)
                                saveBestScore(updated.score)
                            }

                            _uiState.update { it.copy(gameState = updated) }
                        }
                        delay(16) // ~60 FPS
                    }
                }
    }

    fun onScreenVisible() {
        audioController.playGameMusic()
    }

    fun onAction(action: GameAction) {
        when (action) {
            is GameAction.Tap -> handleTap()
            is GameAction.Pause -> _uiState.update { it.copy(gameState = it.gameState.copy(isPaused = true)) }
            is GameAction.Resume ->
                    _uiState.update { it.copy(gameState = it.gameState.copy(isPaused = false)) }
            is GameAction.Restart -> restartGame(showIntro = false)
            is GameAction.StartIntro -> beginGameplay()
        }
    }

    private fun handleTap() {
        val snapshot = _uiState.value
        if (snapshot.gameState.isGameOver || snapshot.gameState.isPaused || snapshot.showIntro) return

        _uiState.update { it.copy(gameState = engine.flipGravity(snapshot.gameState)) }
        audioController.playSfx(AudioController.SfxType.JUMP)
    }

    private fun beginGameplay() {
        _uiState.update {
            it.copy(showIntro = false, gameState = it.gameState.copy(isPaused = false, isGameOver = false))
        }
        audioController.playGameMusic()
    }

    private fun restartGame(showIntro: Boolean) {
        _uiState.value =
                _uiState.value.copy(
                        gameState = engine.createInitialState().copy(isPaused = showIntro),
                        showIntro = showIntro
                )
        if (!showIntro) {
            audioController.playGameMusic()
        }
    }

    fun returnToMenu() {
        audioController.playMenuMusic()
    }

    fun updateMusicVolume(volume: Float) {
        viewModelScope.launch { settingsRepository.setMusicVolume(volume) }
    }

    fun updateSfxVolume(volume: Float) {
        viewModelScope.launch { settingsRepository.setSfxVolume(volume) }
    }

    private fun saveBestScore(score: Int) {
        viewModelScope.launch { settingsRepository.updateBestScore(score) }
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
    object StartIntro : GameAction()
}
