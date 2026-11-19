package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import com.example.ui.i18n.AppLanguage
import com.example.ui.i18n.AppStrings
import com.example.ui.i18n.LocalAppLanguage
import com.example.ui.i18n.LocalAppStrings

private val BioluminescentLabScheme = darkColorScheme(
    primary = BioCyan,
    onPrimary = ObsidianBg,
    primaryContainer = BioCyanContainer,
    onPrimaryContainer = BioCyan,
    secondary = ChloroplastEmerald,
    onSecondary = ObsidianBg,
    secondaryContainer = EmeraldContainer,
    onSecondaryContainer = ChloroplastEmerald,
    tertiary = AtpGold,
    onTertiary = ObsidianBg,
    tertiaryContainer = GoldContainer,
    onTertiaryContainer = AtpGold,
    error = CoralTrap,
    onError = ObsidianBg,
    errorContainer = CoralContainer,
    onErrorContainer = CoralTrap,
    background = ObsidianBg,
    onBackground = TextPrimary,
    surface = LabSurface,
    onSurface = TextPrimary,
    surfaceVariant = LabSurfaceElevated,
    onSurfaceVariant = TextSecondary,
    outline = LabBorder,
    outlineVariant = LabSurfaceHighlight
)

private val LabShapes = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(22.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun ZistNegarTheme(
    language: AppLanguage = AppLanguage.FA,
    content: @Composable () -> Unit
) {
    val strings = AppStrings.forLanguage(language)
    CompositionLocalProvider(
        LocalLayoutDirection provides language.layoutDirection,
        LocalAppLanguage provides language,
        LocalAppStrings provides strings
    ) {
        MaterialTheme(
            colorScheme = BioluminescentLabScheme,
            typography = ZistTypography,
            shapes = LabShapes,
            content = content
        )
    }
}
