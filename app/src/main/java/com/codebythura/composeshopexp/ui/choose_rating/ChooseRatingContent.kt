package com.codebythura.composeshopexp.ui.choose_rating

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ChooseRatingContent(modifier: Modifier = Modifier) {
    Text(
        text = "Choose Rating Content",
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    )
}