package com.codebythura.composeshopexp.ui.choose_rating

import androidx.annotation.FloatRange
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.ExperimentalTransitionApi
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codebythura.composeshopexp.ui.AppRating
import com.codebythura.composeshopexp.ui.SubmitButton
import kotlin.math.roundToInt

@OptIn(ExperimentalTransitionApi::class)
@Composable
fun ChooseRatingContent(
    @FloatRange(from = 0.0, to = 1.0) value: Float,
    onValueChanged: (Float) -> Unit,
    onAddNoteClicked: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {

    val transition = animatedVisibilityScope.transition
        .createChildTransition("choose_rating_transition") { it == EnterExitState.Visible }

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AnimatedTitle(transition = transition)
        with(sharedTransitionScope) {
            EmotionalFace(
                value = value,
                modifier = Modifier
                    .padding(vertical = 32.dp)
                    .sharedElement(
                        sharedContentState = rememberSharedContentState(key = "face"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
            )
        }
        AnimatedEmotionTextPager(
            value = value,
            transition = transition,
            modifier = Modifier.padding(vertical = 18.dp)
        )
        AnimatedEmotionSlider(
            value = value,
            transition = transition,
            onValueChanged = onValueChanged,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 18.dp)
        )
        AnimatedAddNotePlaceholder(
            transition = transition,
            onClick = onAddNoteClicked,
            modifier = Modifier.padding(32.dp)
        )
    }

}


@Composable
private fun AnimatedAddNotePlaceholder(
    transition: Transition<Boolean>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    transition.AnimatedVisibility(
        visible = { it },
        enter = fadeIn() + slideInVertically { -it },
        exit = fadeOut() + slideOutVertically { -it / 2 },
        modifier = modifier,
    ) {
        AddNotePlaceholder(onClick = onClick)
    }
}

@Composable
private fun AddNotePlaceholder(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .height(58.dp)
            .fillMaxWidth()
            .background(Color.Black.copy(alpha = 0.14f), shape = RoundedCornerShape(54.dp))
            .clip(RoundedCornerShape(54.dp))
            .clickable(onClick = onClick)
            .padding(start = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Add note",
            color = Color.DarkGray,
            modifier = Modifier.weight(1f),
        )
        SubmitButton(
            onClick = onClick,
            modifier = Modifier.fillMaxHeight()
        )
    }
}

@Composable
fun AnimatedEmotionSlider(
    transition: Transition<Boolean>,
    @FloatRange(from = 0.0, to = 1.0) value: Float,
    onValueChanged: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    transition.AnimatedVisibility(
        visible = { it },
        enter = fadeIn() + slideInHorizontally { -it },
        exit = fadeOut() + slideOutHorizontally { -it },
        modifier = modifier,
    ) {
        EmotionSlider(
            value = value,
            onValueChanged = onValueChanged,
        )
    }
}


@Composable
private fun AnimatedEmotionTextPager(
    @FloatRange(from = 0.0, to = 1.0) value: Float,
    transition: Transition<Boolean>,
    modifier: Modifier = Modifier,
) {

    transition.AnimatedVisibility(
        visible = { it },
        enter = fadeIn() + slideInHorizontally { -it },
        exit = fadeOut() + slideOutHorizontally { -it },
        modifier = modifier.fillMaxWidth(),
    ) {

        val pagerState = rememberPagerState { AppRating.entries.size }

        LaunchedEffect(value) {
            val totalPosition = (value * 2f).coerceIn(0f, 2f)
            val targetPage = totalPosition.roundToInt()
            val pageOffset = totalPosition - targetPage
            pagerState.scrollToPage(
                page = targetPage,
                pageOffsetFraction = pageOffset
            )
        }

        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false,
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        ) { page ->
            Text(
                text = AppRating.entries[page].text.uppercase(),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 54.sp,
                    letterSpacing = (-4).sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black.copy(alpha = 0.67f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(align = Alignment.CenterHorizontally),
            )
        }

    }
}

@Composable
private fun AnimatedTitle(
    transition: Transition<Boolean>,
    modifier: Modifier = Modifier,
) {
    transition.AnimatedVisibility(
        visible = { it },
        enter = fadeIn() + slideInVertically { -it },
        exit = fadeOut() + slideOutVertically { -it },
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = "How was your shopping\n experience?",
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}