package com.codebythura.composeshopexp.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.lerp
import com.codebythura.composeshopexp.ui.choose_rating.ChooseRatingContent
import com.codebythura.composeshopexp.ui.enter_note.EnterNoteContent
import com.codebythura.composeshopexp.ui.submitted.SubmittedContent


@Composable
fun ShopExpApp(
    modifier: Modifier = Modifier,
    rating: AppRating = AppRating.Good,
    step: AppStep = AppStep.CHOOSE_RATING,
) {

    var appStep by rememberSaveable { mutableStateOf(step) }
    var ratingValue by rememberSaveable { mutableFloatStateOf(rating.rating) }
    val animatedColor = when {
        ratingValue <= 0.5f -> {
            val fraction = (ratingValue / 0.5f).coerceIn(0f, 1f)
            lerp(AppRating.Bad.color, AppRating.NotBad.color, fraction)
        }
        else -> {
            val fraction = ((ratingValue - 0.5f) / 0.5f).coerceIn(0f, 1f)
            lerp(AppRating.NotBad.color, AppRating.Good.color, fraction)
        }
    }

    BackHandler(enabled = appStep == AppStep.ENTER_NOTE) {
        appStep = AppStep.CHOOSE_RATING
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = animatedColor,
        topBar = {
            IconsAppBar(
                onInfoClicked = {},
                onCloseClicked = {}
            )
        },
        content = { innerPadding ->
            SharedTransitionLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                AnimatedContent(
                    targetState = appStep,
                    modifier = Modifier.fillMaxSize(),
                    transitionSpec = { EnterTransition.None togetherWith ExitTransition.None },
                ) { targetState ->
                    when(targetState) {
                        AppStep.CHOOSE_RATING -> ChooseRatingContent(
                            value = ratingValue,
                            onValueChanged = { ratingValue = it },
                            onAddNoteClicked = { appStep = AppStep.ENTER_NOTE },
                            animatedVisibilityScope = this@AnimatedContent,
                            sharedTransitionScope = this@SharedTransitionLayout
                        )
                        AppStep.ENTER_NOTE -> EnterNoteContent(
                            value = ratingValue,
                            text = "",
                            onTextChange = {},
                            onNoteSubmit = { appStep = AppStep.SUBMITTED },
                            animatedVisibilityScope = this@AnimatedContent,
                            sharedTransitionScope = this@SharedTransitionLayout,
                        )
                        AppStep.SUBMITTED -> SubmittedContent()
                    }
                }
            }
        }
    )


}