package com.chicken.flipdash.ui.screens.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chicken.flipdash.AudioController
import com.chicken.flipdash.data.SettingsRepository
import com.chicken.flipdash.data.SoundSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MenuViewModel
@Inject
constructor(
        private val audioController: AudioController,
        private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _soundSettings = MutableStateFlow(SoundSettings())
    val soundSettings: StateFlow<SoundSettings> = _soundSettings.asStateFlow()

    init {
        viewModelScope.launch { settingsRepository.soundSettingsFlow.collect { _soundSettings.value = it } }
        audioController.playMenuMusic()
    }

    fun updateMusic(volume: Float) {
        viewModelScope.launch { settingsRepository.setMusicVolume(volume) }
    }

    fun updateSfx(volume: Float) {
        viewModelScope.launch { settingsRepository.setSfxVolume(volume) }
    }

    fun restartMenuMusic() {
        audioController.playMenuMusic()
    }
}
