package com.chicken.flipdash

import android.content.Context
import android.media.MediaPlayer
import android.media.SoundPool
import com.chicken.flipdash.data.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Singleton
class AudioPullController
@Inject
constructor(
        @ApplicationContext private val context: Context,
        private val settingsRepository: SettingsRepository
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private var musicPlayer: MediaPlayer? = null
    private var currentTrack: MusicTrack? = null
    private var musicVolume: Float = 0.8f
    private var sfxVolume: Float = 0.8f

    private val soundPool: SoundPool = SoundPool.Builder().setMaxStreams(4).build()
    private val sfxIds: Map<SfxType, Int> =
            mapOf(
                    SfxType.JUMP to soundPool.load(context, R.raw.sfx_jump, 1),
                    SfxType.LOSE to soundPool.load(context, R.raw.sfx_lose, 1),
                    SfxType.PICK_EGG to soundPool.load(context, R.raw.sfx_pick_egg, 1)
            )

    init {
        scope.launch {
            settingsRepository.soundSettingsFlow.collectLatest { settings ->
                setMusicVolume(settings.musicVolume, shouldPersist = false)
                setSfxVolume(settings.sfxVolume, shouldPersist = false)
            }
        }
    }

    fun onAppForeground() {
        if (musicPlayer != null && currentTrack != null) {
            resumeMusic()
        }
    }

    fun onAppBackground() {
        pauseMusic()
    }

    fun playMenuMusic() {
        playMusic(MusicTrack.MENU)
    }

    fun playGameMusic() {
        playMusic(MusicTrack.GAME)
    }

    private fun playMusic(track: MusicTrack) {
        if (currentTrack == track && musicPlayer?.isPlaying == true) return

        musicPlayer?.stop()
        musicPlayer?.release()

        val resId =
                when (track) {
                    MusicTrack.MENU -> R.raw.menu_music
                    MusicTrack.GAME -> R.raw.game_loop
                }

        musicPlayer =
                MediaPlayer.create(context, resId)?.apply {
                    isLooping = true
                    setVolume(musicVolume, musicVolume)
                    start()
                }
        currentTrack = track
    }

    fun pauseMusic() {
        musicPlayer?.pause()
    }

    fun resumeMusic() {
        if (musicPlayer?.isPlaying == false) {
            musicPlayer?.start()
        }
    }

    fun stopMusic() {
        musicPlayer?.stop()
        musicPlayer?.release()
        musicPlayer = null
        currentTrack = null
    }

    fun setMusicVolume(volume: Float, shouldPersist: Boolean = true) {
        val clamped = volume.coerceIn(0f, 1f)
        musicVolume = clamped
        musicPlayer?.setVolume(clamped, clamped)
        if (shouldPersist) {
            scope.launch { settingsRepository.setMusicVolume(clamped) }
        }
    }

    fun setSfxVolume(volume: Float, shouldPersist: Boolean = true) {
        val clamped = volume.coerceIn(0f, 1f)
        sfxVolume = clamped
        if (shouldPersist) {
            scope.launch { settingsRepository.setSfxVolume(clamped) }
        }
    }

    fun playSfx(type: SfxType) {
        val soundId = sfxIds[type] ?: return
        // Reuse the existing scope instead of creating a new one every time
        scope.launch(Dispatchers.Default) {
            soundPool.play(soundId, sfxVolume, sfxVolume, 1, 0, 1f)
        }
    }

    enum class SfxType {
        JUMP,
        PICK_EGG,
        LOSE
    }

    enum class MusicTrack {
        MENU,
        GAME
    }
}
