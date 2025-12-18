package com.chicken.flipdash

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioController @Inject constructor() {
    fun onAppForeground() {
        // Stub: Resume music if needed
    }

    fun onAppBackground() {
        // Stub: Pause music if needed
    }

    fun playSfx(type: SfxType) {
        // Stub: Play sound effect
    }

    enum class SfxType {
        TAP,
        SCORE,
        DEATH
    }
}
