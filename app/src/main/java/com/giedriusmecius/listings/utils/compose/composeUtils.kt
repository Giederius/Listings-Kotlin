package com.giedriusmecius.listings.utils.compose

import android.animation.ArgbEvaluator
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.giedriusmecius.listings.ui.common.composeStyles.DisabledBackgroundColor

fun getColorAtProgress(progress: Float, start: Color, end: Color, evaluator: ArgbEvaluator): Color {
    return Color(evaluator.evaluate(progress, start.toArgb(), end.toArgb()) as Int)
}

@Composable
fun DashedDivider(modifier: Modifier, color: Color, thickness: Int) {
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(40f, 20f), 2f)
    Canvas(modifier.fillMaxWidth().height(thickness.dp)) {
        drawLine(
            color = DisabledBackgroundColor,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            pathEffect = pathEffect
        )
    }
}