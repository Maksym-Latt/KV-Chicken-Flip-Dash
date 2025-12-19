package com.chicken.flipdash.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp

@Composable
fun GamePillSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var widthPx by remember { mutableIntStateOf(0) }
    val shape = RoundedCornerShape(999.dp)

    fun updateFromX(x: Float) {
        if (widthPx <= 0) return
        val v = (x / widthPx.toFloat()).coerceIn(0f, 1f)
        onValueChange(v)
    }

    Box(
        modifier = modifier
            .height(40.dp)
            .fillMaxWidth()
            .onSizeChanged { widthPx = it.width }
            .clip(shape)
            .background(Color(0xFFF6E2C2))
            .border(2.dp, Color(0xFF5E3B00), shape)
            .pointerInput(widthPx) {
                detectTapGestures { offset -> updateFromX(offset.x) }
            }
            .pointerInput(widthPx) {
                detectDragGestures { change, _ ->
                    change.consume()
                    updateFromX(change.position.x)
                }
            }
            .padding(3.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(value.coerceIn(0f, 1f))
                .clip(shape)
                .background(Color(0xFFF39A00))
        )
    }
}