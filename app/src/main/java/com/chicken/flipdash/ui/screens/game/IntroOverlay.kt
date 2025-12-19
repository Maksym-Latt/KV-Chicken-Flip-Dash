package com.chicken.flipdash.ui.screens.game

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
fun IntroOverlay(onStart: () -> Unit) {
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
                .statusBarsPadding()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.45f))

            GameText(text = "HOW TO PLAY", fontSize = 40.sp)

            Spacer(modifier = Modifier.weight(0.15f))

            GameText(
                text = "Tap the screen to flip gravity",
                fontSize = 22.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            GameText(
                text = "Collect eggs and avoid platforms",
                fontSize = 22.sp
            )

            Spacer(modifier = Modifier.weight(0.35f))

            Box(
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onStart
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
                GameText(text = "START RUN", fontSize = 30.sp)
            }

            Spacer(modifier = Modifier.weight(0.45f))
        }
    }
}
