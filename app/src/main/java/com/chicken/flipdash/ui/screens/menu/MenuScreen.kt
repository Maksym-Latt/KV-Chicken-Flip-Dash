package com.chicken.flipdash.ui.screens.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.flipdash.R
import com.chicken.flipdash.ui.components.GameText

@Composable
fun MenuScreen(
    onStartGame: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .windowInsetsPadding(WindowInsets.safeDrawing),
                contentAlignment = Alignment.TopEnd
            ) {
                Box(contentAlignment = Alignment.Center) {
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
                modifier = Modifier
                    .clickable(onClick = onStartGame)
                    .padding(bottom = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.btn_bg_primary_orange),
                    contentDescription = "Start",
                    modifier = Modifier
                        .width(260.dp)
                        .height(80.dp)
                )
                GameText(text = "START", fontSize = 32.sp)
            }
            Spacer(modifier = Modifier.weight(0.5f))
        }
    }
}