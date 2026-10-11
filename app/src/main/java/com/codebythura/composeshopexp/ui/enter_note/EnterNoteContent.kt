package com.codebythura.composeshopexp.ui.enter_note

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
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.codebythura.composeshopexp.ui.SubmitButton
import com.codebythura.composeshopexp.ui.choose_rating.EmotionalFace
import kotlinx.coroutines.delay

@OptIn(ExperimentalTransitionApi::class)
@Composable
fun EnterNoteContent(
    @FloatRange(from = 0.0, to = 1.0) value: Float,
    text: String,
    onTextChange: (String) -> Unit,
    onNoteSubmit: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier : Modifier = Modifier,
) {

    val transition = animatedVisibilityScope.transition
        .createChildTransition("enter_note_transition") { it == EnterExitState.Visible }

    Column(
        modifier = modifier
            .imePadding()
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
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
        AnimatedNoteTextField(
            text = text,
            onTextChange = onTextChange,
            onSubmit = onNoteSubmit,
            transition = transition,
            modifier = Modifier.padding(horizontal = 32.dp),
        )
        Spacer(modifier = Modifier.weight(2.5f))
    }
}

@Composable
fun AnimatedNoteTextField(
    text: String,
    onTextChange: (String) -> Unit,
    onSubmit: () -> Unit,
    transition: Transition<Boolean>,
    modifier: Modifier = Modifier,
) {
    transition.AnimatedVisibility(
        visible = { it },
        enter = fadeIn() + slideInVertically { it },
        exit = fadeOut() + slideOutVertically { it },
        modifier = modifier,
    ) {
        NoteTextField(
            text = text,
            onTextChange = onTextChange,
            onSubmit = onSubmit,
        )
    }
}

@Composable
private fun NoteTextField(
    text: String,
    onTextChange: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        delay(250L)
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color.White.copy(alpha = 0.6f),
                shape = RoundedCornerShape(24.dp)
            )
            .border(
                width = 2.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(18.dp)
            )
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BasicTextField(
            value = text,
            onValueChange = onTextChange,
            modifier = Modifier
                .focusRequester(focusRequester)
                .fillMaxWidth(),
            decorationBox = { innerTextField ->
                Box {
                    if (text.isEmpty()) {
                        Text("Add note", color = Color.Gray)
                    }
                    innerTextField()
                }
            }
        )
        SubmitButton(
            onClick = {
                keyboardController?.hide()
                focusManager.clearFocus()
                onSubmit()
            },
            modifier = Modifier
                .align(Alignment.End)
                .height(48.dp)
        )
    }

}
