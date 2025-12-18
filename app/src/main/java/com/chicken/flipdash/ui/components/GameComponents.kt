package com.chicken.flipdash.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.chicken.flipdash.R
import com.chicken.flipdash.domain.GameState

@Composable
fun GameCanvas(state: GameState, modifier: Modifier = Modifier) {
        val playerPainter = painterResource(id = R.drawable.player)
        val bushPainter = painterResource(id = R.drawable.bush)

        Canvas(modifier = modifier.fillMaxSize()) {
                val scaleX = size.width / state.worldWidth
                val scaleY = size.height / state.worldHeight

                // Render Platforms
                state.platforms.forEach { plat ->
                        drawLargePlatformWithBushes(
                                plat.position,
                                plat.size,
                                plat.isCeiling,
                                scaleX,
                                scaleY,
                                bushPainter
                        )
                }

                // Eggs
                state.eggs.forEach { egg ->
                        drawStylizedEgg(egg.position, egg.size, scaleX, scaleY)
                }

                // Chicken
                drawPlayer(
                        state.chicken.position,
                        state.chicken.size,
                        state.chicken.rotation,
                        scaleX,
                        scaleY,
                        playerPainter
                )
        }
}

private fun DrawScope.drawLargePlatformWithBushes(
        pos: Offset,
        s: Size,
        isIsland: Boolean,
        sx: Float,
        sy: Float,
        bushPainter: Painter
) {
        val x = pos.x * sx
        val y = pos.y * sy
        val w = s.width * sx
        val h = s.height * sy

        // Simple Block Body (Polygonal hint)
        drawRoundRect(
                color = Color(0xFFEEEEEE),
                topLeft = Offset(x - 2f, y),
                size = Size(w + 4f, h),
                cornerRadius = CornerRadius(12f, 12f)
        )

        // Top Highlight
        drawLine(
                color = Color(0xFFDDDDDD),
                start = Offset(x, y),
                end = Offset(x + w, y),
                strokeWidth = 6f
        )

        // Bushes (Using provided image)
        // Draw bushes on both ground and islands as requested
        val bushSize = 40f * sx

        // Draw 1-2 bushes per segment
        drawImageAt(
                bushPainter,
                Offset(x + 50f * sx, y - bushSize * 0.8f),
                Size(bushSize, bushSize)
        )
        if (w > 300f * sx) {
                drawImageAt(
                        bushPainter,
                        Offset(x + 200f * sx, y - bushSize * 0.8f),
                        Size(bushSize, bushSize)
                )
        }
}

private fun DrawScope.drawImageAt(painter: Painter, pos: Offset, size: Size) {
        translate(pos.x, pos.y) { with(painter) { draw(size) } }
}

private fun DrawScope.drawStylizedEgg(pos: Offset, s: Size, sx: Float, sy: Float) {
        val x = pos.x * sx
        val y = pos.y * sy
        val w = s.width * sx
        val h = s.height * sy

        drawOval(
                brush = Brush.radialGradient(colors = listOf(Color(0xFFFFEE58), Color(0xFFFDD835))),
                topLeft = Offset(x, y),
                size = Size(w, h)
        )
}

private fun DrawScope.drawPlayer(
        pos: Offset,
        s: Size,
        rotation: Float,
        sx: Float,
        sy: Float,
        painter: Painter
) {
        val x = pos.x * sx
        val y = pos.y * sy
        val w = s.width * sx
        val h = s.height * sy

        rotate(degrees = rotation, pivot = Offset(x + w / 2, y + h / 2)) {
                translate(x, y) { with(painter) { draw(Size(w, h)) } }
        }
}
