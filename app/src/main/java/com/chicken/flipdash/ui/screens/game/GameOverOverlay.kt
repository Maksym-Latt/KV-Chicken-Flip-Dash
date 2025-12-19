package com.chicken.flipdash.ui.screens.game

import android.R.attr.text
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.flipdash.R
import com.chicken.flipdash.ui.components.GameText


@Composable
fun GameOverOverlay(
    score: Int,
    bestScore: Int,
    onRestart: () -> Unit,
    onMenu: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.30f))

            GameText(text = "GRAVITY WINS", fontSize = 40.sp)
            Spacer(modifier = Modifier.height(6.dp))
            GameText(text = "THIS TIME...", fontSize = 40.sp)

            Spacer(modifier = Modifier.height(14.dp))

            GameText(text = "SCORE: $score", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(6.dp))
            GameText(text = "BEST: $bestScore", fontSize = 24.sp)

            Spacer(modifier = Modifier.weight(0.22f))

            Image(
                painter = painterResource(id = R.drawable.lose_chicken),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(1f),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.weight(0.22f))

            val noRipple = remember { MutableInteractionSource() }

            Box(
                modifier = Modifier
                    .clickable(
                        interactionSource = noRipple,
                        indication = null,
                        onClick = onRestart
                    )
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.btn_bg_primary_orange),
                    contentDescription = null,
                    modifier = Modifier
                        .width(260.dp)
                        .height(80.dp)
                )
                GameText(text = "RESTART", fontSize = 30.sp)
            }

            Box(
                modifier = Modifier
                    .clickable(
                        interactionSource = noRipple,
                        indication = null,
                        onClick = onMenu
                    )
                    .padding(bottom = 48.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.btn_bg_primary_red),
                    contentDescription = null,
                    modifier = Modifier
                        .width(260.dp)
                        .height(80.dp)
                )
                GameText(text = "HOME", fontSize = 30.sp)
            }


            Spacer(modifier = Modifier.weight(0.30f))
        }
    }
}