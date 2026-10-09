package com.codebythura.composeshopexp.ui.enter_note

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun EnterNoteContent(modifier: Modifier = Modifier) {
    Text(
        text = "Enter Note Content",
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    )
}