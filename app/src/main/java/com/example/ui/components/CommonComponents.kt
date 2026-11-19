package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.MusicOff
import androidx.compose.material.icons.rounded.OfflineBolt
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.i18n.AppLanguage
import com.example.ui.i18n.LocalAppLanguage
import com.example.ui.i18n.LocalAppStrings
import com.example.ui.theme.AtpGold
import com.example.ui.theme.BioCyan
import com.example.ui.theme.BioCyanContainer
import com.example.ui.theme.ChloroplastEmerald
import com.example.ui.theme.CoralContainer
import com.example.ui.theme.CoralTrap
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.LabBorder
import com.example.ui.theme.LabSurface
import com.example.ui.theme.NucleusViolet
import com.example.ui.theme.ObsidianBg

@Composable
fun LabBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBg)
            .drawBehind {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0x282EE6D6), Color.Transparent),
                        center = Offset(size.width * 0.85f, size.height * 0.08f),
                        radius = size.width * 0.85f
                    )
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0x1E34D399), Color.Transparent),
                        center = Offset(size.width * 0.12f, size.height * 0.82f),
                        radius = size.width * 0.78f
                    )
                )
            },
        content = content
    )
}

@Composable
fun LabGlassCard(
    modifier: Modifier = Modifier,
    containerColor: Color = LabSurface,
    borderColor: Color = LabBorder,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        color = containerColor.copy(alpha = 0.94f),
        border = BorderStroke(1.dp, borderColor),
        tonalElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(contentPadding),
            content = content
        )
    }
}

@Composable
fun BadgeChip(
    text: String,
    textColor: Color = BioCyan,
    containerColor: Color = BioCyanContainer,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = containerColor,
        border = BorderStroke(1.dp, textColor.copy(alpha = 0.35f))
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = textColor,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun DifficultyBadge(difficulty: String, modifier: Modifier = Modifier) {
    val strings = LocalAppStrings.current
    val (fg, bg) = when (difficulty) {
        "دام کنکوری" -> CoralTrap to CoralContainer
        "بسیار سخت" -> AtpGold to GoldContainer
        else -> NucleusViolet to Color(0xFF281C4A)
    }
    BadgeChip(
        text = strings.localizeDifficulty(difficulty),
        textColor = fg,
        containerColor = bg,
        modifier = modifier
    )
}

@Composable
fun LabTopStatusBar(
    title: String,
    pianoEnabled: Boolean,
    onTogglePiano: () -> Unit,
    onSelectLanguage: ((AppLanguage) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val strings = LocalAppStrings.current
    val currentLang = LocalAppLanguage.current
    var langMenuExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.size(4.dp))
            Surface(
                shape = RoundedCornerShape(50),
                color = EmeraldContainer.copy(alpha = 0.8f),
                border = BorderStroke(1.dp, ChloroplastEmerald.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.OfflineBolt,
                        contentDescription = strings.offlineBadge,
                        tint = ChloroplastEmerald,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = strings.offlineBadge,
                        style = MaterialTheme.typography.labelSmall,
                        color = ChloroplastEmerald
                    )
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (onSelectLanguage != null) {
                Box {
                    Surface(
                        onClick = { langMenuExpanded = true },
                        shape = RoundedCornerShape(14.dp),
                        color = LabSurface,
                        border = BorderStroke(1.dp, BioCyan.copy(alpha = 0.45f)),
                        modifier = Modifier.testTag("top_language_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Language,
                                contentDescription = strings.languageSelectHint,
                                tint = BioCyan,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = currentLang.shortCode,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = BioCyan
                            )
                        }
                    }
                    DropdownMenu(
                        expanded = langMenuExpanded,
                        onDismissRequest = { langMenuExpanded = false }
                    ) {
                        AppLanguage.entries.forEach { lang ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "${lang.nativeName} (${lang.shortCode})",
                                        color = if (lang == currentLang) BioCyan else MaterialTheme.colorScheme.onBackground,
                                        fontWeight = if (lang == currentLang) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                onClick = {
                                    langMenuExpanded = false
                                    onSelectLanguage(lang)
                                },
                                modifier = Modifier.testTag("top_lang_option_${lang.code}")
                            )
                        }
                    }
                }
            }

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = if (pianoEnabled) BioCyanContainer else LabSurface,
                border = BorderStroke(
                    1.dp,
                    if (pianoEnabled) BioCyan.copy(alpha = 0.55f) else LabBorder
                )
            ) {
                IconButton(
                    onClick = onTogglePiano,
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("piano_toggle_button")
                ) {
                    Icon(
                        imageVector = if (pianoEnabled) Icons.Rounded.MusicNote else Icons.Rounded.MusicOff,
                        contentDescription = if (pianoEnabled) {
                            strings.pianoPlayingDesc
                        } else {
                            strings.pianoMutedDesc
                        },
                        tint = if (pianoEnabled) BioCyan else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
