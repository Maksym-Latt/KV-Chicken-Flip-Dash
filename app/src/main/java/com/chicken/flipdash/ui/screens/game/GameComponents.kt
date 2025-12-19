package com.chicken.flipdash.ui.screens.game

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.chicken.flipdash.R
import com.chicken.flipdash.domain.GameState

@Composable
fun GameCanvas(state: GameState, modifier: Modifier = Modifier) {
    val playerPainter = painterResource(id = R.drawable.player)
    val bushPainter = painterResource(id = R.drawable.bush)
    val eggPainter = painterResource(id = R.drawable.egg)

    Canvas(modifier = modifier.fillMaxSize()) {
        val scaleX = size.width / state.worldWidth
        val scaleY = size.height / state.worldHeight

        // Render Platforms
        state.platforms.forEach { plat ->
            drawLargePlatformWithBushes(
                plat.position,
                plat.size,
                scaleX,
                scaleY,
                bushPainter
            )
        }

        // Eggs
        state.eggs.forEach { egg ->
            drawEgg(egg.position, egg.size, scaleX, scaleY, eggPainter)
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
    sx: Float,
    sy: Float,
    bushPainter: Painter
) {
    val x = pos.x * sx
    val y = pos.y * sy
    val w = s.width * sx
    val h = s.height * sy

    val r = 12f
    val stableSeed = (s.width.toInt() * 31 + s.height.toInt() * 17).toLong()
    val random = java.util.Random(stableSeed)

    drawRoundRect(
        brush =
            Brush.verticalGradient(
                colors = listOf(Color(0xFFC2C6CB), Color(0xFFE7EAED)),
                startY = y,
                endY = y + h
            ),
        topLeft = Offset(x - 2f, y),
        size = Size(w + 4f, h),
        cornerRadius = CornerRadius(r, r)
    )

    clipRect(left = x, top = y, right = x + w, bottom = y + h) {
        // Simplified band and oval count for better performance
        val bandCount = (4 + (h / (40f * sy)).toInt()).coerceAtLeast(4)
        val bandHeight = h / bandCount.toFloat()

        for (i in 0 until bandCount) {
            val yy = y + i * bandHeight - bandHeight * 0.35f
            val alpha = 0.18f + (i.toFloat() / bandCount) * 0.12f

            val ovalH = bandHeight * (1.2f + random.nextFloat() * 0.4f)
            val stepX = 130f * sx // Increased step to reduce draw calls

            var xx = x - stepX * 0.5f
            while (xx < x + w + stepX) {
                val ovalW = stepX * (1.3f + random.nextFloat() * 0.7f)
                val ox = (random.nextFloat() - 0.5f) * stepX * 0.3f
                val oy = (random.nextFloat() - 0.5f) * bandHeight * 0.2f

                drawOval(
                    color = Color(1f, 1f, 1f, alpha),
                    topLeft = Offset(xx + ox, yy + oy),
                    size = Size(ovalW, ovalH)
                )

                xx += stepX * 0.85f
            }
        }

        drawRoundRect(
            brush =
                Brush.verticalGradient(
                    colors = listOf(Color(0x66000000), Color.Transparent),
                    startY = y,
                    endY = y + h * 0.55f
                ),
            topLeft = Offset(x, y),
            size = Size(w, h * 0.55f),
            cornerRadius = CornerRadius(r, r)
        )
    }

    drawLine(
        color = Color(0x88FFFFFF),
        start = Offset(x, y + 2f),
        end = Offset(x + w, y + 2f),
        strokeWidth = 4f
    )

    val bushSize = 120f * sx

    drawImageAt(
        bushPainter,
        Offset(x + 50f * sx, y - bushSize * 0.7f),
        Size(bushSize, bushSize)
    )
    if (w > 300f * sx) {
        drawImageAt(
            bushPainter,
            Offset(x + 200f * sx, y - bushSize * 0.7f),
            Size(bushSize, bushSize)
        )
    }
}

private fun DrawScope.drawImageAt(painter: Painter, pos: Offset, size: Size) {
    translate(pos.x, pos.y) { with(painter) { draw(size) } }
}

private fun DrawScope.drawEgg(pos: Offset, s: Size, sx: Float, sy: Float, painter: Painter) {
    val x = pos.x * sx
    val y = pos.y * sy
    val w = s.width * sx

    // Maintain aspect ratio for the egg
    val drawnH = s.height * sy
    val intrinsicSize = painter.intrinsicSize
    val aspectRatio =
        if (intrinsicSize.height != 0f) intrinsicSize.width / intrinsicSize.height else 1f
    val drawnW = drawnH * aspectRatio

    // Center on X
    val offsetX = (w - drawnW) / 2f

    translate(x + offsetX, y) { with(painter) { draw(Size(drawnW, drawnH)) } }
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
    val drawnH = s.height * sy
    val isUpsideDown = rotation > 90f || rotation < -90f
    val intrinsicSize = painter.intrinsicSize
    val aspectRatio =
        if (intrinsicSize.height != 0f) intrinsicSize.width / intrinsicSize.height else 1f
    val drawnW = drawnH * aspectRatio

    // Center on X within its logical bounding box
    val offsetX = (w - drawnW) / 2f

    rotate(degrees = rotation, pivot = Offset(x + w / 2, y + drawnH / 2)) {
        translate(x + offsetX, y) {
            with(painter) {
                // Flip horizontally if upside down (rotation > 90) so it faces
                // right
                if (isUpsideDown) {
                    scale(
                        scaleX = -1f,
                        scaleY = 1f,
                        pivot = Offset(drawnW / 2f, drawnH / 2f)
                    ) { draw(Size(drawnW, drawnH)) }
                } else {
                    draw(Size(drawnW, drawnH))
                }
            }
        }
    }
}

@Composable
fun JumpIndicator(state: GameState, modifier: Modifier = Modifier) {
    val progress =
        if (state.maxJumpCooldownMs > 0) {
            (1f - (state.jumpCooldownMs / state.maxJumpCooldownMs)).coerceIn(0f, 1f)
        } else 1f

    Box(
        modifier =
            modifier
                .width(200.dp)
                .height(12.dp)
                .background(
                    color = Color.Black.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(6.dp)
                )
                .border(
                    width = 2.dp,
                    color = Color.White.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(6.dp)
                )
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
                    .background(
                        color =
                            if (progress >= 1f) Color(0xFFFFEB3B)
                            else Color(0xFFFF9800),
                        shape = RoundedCornerShape(6.dp)
                    )
        )
    }
}
