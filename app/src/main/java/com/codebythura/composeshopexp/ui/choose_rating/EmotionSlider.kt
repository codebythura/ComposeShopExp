package com.codebythura.composeshopexp.ui.choose_rating

import androidx.annotation.FloatRange
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codebythura.composeshopexp.ui.AppRating
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun EmotionSlider(
    @FloatRange(from = 0.0, to = 1.0) value: Float,
    onValueChanged : (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CustomSlider(
            value = value,
            onValueChanged = onValueChanged,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            AppRating.entries.map { it.text }.forEachIndexed { index, text ->
                val selectedIndex = (value * (AppRating.entries.size - 1)).roundToInt()
                Text(
                    text,
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    color = if(selectedIndex == index) Color.Black else Color.Gray,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}


private fun snapToPoint(@FloatRange(from = 0.0, to = 1.0) value : Float, onChanged : (Float) -> Unit) {
    val stepFractions = listOf(0.0f, 0.5f, 1.0f)
    stepFractions.minByOrNull { abs(it - value.coerceIn(0f,1f)) }?.let(onChanged)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomSlider(
    @FloatRange(from = 0.0, to = 1.0) value : Float,
    onValueChanged : (Float) -> Unit,
    modifier: Modifier = Modifier,
    trackHeight: Dp = 8.dp,
    thumbRadius: Dp = 18.dp,
) {

    val scope = rememberCoroutineScope()
    var dragging by remember { mutableStateOf(false) }
    val animatable = remember { Animatable(value.coerceIn(0f,1f)) }

    LaunchedEffect(value) {
        if(!dragging) {
            snapToPoint(value) {
                scope.launch { animatable.snapTo(it) }
            }
        }
    }

    LaunchedEffect(animatable) {
        snapshotFlow { animatable.value }
            .collect { onValueChanged(it) }
    }

    fun onDragEnd() {
        snapToPoint(animatable.value) {
            scope.launch {
                animatable.animateTo(it)
                dragging = false
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(thumbRadius * 2)
            .drawSliderTrackBackground(thumbRadius = thumbRadius * 0.5f, trackHeight = trackHeight)
    ) {

        // Slider thumb
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {

            val maxWidthPx = constraints.maxWidth.toFloat()
            val thumbRadiusPx = with(LocalDensity.current) { thumbRadius.toPx() }
            val availableWidth = maxWidthPx - (thumbRadiusPx * 2)
            val thumbOffsetPx = (availableWidth * animatable.value)

            Box(
                modifier = Modifier
                    .offset { IntOffset(thumbOffsetPx.toInt(), 0) }
                    .size(thumbRadius * 2)
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragEnd = ::onDragEnd,
                            onDragCancel = ::onDragEnd,
                            onDragStart = { dragging = true },
                            onHorizontalDrag = { change, dragAmount ->
                                change.consume()
                                if (availableWidth > 0f) {
                                    val deltaFraction = dragAmount / availableWidth
                                    scope.launch {
                                        val previousValue = animatable.value
                                        animatable.snapTo((previousValue + deltaFraction).coerceIn(0f, 1f))
                                    }
                                }
                            }
                        )
                    }
                    .background(Color.Black, CircleShape)
                    .drawWithContent {
                        drawContent()
                        val rotationAngle = if (animatable.value <= 0.5f) {
                            0f
                        } else {
                            ((animatable.value - 0.5f) / 0.5f) * 180f
                        }
                        val radius = thumbRadius.toPx()
                        val smileWidth = radius * 1.0f
                        val smileHeight = radius * 0.68f
                        rotate(degrees = rotationAngle, pivot = center) {
                            drawArc(
                                color = Color.White,
                                startAngle = -20f,
                                sweepAngle = -140f,
                                useCenter = false,
                                topLeft = Offset(center.x - smileWidth / 2, center.y - smileHeight / 2.8f),
                                size = Size(smileWidth, smileHeight),
                                style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round),
                            )
                        }
                    }
            )
        }
    }

}

private fun Modifier.drawSliderTrackBackground(
    thumbRadius: Dp,
    trackHeight: Dp,
    color : Color = Color.Gray
) = drawBehind {

    val thumbRadiusPx = thumbRadius.toPx()
    val centerY = size.height / 2
    val endX = size.width - thumbRadiusPx

    // draw track background
    drawLine(
        color = color,
        start = Offset(thumbRadiusPx, centerY),
        end = Offset(endX, centerY),
        strokeWidth = trackHeight.toPx(),
        cap = StrokeCap.Round
    )

    // draw circles for each steps
    val circleCenters = listOf(
        Offset(thumbRadiusPx, centerY),
        center,
        Offset(endX, centerY)
    )

    for (circleCenter in circleCenters) {
        drawCircle(
            color = color,
            center = circleCenter,
            radius = thumbRadiusPx
        )
    }

}