package com.chicken.flipdash.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.chicken.flipdash.ui.theme.CoinyFontFamily

@Composable
fun GameText(
        text: String,
        modifier: Modifier = Modifier,
        fontSize: TextUnit = 24.sp,
        color: Color = Color.White,
        outlineColor: Color = Color.Black,
        outlineWidth: Int = 2,
        fontWeight: FontWeight = FontWeight.Normal
) {
    val textStyle =
            TextStyle(
                    fontFamily = CoinyFontFamily,
                    fontSize = fontSize,
                    fontWeight = fontWeight,
                    textAlign = TextAlign.Center,
                    platformStyle = PlatformTextStyle(includeFontPadding = false)
            )

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        // Efficient outline using Stroke
        Text(
                text = text,
                style =
                        textStyle.copy(
                                color = outlineColor,
                                drawStyle =
                                        Stroke(
                                                miter = 10f,
                                                width = outlineWidth.toFloat() * 2f,
                                                join = StrokeJoin.Round
                                        )
                        )
        )
        // Main text fill
        Text(text = text, style = textStyle.copy(color = color))
    }
}
