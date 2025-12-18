package com.chicken.flipdash.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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

@Composable
fun IntroOverlay(onStart: () -> Unit) {
    Box(
            modifier = Modifier.fillMaxSize().background(Color(0xAA000000)),
            contentAlignment = Alignment.Center
    ) {
        Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFFDF4E3))) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                GameText(text = "HOW TO PLAY", fontSize = 28.sp, color = Color(0xFF5E3B00))
                Spacer(modifier = Modifier.height(12.dp))
                GameText(
                        text = "Tap the screen to flip gravity.\nCollect eggs and avoid platforms.",
                        fontSize = 18.sp,
                        color = Color(0xFF5E3B00)
                )
                Spacer(modifier = Modifier.height(24.dp))

                Box(contentAlignment = Alignment.Center, modifier = Modifier.clickable { onStart() }) {
                    Image(
                            painter = painterResource(id = R.drawable.btn_bg_primary_orange),
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth(0.8f).height(70.dp),
                            contentScale = ContentScale.FillBounds
                    )
                    GameText(text = "START RUN", fontSize = 24.sp)
                }
            }
        }
    }
}

@Composable
fun GameOverOverlay(score: Int, bestScore: Int, onRestart: () -> Unit, onMenu: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(Color(0xAA000000)), contentAlignment = Alignment.Center) {
        Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFFDF4E3))) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                GameText(text = "GAME OVER", fontSize = 50.sp, color = Color(0xFF5E3B00))
                Spacer(modifier = Modifier.height(12.dp))
                GameText(text = "SCORE: $score", fontSize = 28.sp, color = Color(0xFF5E3B00))
                Spacer(modifier = Modifier.height(6.dp))
                GameText(text = "BEST: $bestScore", fontSize = 22.sp, color = Color(0xFF5E3B00))
                Spacer(modifier = Modifier.height(28.dp))

                Box(contentAlignment = Alignment.Center, modifier = Modifier.clickable { onRestart() }) {
                    Image(
                            painter = painterResource(R.drawable.btn_bg_primary_orange),
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth().height(70.dp)
                    )
                    GameText(text = "RETRY", fontSize = 28.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(contentAlignment = Alignment.Center, modifier = Modifier.clickable { onMenu() }) {
                    Image(
                            painter = painterResource(R.drawable.btn_bg_primary_red),
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth().height(70.dp)
                    )
                    GameText(text = "MENU", fontSize = 26.sp)
                }
            }
        }
    }
}

@Composable
fun PauseOverlay(
        onResumed: () -> Unit,
        onMenu: () -> Unit,
        settingsContent: @Composable () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().background(Color(0xAA000000)), contentAlignment = Alignment.Center) {
        Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFFDF4E3))) {
            Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                GameText(text = "PAUSED", fontSize = 44.sp, color = Color(0xFF5E3B00))
                settingsContent()

                Box(contentAlignment = Alignment.Center, modifier = Modifier.clickable { onResumed() }) {
                    Image(
                            painter = painterResource(R.drawable.btn_bg_primary_orange),
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth().height(64.dp)
                    )
                    GameText(text = "RESUME", fontSize = 26.sp)
                }

                Box(contentAlignment = Alignment.Center, modifier = Modifier.clickable { onMenu() }) {
                    Image(
                            painter = painterResource(R.drawable.btn_bg_primary_red),
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth().height(64.dp)
                    )
                    GameText(text = "MENU", fontSize = 24.sp)
                }
            }
        }
    }
}

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
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                GameText(text = "SETTINGS", fontSize = 22.sp, color = Color(0xFF5E3B00))
                Spacer(modifier = Modifier.weight(1f))
                Box(modifier = Modifier.size(40.dp).clickable { onHome() }, contentAlignment = Alignment.Center) {
                    Image(
                            painter = painterResource(id = R.drawable.ic_home),
                            contentDescription = "Close",
                            modifier = Modifier.size(28.dp)
                    )
                }
            }

            Column {
                GameText(text = "Music", fontSize = 18.sp, color = Color(0xFF5E3B00))
                Slider(value = soundSettings.musicVolume, onValueChange = onMusicVolumeChanged, valueRange = 0f..1f)
            }

            Divider(color = Color(0xFFC4A484))

            Column {
                GameText(text = "SFX", fontSize = 18.sp, color = Color(0xFF5E3B00))
                Slider(value = soundSettings.sfxVolume, onValueChange = onSfxVolumeChanged, valueRange = 0f..1f)
            }
        }
    }

    if (isCard) {
        Card(
                modifier = modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF))
        ) {
            content()
        }
    } else {
        Box(modifier = Modifier.fillMaxSize().background(Color(0xAA000000)), contentAlignment = Alignment.Center) {
            Card(
                    modifier = modifier.fillMaxWidth(0.9f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFDF4E3))
            ) {
                content()
            }
        }
    }
}
