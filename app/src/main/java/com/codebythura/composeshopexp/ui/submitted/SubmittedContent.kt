package com.codebythura.composeshopexp.ui.submitted

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SubmittedContent(modifier: Modifier = Modifier) {
    Text(
        text = "Submitted Content",
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    )
}