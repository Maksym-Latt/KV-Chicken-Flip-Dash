package com.chicken.flipdash.ui.screens.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chicken.flipdash.R
import com.chicken.flipdash.ui.components.GameText
import com.chicken.flipdash.ui.components.SettingsOverlay

@Composable
fun MenuScreen(onStartGame: () -> Unit, viewModel: MenuViewModel = hiltViewModel()) {
    val soundSettings by viewModel.soundSettings.collectAsState()
    var showSettings by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { viewModel.restartMenuMusic() }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
                painter = painterResource(id = R.drawable.bg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
        )

        Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
                            .windowInsetsPadding(WindowInsets.safeDrawing),
                    contentAlignment = Alignment.TopEnd
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.clickable { showSettings = true }) {
                    Image(
                            painter = painterResource(id = R.drawable.btn_bg_round_orange),
                            contentDescription = null,
                            modifier = Modifier.size(60.dp)
                    )
                    Image(
                            painter = painterResource(id = R.drawable.ic_settings),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(0.2f))

            Image(
                    painter = painterResource(id = R.drawable.title),
                    contentDescription = "Chicken Flip Dash",
                    modifier = Modifier.fillMaxWidth(0.8f),
                    contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.weight(0.7f))

            Image(
                    painter = painterResource(id = R.drawable.title_chicken),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(0.65f),
                    contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.weight(0.5f))

            Box(
                    modifier = Modifier.clickable(onClick = onStartGame).padding(bottom = 40.dp),
                    contentAlignment = Alignment.Center
            ) {
                Image(
                        painter = painterResource(id = R.drawable.btn_bg_primary_orange),
                        contentDescription = "Start",
                        modifier = Modifier.width(260.dp).height(80.dp)
                )
                GameText(text = "START", fontSize = 32.sp)
            }
            Spacer(modifier = Modifier.weight(0.5f))
        }

        if (showSettings) {
            SettingsOverlay(
                    soundSettings = soundSettings,
                    onMusicVolumeChanged = viewModel::updateMusic,
                    onSfxVolumeChanged = viewModel::updateSfx,
                    onHome = { showSettings = false }
            )
        }
    }
}
