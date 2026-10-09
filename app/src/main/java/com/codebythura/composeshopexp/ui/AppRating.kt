package com.codebythura.composeshopexp.ui

import androidx.compose.ui.graphics.Color


enum class AppRating(
    val text: String,
    val color: Color,
    val rating: Float,
) {
    Bad(text = "Bad", color = Color(0xFFf5724e), rating = 0f),
    NotBad(text = "Not Bad", color = Color(0xFFf0b537), rating = 0.5f),
    Good(text = "Good", color = Color(0xFFcae846), rating = 1f)
}