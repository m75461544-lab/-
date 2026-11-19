package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.HighlightOff
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.TimerOff
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.i18n.LocalAppStrings
import com.example.ui.theme.AtpGold
import com.example.ui.theme.BioCyan
import com.example.ui.theme.ChloroplastEmerald
import com.example.ui.theme.CoralContainer
import com.example.ui.theme.CoralTrap
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.LabSurface
import com.example.ui.theme.ObsidianBg

enum class AnswerFeedbackStatus {
    CORRECT,
    INCORRECT,
    TIMED_OUT
}

/**
 * Material 3 Bioluminescent Answer Feedback Card with animated pulsing radial bloom,
 * luminous gradient border, status orb, streak/score chip, and Konkur explanation.
 */
@Composable
fun BioluminescentAnswerFeedbackCard(
    status: AnswerFeedbackStatus,
    correctOptionLabel: String,
    explanation: String,
    figure2dNote: String?,
    currentStreak: Int,
    modifier: Modifier = Modifier
) {
    val strings = LocalAppStrings.current

    val primaryGlow: Color
    val secondaryGlow: Color
    val containerSurface: Color
    val statusBadgeText: String
    val headlineText: String
    val statusIcon = when (status) {
        AnswerFeedbackStatus.CORRECT -> {
            primaryGlow = ChloroplastEmerald
            secondaryGlow = BioCyan
            containerSurface = EmeraldContainer.copy(alpha = 0.56f)
            statusBadgeText = strings.feedbackCorrectBadge
            headlineText = strings.feedbackCorrectTitle
            Icons.Rounded.CheckCircle
        }
        AnswerFeedbackStatus.INCORRECT -> {
            primaryGlow = CoralTrap
            secondaryGlow = AtpGold
            containerSurface = CoralContainer.copy(alpha = 0.56f)
            statusBadgeText = strings.feedbackWrongBadge
            headlineText = "${strings.feedbackWrongPrefix} ${strings.feedbackOptionWord} $correctOptionLabel"
            Icons.Rounded.HighlightOff
        }
        AnswerFeedbackStatus.TIMED_OUT -> {
            primaryGlow = CoralTrap
            secondaryGlow = AtpGold
            containerSurface = CoralContainer.copy(alpha = 0.52f)
            statusBadgeText = strings.feedbackTimeoutBadge
            headlineText = "${strings.feedbackTimeoutPrefix} ${strings.feedbackOptionWord} $correctOptionLabel"
            Icons.Rounded.TimerOff
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "bio_feedback_glow")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.24f,
        targetValue = 0.54f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )
    val glowRadiusFactor by infiniteTransition.animateFloat(
        initialValue = 0.72f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_radius"
    )

    val animatedPrimary by animateColorAsState(
        targetValue = primaryGlow,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "feedback_primary_color"
    )

    val outerShape = MaterialTheme.shapes.large

    Box(
        modifier = modifier
            .fillMaxWidth()
            .testTag("bioluminescent_feedback_card")
            .drawBehind {
                // Soft bioluminescent outer halo bloom
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            animatedPrimary.copy(alpha = pulseAlpha * 0.65f),
                            secondaryGlow.copy(alpha = pulseAlpha * 0.22f),
                            Color.Transparent
                        ),
                        center = Offset(size.width * 0.2f, size.height * 0.18f),
                        radius = size.maxDimension * 0.78f * glowRadiusFactor
                    )
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            secondaryGlow.copy(alpha = pulseAlpha * 0.32f),
                            Color.Transparent
                        ),
                        center = Offset(size.width * 0.88f, size.height * 0.82f),
                        radius = size.maxDimension * 0.58f
                    )
                )
            }
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = outerShape,
            color = LabSurface.copy(alpha = 0.94f),
            border = BorderStroke(
                width = 1.75.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        animatedPrimary.copy(alpha = 0.92f),
                        secondaryGlow.copy(alpha = 0.75f),
                        animatedPrimary.copy(alpha = 0.55f)
                    )
                )
            ),
            tonalElevation = 8.dp,
            shadowElevation = 10.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                containerSurface,
                                LabSurface.copy(alpha = 0.92f)
                            )
                        )
                    )
                    .padding(16.dp)
            ) {
                // Header: Glowing Orb + Title + Status Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Glowing M3 Status Orb
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .drawBehind {
                                drawCircle(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            animatedPrimary.copy(alpha = pulseAlpha),
                                            Color.Transparent
                                        ),
                                        radius = size.minDimension * 0.72f
                                    )
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = animatedPrimary.copy(alpha = 0.20f),
                            border = BorderStroke(1.5.dp, animatedPrimary.copy(alpha = 0.85f)),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = statusIcon,
                                    contentDescription = statusBadgeText,
                                    tint = animatedPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = animatedPrimary.copy(alpha = 0.18f),
                                border = BorderStroke(1.dp, animatedPrimary.copy(alpha = 0.55f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.AutoAwesome,
                                        contentDescription = null,
                                        tint = animatedPrimary,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = statusBadgeText,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = animatedPrimary
                                    )
                                }
                            }

                            if (status == AnswerFeedbackStatus.CORRECT && currentStreak > 0) {
                                Surface(
                                    shape = RoundedCornerShape(50),
                                    color = AtpGold.copy(alpha = 0.16f),
                                    border = BorderStroke(1.dp, AtpGold.copy(alpha = 0.5f))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.LocalFireDepartment,
                                            contentDescription = null,
                                            tint = AtpGold,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Text(
                                            text = "${strings.feedbackStreakBadge} $currentStreak",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = AtpGold
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = headlineText,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(
                    thickness = 1.dp,
                    color = animatedPrimary.copy(alpha = 0.28f)
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = explanation,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                if (!figure2dNote.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Figure2DExplanationBox(note = figure2dNote)
                }
            }
        }
    }
}

/**
 * Reusable glowing border modifier styling helper for quiz option cards.
 */
@Composable
fun rememberBioluminescentOptionStyle(
    submitted: Boolean,
    isSelected: Boolean,
    isCorrect: Boolean
): Triple<Brush, Color, Color> {
    val defaultBorder = MaterialTheme.colorScheme.outline
    val defaultText = MaterialTheme.colorScheme.onSurfaceVariant

    val borderStrokeWidth by animateDpAsState(
        targetValue = if (submitted && (isCorrect || isSelected)) 2.dp else 1.5.dp,
        label = "option_border_width"
    )
    // Used implicitly for recomposition smoothness
    val _unused = borderStrokeWidth

    return when {
        submitted && isCorrect -> Triple(
            Brush.linearGradient(listOf(ChloroplastEmerald, BioCyan)),
            EmeraldContainer.copy(alpha = 0.78f),
            ChloroplastEmerald
        )
        submitted && isSelected && !isCorrect -> Triple(
            Brush.linearGradient(listOf(CoralTrap, AtpGold)),
            CoralContainer.copy(alpha = 0.78f),
            CoralTrap
        )
        isSelected -> Triple(
            Brush.linearGradient(listOf(BioCyan, ChloroplastEmerald.copy(alpha = 0.8f))),
            Color(0xFF0E3342).copy(alpha = 0.78f),
            BioCyan
        )
        else -> Triple(
            SolidColor(defaultBorder),
            LabSurface.copy(alpha = 0.94f),
            defaultText
        )
    }
}
