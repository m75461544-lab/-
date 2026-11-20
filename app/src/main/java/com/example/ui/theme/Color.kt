package com.example.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.example.model.AppThemeMode

@Immutable
data class ExtendedColors(
    val goldAccent: Color,
    val warmSecondary: Color,
    val emeraldSuccess: Color,
    val crimsonError: Color,
    val turquoiseInfo: Color,
    val cardElevated: Color,
    val cardBorder: Color,
    val heroGradientStart: Color,
    val heroGradientEnd: Color,
    val poemSurface: Color
)

// 1. Default Theme: Dark Brown (قهوه‌ای تیره - کهن‌نامه)
val EspressoBg = Color(0xFF160E0A)
val EspressoSurface = Color(0xFF211610)
val EspressoSurfaceVariant = Color(0xFF2E1F17)
val EspressoCardElevated = Color(0xFF36251C)
val EspressoGold = Color(0xFFE0A96D)
val EspressoOnGold = Color(0xFF2A180C)
val EspressoGoldContainer = Color(0xFF4F3421)
val EspressoTurquoise = Color(0xFF66B9B0)
val EspressoText = Color(0xFFF6ECE1)
val EspressoTextMuted = Color(0xFFD0BEAE)
val EspressoBorder = Color(0xFF563D2E)

// 2. Midnight Indigo (سرمه‌ای شبِ شیراز)
val IndigoBg = Color(0xFF0C121E)
val IndigoSurface = Color(0xFF141E30)
val IndigoSurfaceVariant = Color(0xFF1E2B42)
val IndigoGold = Color(0xFFF0C35E)
val IndigoTeal = Color(0xFF4FD1C5)
val IndigoText = Color(0xFFF0F4FA)
val IndigoTextMuted = Color(0xFFB3C0D4)
val IndigoBorder = Color(0xFF334769)

// 3. Emerald Garden (سبز زمردی باغ نظر)
val EmeraldBg = Color(0xFF0A1713)
val EmeraldSurface = Color(0xFF12251E)
val EmeraldSurfaceVariant = Color(0xFF1A342B)
val EmeraldPrimary = Color(0xFF5AD4A2)
val EmeraldGold = Color(0xFFE6B96E)
val EmeraldText = Color(0xFFEEF7F2)
val EmeraldTextMuted = Color(0xFFB5CEC3)
val EmeraldBorder = Color(0xFF2C5445)

// 4. Parchment Light (کاغذی روشن - نسخه خطی)
val ParchmentBg = Color(0xFFF8F2E7)
val ParchmentSurface = Color(0xFFEFE4D2)
val ParchmentSurfaceVariant = Color(0xFFE5D6BE)
val ParchmentPrimary = Color(0xFF6B3E26)
val ParchmentGold = Color(0xFFA76D2E)
val ParchmentText = Color(0xFF2B1A10)
val ParchmentTextMuted = Color(0xFF634B3B)
val ParchmentBorder = Color(0xFFCBB69B)

