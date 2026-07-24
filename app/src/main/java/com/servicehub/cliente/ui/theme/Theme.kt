package com.servicehub.cliente.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColors = lightColorScheme(
    primary = ServiceHubBlue,
    onPrimary = Color.White,
    secondary = ServiceHubBlueDark,
    onSecondary = Color.White,
    background = ServiceHubBackground,
    onBackground = ServiceHubOnSurface,
    surface = ServiceHubSurface,
    onSurface = ServiceHubOnSurface,
    surfaceVariant = ServiceHubSurfaceVariant,
    onSurfaceVariant = ServiceHubOnSurface
)

private val DarkColors = darkColorScheme(
    primary = ServiceHubBlue,
    onPrimary = Color.White,
    secondary = ServiceHubSurfaceVariant,
    onSecondary = ServiceHubOnSurface
)

@Composable
fun ServiceHubClienteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = ServiceHubTypography,
        content = content
    )
}
