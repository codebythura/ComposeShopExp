package com.codebythura.composeshopexp.ui.submitted

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
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codebythura.composeshopexp.ui.choose_rating.EmotionalFace

@OptIn(ExperimentalTransitionApi::class)
@Composable
fun SubmittedContent(
    @FloatRange(from = 0.0, to = 1.0) value: Float,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onShoppingContinued : () -> Unit,
    modifier: Modifier = Modifier,
) {

    val transition = animatedVisibilityScope.transition
        .createChildTransition("submitted_transition") { it == EnterExitState.Visible }

    Column(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Spacer(modifier = Modifier.weight(1f))
        with(sharedTransitionScope) {
            EmotionalFace(
                value = value,
                modifier = Modifier
                    .sharedElement(
                        sharedContentState = rememberSharedContentState(key = "face"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
            )
        }
        AnimatedText(
            transition = transition,
            modifier = Modifier.padding(top = 18.dp)
        )
        Spacer(modifier = Modifier.weight(1.5f))
        ContinueToShoppingButton(onClick = onShoppingContinued)
    }
}

@Composable
private fun ContinueToShoppingButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .padding(start = 32.dp, end = 32.dp, bottom = 32.dp)
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black,
            contentColor = Color.White
        )
    ) {
        Text(
            text = "Continue my shopping",
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(end = 8.dp)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
        )
    }
}

@Composable
private fun AnimatedText(
    transition: Transition<Boolean>,
    modifier: Modifier = Modifier,
) {
    transition.AnimatedVisibility(
        visible = { it },
        enter = fadeIn() + slideInVertically { it },
        exit = fadeOut() + slideOutVertically { it },
        modifier = modifier,
    ) {
        Text(
            text = "Thank you for your\n feedback.",
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black.copy(alpha = 0.68f)
            ),
        )
    }
}