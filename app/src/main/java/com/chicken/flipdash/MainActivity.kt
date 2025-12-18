package com.chicken.flipdash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.chicken.flipdash.ui.GameAction
import com.chicken.flipdash.ui.GameViewModel
import com.chicken.flipdash.ui.theme.ChickenFlipDashTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var audioController: AudioController

    private val gameViewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent { ChickenFlipDashTheme { AppNavHost() } }

        hideSystemUI()
    }

    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(
                WindowInsetsCompat.Type.navigationBars() or WindowInsetsCompat.Type.statusBars()
        )
        controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()
        audioController.onAppForeground()
        gameViewModel.onAction(GameAction.Resume)
    }

    override fun onPause() {
        super.onPause()
        audioController.onAppBackground()
        gameViewModel.onAction(GameAction.Pause)
    }

    override fun onDestroy() {
        super.onDestroy()
        audioController.onAppBackground()
    }
}
