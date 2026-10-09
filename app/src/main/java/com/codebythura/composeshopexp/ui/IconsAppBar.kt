package com.codebythura.composeshopexp.ui


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp


@Composable
fun IconsAppBar(
    onCloseClicked: () -> Unit,
    onInfoClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .statusBarsPadding()
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircleShapedIconButton(
            onClick = onCloseClicked,
            icon = Icons.Outlined.Close,
            contentDescription = "Close",
        )
        CircleShapedIconButton(
            onClick = onInfoClicked,
            icon = Icons.Outlined.Info,
            contentDescription = "Info",
        )
    }
}

@Composable
private fun CircleShapedIconButton(
    icon : ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(containerColor = Color.Black.copy(alpha = 0.1f)),
) {
    IconButton(
        onClick = onClick,
        shape = CircleShape,
        colors = colors,
        modifier = modifier.size(50.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription
        )
    }
}