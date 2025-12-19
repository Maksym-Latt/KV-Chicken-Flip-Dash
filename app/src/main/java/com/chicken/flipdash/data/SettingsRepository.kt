package com.chicken.flipdash.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

data class SoundSettings(val musicVolume: Float = 0.8f, val sfxVolume: Float = 0.8f)

@Singleton
class SettingsRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private val musicVolumeKey = floatPreferencesKey("music_volume")
    private val sfxVolumeKey = floatPreferencesKey("sfx_volume")
    private val bestScoreKey = intPreferencesKey("best_score")

    val soundSettingsFlow: Flow<SoundSettings> =
            context.dataStore.data.map { prefs ->
                SoundSettings(
                        musicVolume = prefs[musicVolumeKey] ?: 0.8f,
                        sfxVolume = prefs[sfxVolumeKey] ?: 0.8f
                )
            }

    val bestScoreFlow: Flow<Int> = context.dataStore.data.map { prefs -> prefs[bestScoreKey] ?: 0 }

    suspend fun setMusicVolume(volume: Float) {
        context.dataStore.edit { prefs -> prefs[musicVolumeKey] = volume.coerceIn(0f, 1f) }
    }

    suspend fun setSfxVolume(volume: Float) {
        context.dataStore.edit { prefs -> prefs[sfxVolumeKey] = volume.coerceIn(0f, 1f) }
    }

    suspend fun updateBestScore(score: Int) {
        context.dataStore.edit { prefs ->
            val current = prefs[bestScoreKey] ?: 0
            if (score > current) {
                prefs[bestScoreKey] = score
            }
        }
    }
}
