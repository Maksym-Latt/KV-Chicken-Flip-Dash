package com.chicken.flipdash.ui.screens.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chicken.flipdash.R
import com.chicken.flipdash.ui.components.GameText

@Composable
fun SplashScreen(onContinue: () -> Unit, viewModel: MenuViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) { viewModel.restartMenuMusic() }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
                painter = painterResource(id = R.drawable.bg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(40.dp))
            Image(
                    painter = painterResource(id = R.drawable.title),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(0.85f),
                    contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.weight(1f))

            Image(
                    painter = painterResource(id = R.drawable.title_chicken),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(0.6f),
                    contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.weight(1f))

            Box(
                    modifier = Modifier.clickable(onClick = onContinue).padding(bottom = 48.dp),
                    contentAlignment = Alignment.Center
            ) {
                Image(
                        painter = painterResource(id = R.drawable.btn_bg_primary_orange),
                        contentDescription = null,
                        modifier = Modifier.size(width = 260.dp, height = 80.dp)
                )
                GameText(text = "ENTER", fontSize = 30.sp)
            }
        }
    }
}
