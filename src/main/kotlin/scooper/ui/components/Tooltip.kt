package scooper.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.TooltipPlacement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class TooltipPostion {
    Top, Bottom
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Tooltip(
    text: String = "...",
    position: TooltipPostion = TooltipPostion.Bottom,
    content: @Composable () -> Unit,
) {
    val tooltipPlacement = when (position) {
        TooltipPostion.Top -> {
            TooltipPlacement.CursorPoint(
                alignment = Alignment.TopStart,
                offset = DpOffset((-8).dp, (-8).dp) // tooltip offset
            )
        }

        TooltipPostion.Bottom -> {
            TooltipPlacement.CursorPoint(
                alignment = Alignment.BottomStart,
                offset = DpOffset((-8).dp, 8.dp) // tooltip offset
            )
        }
    }
    TooltipArea(
        tooltip = {
            Surface(
                modifier = Modifier.shadow(4.dp),
                color = Color(255, 255, 210),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = text,
                    modifier = Modifier.padding(10.dp),
                    color = Color.Gray,
                    fontSize = 13.sp,
                )
            }
        },
        delayMillis = 600, // in millisecond
        tooltipPlacement = tooltipPlacement
    ) {
        content()
    }
}