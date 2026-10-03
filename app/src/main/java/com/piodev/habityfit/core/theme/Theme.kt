package com.piodev.habityfit.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val DarkColorScheme = darkColorScheme(
    background = Color(0xFF000000),
    onBackground = Color(0xFFFFFFFF),
    ///////////////////////////////////////////////////////////////////////////// Nav Bar y su texto
    surface = Color(0xFF121212),
    onSurface = Color(0xFFF2F2F2),
    ///////////////// Color de Iconos de Nav Bar (no seleccionados) y fondo de las Card y sus textos
    surfaceVariant = Color(0xFF363434),
    onSurfaceVariant = Color(0xFFBDBDBD),
    ////////////////////// Color de Iconos de Nav Bar y su texto (seleccionados), Botones navigacion
    primary = Color(0xFFD0BCFF),
    onPrimary = Color(0xFF381E72),
    //////////////////////////////////////////////////////////////////////////// BOTONES SECUNDARIOS
    secondary = Color(0xFF03A9F4),
    onSecondary = Color(0xFFFFFFFF),
    /////////////////////////////////////////////////////////////// Contraste de los card y su texto
    tertiary = Color(0xFFD4E157),
    onTertiary = Color(0xFF000000),
    ///////////////////////////////////////////////////////////////////////////// BAR NAV
    primaryContainer = Color(0xFF75A83C),
    onPrimaryContainer = Color(0xFFA6FF00),
    outlineVariant = Color(0xFFFFFFFF),
    /////////////////////////////////////////////////////////////////////////// Bordes y Separadores
    outline = Color(0xFF444746),
    ///////////////////////////////////////////////////////////////////////// Fondo y texto de error
    error = Color(0xFFD32F2F),
    onError = Color(0xFFFFFFFF)
)

private val LightColorScheme = lightColorScheme(
    background = Color(0xFFFBFBF9),
    onBackground = Color(0xFF000000),
    ///////////////////////////////////////////////////////////////////////////// Nav Bar y su texto
    surface = Color(0xFFF2F2F0),
    onSurface = Color(0xFF000000),
    ///////////////// Color de Iconos de Nav Bar (no seleccionados) y fondo de las Card y sus textos
    surfaceVariant = Color(0xFFD2D2CE),
    onSurfaceVariant = Color(0xFF454545),
    ////////////////////// Color de Iconos de Nav Bar y su texto (seleccionados), Botones navigacion
    primary = Color(0xFF000000),
    onPrimary = Color(0xFFFFFFFF),
    //////////////////////////////////////////////////////////////////////////// BOTONES SECUNDARIOS
    secondary = Color(0xFF1A73E8),
    onSecondary = Color(0xFFFFFFFF),
    /////////////////////////////////////////////////////////////// Contraste de los card y su texto
    tertiary = Color(0xFFFFEB3B),
    onTertiary = Color(0xFF000000),
    ///////////////////////////////////////////////////////////////////////////// BAR NAV
    primaryContainer = Color(0xFFFFEB3B),
    onPrimaryContainer = Color(0xFF000000),
    outlineVariant = Color(0xFF606060),
    /////////////////////////////////////////////////////////////////////////// Bordes y Separadores
    outline = Color(0xFF121212),
    ///////////////////////////////////////////////////////////////////////// Fondo y texto de error
    error = Color(0xFFD32F2F),
    onError = Color(0xFFFFFFFF)
)

// Tipografía para darle ese toque "Pro"
private val AppTypography = Typography(
    titleLarge = TextStyle(
        fontWeight = FontWeight.Black,
        fontSize = 22.sp,
        letterSpacing = 0.5.sp
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.ExtraBold,
        fontSize = 18.sp
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    )
)

@Composable
fun SetpointTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}