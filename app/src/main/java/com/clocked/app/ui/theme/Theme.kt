package com.clocked.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val ClockedColorScheme = darkColorScheme(
    primary = ClockedBlue,
    onPrimary = ClockedBackground,
    primaryContainer = ClockedBlueVariant,
    background = ClockedBackground,
    surface = ClockedSurface,
    surfaceVariant = ClockedSurfaceVariant,
    onBackground = ClockedOnSurface,
    onSurface = ClockedOnSurface,
    onSurfaceVariant = ClockedOnSurfaceMuted,
)

@Composable
fun ClockedTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ClockedColorScheme,
        typography = ClockedTypography,
        content = content
    )
}
