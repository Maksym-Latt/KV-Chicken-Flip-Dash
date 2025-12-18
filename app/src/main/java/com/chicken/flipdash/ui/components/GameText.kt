package com.chicken.flipdash.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
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
    Box(modifier = modifier) {
        // Outline layers
        for (x in -outlineWidth..outlineWidth) {
            for (y in -outlineWidth..outlineWidth) {
                if (x != 0 || y != 0) {
                    Text(
                            text = text,
                            fontFamily = CoinyFontFamily,
                            fontSize = fontSize,
                            fontWeight = fontWeight,
                            color = outlineColor,
                            modifier = Modifier.offset(x.dp, y.dp)
                    )
                }
            }
        }
        // Main text
        Text(
                text = text,
                fontFamily = CoinyFontFamily,
                fontSize = fontSize,
                fontWeight = fontWeight,
                color = color
        )
    }
}
