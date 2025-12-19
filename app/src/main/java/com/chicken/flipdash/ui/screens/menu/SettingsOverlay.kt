package com.chicken.flipdash.ui.screens.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.flipdash.R
import com.chicken.flipdash.data.SoundSettings
import com.chicken.flipdash.ui.components.GameText
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.chicken.flipdash.ui.components.GamePillSlider

@Composable
fun SettingsOverlay(
    soundSettings: SoundSettings,
    onMusicVolumeChanged: (Float) -> Unit,
    onSfxVolumeChanged: (Float) -> Unit,
    onHome: () -> Unit,
    modifier: Modifier = Modifier,
    isCard: Boolean = false
) {
    val content: @Composable () -> Unit = {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.55f))

            GameText(text = "SETTINGS", fontSize = 44.sp)

            Spacer(modifier = Modifier.weight(0.15f))

            GameText(text = "SOUND", fontSize = 26.sp)
            Spacer(modifier = Modifier.height(10.dp))
            GamePillSlider(
                value = soundSettings.sfxVolume,
                onValueChange = { onSfxVolumeChanged(it.coerceIn(0f, 1f)) },
                modifier = Modifier.fillMaxWidth(0.78f)
            )

            Spacer(modifier = Modifier.height(22.dp))

            GameText(text = "MUSIC", fontSize = 26.sp)
            Spacer(modifier = Modifier.height(10.dp))
            GamePillSlider(
                value = soundSettings.musicVolume,
                onValueChange = { onMusicVolumeChanged(it.coerceIn(0f, 1f)) },
                modifier = Modifier.fillMaxWidth(0.78f)
            )

            Spacer(modifier = Modifier.weight(0.15f))

            Box(
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onHome
                    )
                    .padding(bottom = 48.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.btn_bg_primary_orange),
                    contentDescription = null,
                    modifier = Modifier
                        .width(260.dp)
                        .height(80.dp)
                )
                GameText(text = "HOME", fontSize = 30.sp)
            }


            Spacer(modifier = Modifier.weight(0.55f))
        }
    }

    if (isCard) {
        Box(modifier = modifier) { content() }
    } else {
        Box(modifier = modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.bg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            content()
        }
    }
}
