package com.codebythura.composeshopexp.ui.choose_rating

import androidx.annotation.FloatRange
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.util.lerp


@Composable
fun EmotionalFace(
    @FloatRange(from = 0.0, to = 1.0) value: Float,
    modifier: Modifier = Modifier,
) {

    Canvas(modifier = modifier.fillMaxWidth().aspectRatio(2f)) {

        val canvasWidth = size.width
        val canvasHeight = size.height
        val center = Offset(x = canvasWidth / 2, y = canvasHeight / 2)
        val radius = minOf(canvasWidth, canvasHeight) / 2f

        val eyeWidth = radius * driveEyeWithMultiplier(value)
        val eyeHeight = radius * driveEyeHeightMultiplier(value)

        val eyeOffsetX = radius * driveOffsetXMultiplier(value)

        rotate(degrees = -1 * driveEyeRotation(value), pivot = Offset(center.x - eyeHeight / 2,center.y)) {
            drawRoundRect(
                color = Color.Black,
                topLeft = Offset(center.x - eyeWidth - eyeOffsetX, center.y - eyeHeight / 1.6f),
                size = Size(eyeWidth, eyeHeight),
                cornerRadius = CornerRadius(x = eyeWidth, y = eyeWidth)
            )
        }

        rotate(degrees = driveEyeRotation(value),pivot = Offset(center.x + eyeHeight / 2,center.y)) {
            drawRoundRect(
                color = Color.Black,
                topLeft = Offset(center.x + eyeOffsetX, center.y - eyeHeight / 1.6f),
                size = Size(eyeWidth, eyeHeight),
                cornerRadius = CornerRadius(x = eyeWidth, y = eyeWidth)
            )
        }

        rotate(degrees = driveMouthRotationMultiplier(value), pivot = Offset(center.x, center.y + (120f * 1.2f))) {
            drawArc(
                color = Color.Black,
                startAngle = 35f,
                sweepAngle = 110f,
                useCenter = false,
                style = Stroke(width = 32f, cap = StrokeCap.Round),
                size = Size(180f, 120f),
                topLeft = Offset(center.x - 90f, center.y + 60f)
            )
        }


        // Draw lines
//        drawLine(
//            color = Color.Green,
//            start = Offset(canvasWidth / 2f, 0f),
//            end = Offset(canvasWidth / 2f, canvasHeight),
//            strokeWidth = 2f
//        )
//
//        drawLine(
//            color = Color.Green,
//            start = Offset(0f, canvasHeight / 2f),
//            end = Offset(canvasWidth, canvasHeight / 2f),
//            strokeWidth = 2f
//        )

    }

}

private fun driveEyeRotation(@FloatRange(from = 0.0, to = 1.0) value: Float) : Float {
    return if(value <= 0.35f) {
        lerp(start = 30f,0f, fraction = value / 0.35f)
    } else {
        0f
    }
}

private fun driveEyeWithMultiplier(@FloatRange(from = 0.0, to = 1.0) value: Float): Float {
    return if (value <= 0.5f) {
        lerp(start = 0.40f, stop = 1.34f, fraction = value / 0.5f)
    } else {
        lerp(start = 1.34f, stop = 1.40f, fraction = (value - 0.5f) / 0.5f)
    }
}

private fun driveEyeHeightMultiplier(@FloatRange(from = 0.0, to = 1.0) value: Float): Float {
    return if (value <= 0.5f) {
        0.4f
    } else {
        lerp(start = 0.4f, stop = 1.4f, fraction = (value - 0.5f) / 0.5f)
    }
}

private fun driveMouthRotationMultiplier(@FloatRange(from = 0.0, to = 1.0) value: Float): Float {
    return if (value <= 0.5f) {
        180f
    } else {
        lerp(start = 180f, stop = 0f, fraction = (value - 0.5f) / 0.5f)
    }
}

private fun driveOffsetXMultiplier(@FloatRange(from = 0.0, to = 1.0) value: Float): Float {
    return if (value <= 0.5f) {
        lerp(start = 0.2f, stop = 0.1f, fraction = value / 0.5f)
    } else {
        0.1f
    }
}