fun colorSchemeFor(mode: AppThemeMode): ColorScheme = when (mode) {
    AppThemeMode.DARK_BROWN -> darkColorScheme(
        primary = EspressoGold,
        onPrimary = EspressoOnGold,
        primaryContainer = EspressoGoldContainer,
        onPrimaryContainer = Color(0xFFFFE5C7),
        secondary = EspressoTurquoise,
        onSecondary = Color(0xFF082421),
        secondaryContainer = Color(0xFF1D433F),
        onSecondaryContainer = Color(0xFFBFECE6),
        tertiary = Color(0xFFD98A6C),
        onTertiary = Color(0xFF2E1208),
        background = EspressoBg,
        onBackground = EspressoText,
        surface = EspressoSurface,
        onSurface = EspressoText,
        surfaceVariant = EspressoSurfaceVariant,
        onSurfaceVariant = EspressoTextMuted,
        outline = EspressoBorder,
        error = Color(0xFFF28B82),
        onError = Color(0xFF3B0E0A)
    )

    AppThemeMode.MIDNIGHT_INDIGO -> darkColorScheme(
        primary = IndigoGold,
        onPrimary = Color(0xFF261C04),
        primaryContainer = Color(0xFF473714),
        onPrimaryContainer = Color(0xFFFFEAB8),
        secondary = IndigoTeal,
        onSecondary = Color(0xFF052422),
        secondaryContainer = Color(0xFF1A4246),
        onSecondaryContainer = Color(0xFFC0F6F0),
        tertiary = Color(0xFF9FB4FF),
        onTertiary = Color(0xFF0F1B3D),
        background = IndigoBg,
        onBackground = IndigoText,
        surface = IndigoSurface,
        onSurface = IndigoText,
        surfaceVariant = IndigoSurfaceVariant,
        onSurfaceVariant = IndigoTextMuted,
        outline = IndigoBorder,
        error = Color(0xFFF48FB1),
        onError = Color(0xFF3B091D)
    )

    AppThemeMode.EMERALD_GARDEN -> darkColorScheme(
        primary = EmeraldPrimary,
        onPrimary = Color(0xFF05261A),
        primaryContainer = Color(0xFF1B4B39),
        onPrimaryContainer = Color(0xFFC5F7E0),
        secondary = EmeraldGold,
        onSecondary = Color(0xFF291B07),
        secondaryContainer = Color(0xFF473419),
        onSecondaryContainer = Color(0xFFFFE7C0),
        tertiary = Color(0xFF7FD1D1),
        onTertiary = Color(0xFF072323),
        background = EmeraldBg,
        onBackground = EmeraldText,
        surface = EmeraldSurface,
        onSurface = EmeraldText,
        surfaceVariant = EmeraldSurfaceVariant,
        onSurfaceVariant = EmeraldTextMuted,
        outline = EmeraldBorder,
        error = Color(0xFFF28B82),
        onError = Color(0xFF3B0E0A)
    )

    AppThemeMode.PARCHMENT_LIGHT -> lightColorScheme(
        primary = ParchmentPrimary,
        onPrimary = Color(0xFFFFF8F2),
        primaryContainer = Color(0xFFDCC3AA),
        onPrimaryContainer = Color(0xFF2E170B),
        secondary = Color(0xFF1F6A63),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFC3E4DF),
        onSecondaryContainer = Color(0xFF092A27),
        tertiary = ParchmentGold,
        onTertiary = Color(0xFFFFFFFF),
        background = ParchmentBg,
        onBackground = ParchmentText,
        surface = ParchmentSurface,
        onSurface = ParchmentText,
        surfaceVariant = ParchmentSurfaceVariant,
        onSurfaceVariant = ParchmentTextMuted,
        outline = ParchmentBorder,
        error = Color(0xFFB3261E),
        onError = Color(0xFFFFFFFF)
    )
}

fun extendedColorsFor(mode: AppThemeMode): ExtendedColors = when (mode) {
    AppThemeMode.DARK_BROWN -> ExtendedColors(
        goldAccent = EspressoGold,
        warmSecondary = Color(0xFFD98A6C),
        emeraldSuccess = Color(0xFF66C992),
        crimsonError = Color(0xFFEA7B75),
        turquoiseInfo = EspressoTurquoise,
        cardElevated = EspressoCardElevated,
        cardBorder = EspressoBorder,
        heroGradientStart = Color(0xFF3A2418),
        heroGradientEnd = Color(0xFF1E120D),
        poemSurface = Color(0xFF2A1B13)
    )

    AppThemeMode.MIDNIGHT_INDIGO -> ExtendedColors(
        goldAccent = IndigoGold,
        warmSecondary = Color(0xFF8DA7F8),
        emeraldSuccess = Color(0xFF5AD4A2),
        crimsonError = Color(0xFFF28B82),
        turquoiseInfo = IndigoTeal,
        cardElevated = Color(0xFF22314B),
        cardBorder = IndigoBorder,
        heroGradientStart = Color(0xFF21314F),
        heroGradientEnd = Color(0xFF111A2B),
        poemSurface = Color(0xFF19263D)
    )

    AppThemeMode.EMERALD_GARDEN -> ExtendedColors(
        goldAccent = EmeraldGold,
        warmSecondary = EmeraldPrimary,
        emeraldSuccess = Color(0xFF6FE0B0),
        crimsonError = Color(0xFFF28B82),
        turquoiseInfo = Color(0xFF6FD4CC),
        cardElevated = Color(0xFF1F3D32),
        cardBorder = EmeraldBorder,
        heroGradientStart = Color(0xFF1B3B2F),
        heroGradientEnd = Color(0xFF0E2019),
        poemSurface = Color(0xFF152D24)
    )

    AppThemeMode.PARCHMENT_LIGHT -> ExtendedColors(
        goldAccent = ParchmentGold,
        warmSecondary = ParchmentPrimary,
        emeraldSuccess = Color(0xFF1D7A4C),
        crimsonError = Color(0xFFB3261E),
        turquoiseInfo = Color(0xFF1F6A63),
        cardElevated = Color(0xFFFFFBF4),
        cardBorder = ParchmentBorder,
        heroGradientStart = Color(0xFFE7D6BE),
        heroGradientEnd = Color(0xFFF4EBD9),
        poemSurface = Color(0xFFFBF6EC)
    )
}
